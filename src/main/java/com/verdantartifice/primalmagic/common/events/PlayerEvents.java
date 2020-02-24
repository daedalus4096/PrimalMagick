package com.verdantartifice.primalmagic.common.events;

import java.util.HashMap;
import java.util.Map;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.attunements.AttunementManager;
import com.verdantartifice.primalmagic.common.attunements.AttunementThreshold;
import com.verdantartifice.primalmagic.common.blocks.BlocksPM;
import com.verdantartifice.primalmagic.common.blockstates.properties.TimePhase;
import com.verdantartifice.primalmagic.common.capabilities.IPlayerAttunements;
import com.verdantartifice.primalmagic.common.capabilities.IPlayerCooldowns;
import com.verdantartifice.primalmagic.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagic.common.capabilities.IPlayerStats;
import com.verdantartifice.primalmagic.common.capabilities.PrimalMagicCapabilities;
import com.verdantartifice.primalmagic.common.capabilities.IPlayerCooldowns.CooldownType;
import com.verdantartifice.primalmagic.common.effects.EffectsPM;
import com.verdantartifice.primalmagic.common.network.PacketHandler;
import com.verdantartifice.primalmagic.common.network.packets.misc.ResetFallDistancePacket;
import com.verdantartifice.primalmagic.common.research.ResearchManager;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.stats.StatsManager;
import com.verdantartifice.primalmagic.common.util.ItemUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Handlers for player related events.
 * 
 * @author Daedalus4096
 */
@Mod.EventBusSubscriber(modid=PrimalMagic.MODID)
public class PlayerEvents {
    private static final Map<Integer, Float> PREV_STEP_HEIGHTS = new HashMap<>();
    private static final Map<Integer, Boolean> DOUBLE_JUMP_ALLOWED = new HashMap<>();
    
    @SubscribeEvent
    public static void livingTick(LivingEvent.LivingUpdateEvent event) {
        if (!event.getEntity().world.isRemote && (event.getEntity() instanceof ServerPlayerEntity)) {
            ServerPlayerEntity player = (ServerPlayerEntity)event.getEntity();
            if (player.ticksExisted % 5 == 0) {
                // Apply any earned buffs for attunements
                applyAttunementBuffs(player);
                refreshWeakenedSoul(player);
            }
            if (player.ticksExisted % 10 == 0) {
                // Check to see if any players need their capabilities synced to their clients
                doScheduledSyncs(player, false);
            }
            if (player.ticksExisted % 20 == 0) {
                // Periodically check to see if attuned players should drop a light source
                handleLightDrop(player);
            }
            if (player.ticksExisted % 200 == 0) {
                // Periodically check for environmentally-triggered research entries and for photosynthesis
                checkEnvironmentalResearch(player);
                handlePhotosynthesis(player);
            }
            if (player.ticksExisted % 1200 == 0) {
                // Periodically decay temporary attunements on the player
                AttunementManager.decayTemporaryAttunements(player);
            }
        }
        if (event.getEntity().world.isRemote && (event.getEntity() instanceof PlayerEntity)) {
            // If this is a client-side player, handle any step-height changes and double jumps from attunement bonuses
            PlayerEntity player = (PlayerEntity)event.getEntity();
            handleStepHeightChange(player);
            handleDoubleJump(player);
        }
    }
    
    protected static void applyAttunementBuffs(ServerPlayerEntity player) {
        if (AttunementManager.meetsThreshold(player, Source.SEA, AttunementThreshold.LESSER)) {
            // Apply Dolphin's Grace for 30.5s if the player has lesser sea attunement
            player.addPotionEffect(new EffectInstance(Effects.DOLPHINS_GRACE, 610, 0, true, false, true));
        }
        if (AttunementManager.meetsThreshold(player, Source.SEA, AttunementThreshold.GREATER)) {
            // Apply Water Breathing for 30.5s if the player has greater sea attunement
            player.addPotionEffect(new EffectInstance(Effects.WATER_BREATHING, 610, 0, true, false, true));
        }
        if (AttunementManager.meetsThreshold(player, Source.MOON, AttunementThreshold.GREATER)) {
            // Apply Night Vision for 30.5s if the player has greater moon attunement
            player.addPotionEffect(new EffectInstance(Effects.NIGHT_VISION, 610, 0, true, false, true));
        }
    }

    protected static void refreshWeakenedSoul(ServerPlayerEntity player) {
        IPlayerCooldowns cooldowns = PrimalMagicCapabilities.getCooldowns(player);
        if (cooldowns != null) {
            long remaining = cooldowns.getRemainingCooldown(CooldownType.DEATH_SAVE);
            if (remaining > 0 && !player.isPotionActive(EffectsPM.WEAKENED_SOUL.get())) {
                // If the player's death save is on cooldown but they've cleared their marker debuff, reapply it
                player.addPotionEffect(new EffectInstance(EffectsPM.WEAKENED_SOUL.get(), MathHelper.ceil(remaining / 50.0F), 0, true, false, true));
            }
        }
    }

    protected static void doScheduledSyncs(ServerPlayerEntity player, boolean immediate) {
        if (immediate || ResearchManager.isSyncScheduled(player)) {
            IPlayerKnowledge knowledge = PrimalMagicCapabilities.getKnowledge(player);
            if (knowledge != null) {
                knowledge.sync(player);
            }
        }
        if (immediate || StatsManager.isSyncScheduled(player)) {
            IPlayerStats stats = PrimalMagicCapabilities.getStats(player);
            if (stats != null) {
                stats.sync(player);
            }
        }
        if (immediate || AttunementManager.isSyncScheduled(player)) {
            IPlayerAttunements attunements = PrimalMagicCapabilities.getAttunements(player);
            if (attunements != null) {
                attunements.sync(player);
            }
        }
        if (immediate) {
            // Cooldowns don't do scheduled syncs, so only sync if it needs to be done immediately
            IPlayerCooldowns cooldowns = PrimalMagicCapabilities.getCooldowns(player);
            if (cooldowns != null) {
                cooldowns.sync(player);
            }
        }
    }
    
    protected static void checkEnvironmentalResearch(ServerPlayerEntity player) {
        IPlayerKnowledge knowledge = PrimalMagicCapabilities.getKnowledge(player);
        if (knowledge == null || !knowledge.isResearchKnown(SimpleResearchKey.parse("FIRST_STEPS"))) {
            // Only check environmental research if the player has started progression
            return;
        }
        
        Biome biome = player.world.getBiome(player.getPosition());
        if (!knowledge.isResearchKnown(Source.INFERNAL.getDiscoverKey()) && BiomeDictionary.hasType(biome, BiomeDictionary.Type.NETHER)) {
            // If the player is in a Nether-based biome, discover the Infernal source
            ResearchManager.completeResearch(player, Source.INFERNAL.getDiscoverKey());
            player.sendStatusMessage(new TranslationTextComponent("event.primalmagic.discover_source.infernal").applyTextStyle(TextFormatting.GREEN), false);
        }
        if (!knowledge.isResearchKnown(Source.VOID.getDiscoverKey()) && BiomeDictionary.hasType(biome, BiomeDictionary.Type.END)) {
            // If the player is in an End-based biome, discover the Void source
            ResearchManager.completeResearch(player, Source.VOID.getDiscoverKey());
            player.sendStatusMessage(new TranslationTextComponent("event.primalmagic.discover_source.void").applyTextStyle(TextFormatting.GREEN), false);
        }
        
        // If the player is working on the Earth Source research, check if they're far enough down
        if (knowledge.isResearchKnown(SimpleResearchKey.parse("SOURCE_EARTH@1")) && !knowledge.isResearchKnown(SimpleResearchKey.parse("SOURCE_EARTH@2"))) {
            SimpleResearchKey key = SimpleResearchKey.parse("m_env_earth");
            if (player.getPositionVec().y < 10.0D && !knowledge.isResearchKnown(key)) {
                ResearchManager.completeResearch(player, key);
                player.sendStatusMessage(new TranslationTextComponent("event.primalmagic.env_earth").applyTextStyle(TextFormatting.GREEN), false);
            }
        }
        
        // If the player is working on the Sea Source research, check if they're in the ocean
        if (knowledge.isResearchKnown(SimpleResearchKey.parse("SOURCE_SEA@1")) && !knowledge.isResearchKnown(SimpleResearchKey.parse("SOURCE_SEA@2"))) {
            SimpleResearchKey key = SimpleResearchKey.parse("m_env_sea");
            if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.OCEAN) && !knowledge.isResearchKnown(key)) {
                ResearchManager.completeResearch(player, key);
                player.sendStatusMessage(new TranslationTextComponent("event.primalmagic.env_sea").applyTextStyle(TextFormatting.GREEN), false);
            }
        }
        
        // If the player is working on the Sky Source research, check if they're high up enough
        if (knowledge.isResearchKnown(SimpleResearchKey.parse("SOURCE_SKY@1")) && !knowledge.isResearchKnown(SimpleResearchKey.parse("SOURCE_SKY@2"))) {
            SimpleResearchKey key = SimpleResearchKey.parse("m_env_sky");
            if (player.getPositionVec().y > 100.0D && !knowledge.isResearchKnown(key)) {
                ResearchManager.completeResearch(player, key);
                player.sendStatusMessage(new TranslationTextComponent("event.primalmagic.env_sky").applyTextStyle(TextFormatting.GREEN), false);
            }
        }
        
        // If the player is working on the Sun Source research, check if they're in the desert during the daytime
        if (knowledge.isResearchKnown(SimpleResearchKey.parse("SOURCE_SUN@1")) && !knowledge.isResearchKnown(SimpleResearchKey.parse("SOURCE_SUN@2"))) {
            SimpleResearchKey key = SimpleResearchKey.parse("m_env_sun");
            if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.SANDY) && TimePhase.getSunPhase(player.world) == TimePhase.FULL && !knowledge.isResearchKnown(key)) {
                ResearchManager.completeResearch(player, key);
                player.sendStatusMessage(new TranslationTextComponent("event.primalmagic.env_sun").applyTextStyle(TextFormatting.GREEN), false);
            }
        }
        
        // If the player is working on the Moon Source research, check if they're in the forest during the night-time
        if (knowledge.isResearchKnown(SimpleResearchKey.parse("SOURCE_MOON@1")) && !knowledge.isResearchKnown(SimpleResearchKey.parse("SOURCE_MOON@2"))) {
            SimpleResearchKey key = SimpleResearchKey.parse("m_env_moon");
            if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.FOREST) && TimePhase.getMoonPhase(player.world) == TimePhase.FULL && !knowledge.isResearchKnown(key)) {
                ResearchManager.completeResearch(player, key);
                player.sendStatusMessage(new TranslationTextComponent("event.primalmagic.env_moon").applyTextStyle(TextFormatting.GREEN), false);
            }
        }
    }
    
    protected static void handlePhotosynthesis(ServerPlayerEntity player) {
        if (AttunementManager.meetsThreshold(player, Source.SUN, AttunementThreshold.LESSER) && player.world.isDaytime() &&
                player.getBrightness() > 0.5F && player.world.canSeeSky(player.getPosition())) {
            // If an attuned player is outdoors during the daytime, restore some hunger
            player.getFoodStats().addStats(1, 0.3F);
        }
    }

    protected static void handleLightDrop(ServerPlayerEntity player) {
        BlockPos pos = player.getPosition();
        World world = player.world;
        if (world.rand.nextDouble() < 0.1D && 
                AttunementManager.meetsThreshold(player, Source.SUN, AttunementThreshold.GREATER) && 
                !player.isShiftKeyDown() && 
                world.isAirBlock(pos) && 
                world.getBlockState(pos) != BlocksPM.GLOW_FIELD.get().getDefaultState() && 
                world.getLightFor(LightType.BLOCK, pos) < 11) {
            // If an attuned, non-sneaking player is in a dark area, they have a chance to drop a glow field
            world.setBlockState(pos, BlocksPM.GLOW_FIELD.get().getDefaultState(), Constants.BlockFlags.DEFAULT);
        }
    }

    protected static void handleStepHeightChange(PlayerEntity player) {
        if (!player.isShiftKeyDown() && AttunementManager.meetsThreshold(player, Source.EARTH, AttunementThreshold.GREATER)) {
            // If the player has greater earth attunement and is not sneaking, boost their step height and save the old one
            if (!PREV_STEP_HEIGHTS.containsKey(Integer.valueOf(player.getEntityId()))) {
                PREV_STEP_HEIGHTS.put(Integer.valueOf(player.getEntityId()), Float.valueOf(player.stepHeight));
            }
            player.stepHeight = 1.0F;
        } else {
            // Otherwise, check to see if their step height needs to be reset
            if (PREV_STEP_HEIGHTS.containsKey(Integer.valueOf(player.getEntityId()))) {
                player.stepHeight = PREV_STEP_HEIGHTS.remove(Integer.valueOf(player.getEntityId()));
            }
        }
    }

    protected static void handleDoubleJump(PlayerEntity player) {
        boolean jumpPressed = Minecraft.getInstance().gameSettings.keyBindJump.isPressed();
        if (jumpPressed && !DOUBLE_JUMP_ALLOWED.containsKey(player.getEntityId())) {
            DOUBLE_JUMP_ALLOWED.put(player.getEntityId(), Boolean.TRUE);
        }
        if (jumpPressed && !player.onGround && !player.isInWater() && 
                DOUBLE_JUMP_ALLOWED.getOrDefault(player.getEntityId(), Boolean.FALSE).booleanValue() && 
                AttunementManager.meetsThreshold(player, Source.SKY, AttunementThreshold.GREATER)) {
            // If the conditions are right, execute the second jump
            player.world.playSound(player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 
                    SoundCategory.PLAYERS, 0.1F, 1.0F + (0.05F * (float)player.world.rand.nextGaussian()), false);
            DOUBLE_JUMP_ALLOWED.put(player.getEntityId(), Boolean.FALSE);
            
            // Update motion
            Vec3d oldMotion = player.getMotion();
            double motionX = oldMotion.x;
            double motionY = 0.75D;
            double motionZ = oldMotion.z;
            if (player.isPotionActive(Effects.JUMP_BOOST)) {
                motionY += (0.1D * (1 + player.getActivePotionEffect(Effects.JUMP_BOOST).getAmplifier()));
            }
            if (player.isSprinting()) {
                float yawRadians = player.rotationYaw * (float)(Math.PI / 180.0D);
                motionX -= (0.2D * MathHelper.sin(yawRadians));
                motionZ += (0.2D * MathHelper.cos(yawRadians));
            }
            player.setMotion(motionX, motionY, motionZ);
            
            // Reset fall distance
            player.fallDistance = 0.0F;
            PacketHandler.sendToServer(new ResetFallDistancePacket());
            
            // Trigger jump events
            ForgeHooks.onLivingJump(player);
        }
        if (player.onGround && DOUBLE_JUMP_ALLOWED.containsKey(player.getEntityId())) {
            // Reset double jump permissions upon touching the ground
            DOUBLE_JUMP_ALLOWED.remove(player.getEntityId());
        }
    }
    
    @SubscribeEvent
    public static void playerJoinEvent(EntityJoinWorldEvent event) {
        if (!event.getWorld().isRemote && (event.getEntity() instanceof ServerPlayerEntity)) {
            // When a player first joins a world, sync that player's capabilities to their client
            ServerPlayerEntity player = (ServerPlayerEntity)event.getEntity();
            doScheduledSyncs(player, true);
        }
    }
    
    @SubscribeEvent
    public static void playerCloneEvent(PlayerEvent.Clone event) {
        // Preserve player capability data between deaths
        if (event.isWasDeath()) {
            try {
                CompoundNBT nbtKnowledge = PrimalMagicCapabilities.getKnowledge(event.getOriginal()).serializeNBT();
                PrimalMagicCapabilities.getKnowledge(event.getPlayer()).deserializeNBT(nbtKnowledge);
            } catch (Exception e) {
                PrimalMagic.LOGGER.error("Failed to clone player {} knowledge", event.getOriginal().getName().getString());
            }
            
            try {
                CompoundNBT nbtCooldowns = PrimalMagicCapabilities.getCooldowns(event.getOriginal()).serializeNBT();
                PrimalMagicCapabilities.getCooldowns(event.getPlayer()).deserializeNBT(nbtCooldowns);
            } catch (Exception e) {
                PrimalMagic.LOGGER.error("Failed to clone player {} cooldowns", event.getOriginal().getName().getString());
            }
            
            try {
                CompoundNBT nbtStats = PrimalMagicCapabilities.getStats(event.getOriginal()).serializeNBT();
                PrimalMagicCapabilities.getStats(event.getPlayer()).deserializeNBT(nbtStats);
            } catch (Exception e) {
                PrimalMagic.LOGGER.error("Failed to clone player {} stats", event.getOriginal().getName().getString());
            }
            
            try {
                CompoundNBT nbtAttunements = PrimalMagicCapabilities.getAttunements(event.getOriginal()).serializeNBT();
                PrimalMagicCapabilities.getAttunements(event.getPlayer()).deserializeNBT(nbtAttunements);
            } catch (Exception e) {
                PrimalMagic.LOGGER.error("Failed to clone player {} attunements", event.getOriginal().getName().getString());
            }
        }
    }
    
    @SubscribeEvent
    public static void onCrafting(PlayerEvent.ItemCraftedEvent event) {
        if (event.getPlayer() != null && !event.getPlayer().world.isRemote) {
            ItemStack stack = event.getCrafting().copy();
            int stackHash = ItemUtils.getHashCode(stack);
            
            // If a research entry requires crafting the item that was just crafted, grant the appropriate research
            if (ResearchManager.getAllCraftingReferences().contains(Integer.valueOf(stackHash))) {
                ResearchManager.completeResearch(event.getPlayer(), SimpleResearchKey.parse("[#]" + stackHash));
            }
            
            // If a research entry requires crafting the a tag containing the item that was just crafted, grant the appropriate research
            for (ResourceLocation tag : stack.getItem().getTags()) {
                if (tag != null) {
                    int tagHash = ("tag:" + tag.toString()).hashCode();
                    if (ResearchManager.getAllCraftingReferences().contains(Integer.valueOf(tagHash))) {
                        ResearchManager.completeResearch(event.getPlayer(), SimpleResearchKey.parse("[#]" + tagHash));
                    }
                }
            }
        }
    }
    
    @SubscribeEvent
    public static void onWakeUp(PlayerWakeUpEvent event) {
        PlayerEntity player = event.getPlayer();
        if (player != null && !player.world.isRemote) {
            if ( ResearchManager.isResearchComplete(player, SimpleResearchKey.parse("m_found_shrine")) &&
                 !ResearchManager.isResearchComplete(player, SimpleResearchKey.parse("t_got_dream")) ) {
                // If the player is at the appropriate point of the FTUX, grant them the dream journal and research
                grantDreamJournal(player);
            }
        }
    }
    
    protected static void grantDreamJournal(PlayerEntity player) {
        // First grant the appropriate research entry to continue FTUX
        ResearchManager.completeResearch(player, SimpleResearchKey.parse("t_got_dream"));
        
        // Construct the dream journal item
        ItemStack journal = new ItemStack(Items.WRITTEN_BOOK);
        CompoundNBT contents = new CompoundNBT();
        contents.putInt("generation", 3);
        contents.putString("title", new TranslationTextComponent("primalmagic.dream_journal.title").getFormattedText());
        contents.putString("author", player.getName().getFormattedText());
        ListNBT pages = new ListNBT();
        pages.add(StringNBT.valueOf(new TranslationTextComponent("primalmagic.dream_journal.text.1").getFormattedText()));
        pages.add(StringNBT.valueOf(new TranslationTextComponent("primalmagic.dream_journal.text.2").getFormattedText()));
        pages.add(StringNBT.valueOf(new TranslationTextComponent("primalmagic.dream_journal.text.3").getFormattedText()));
        contents.put("pages", pages);
        journal.setTag(contents);
        
        // Give the dream journal to the player and announce it
        if (!player.addItemStackToInventory(journal)) {
            player.dropItem(journal, false);
        }
        player.sendMessage(new TranslationTextComponent("event.primalmagic.got_dream").applyTextStyle(TextFormatting.GREEN));
    }
    
    @SubscribeEvent
    public static void onJump(LivingEvent.LivingJumpEvent event) {
        if (event.getEntityLiving() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity)event.getEntityLiving();
            if (AttunementManager.meetsThreshold(player, Source.SKY, AttunementThreshold.GREATER)) {
                // Boost the player's vertical motion on jump if they have greater sky attunement
                Vec3d motion = player.getMotion();
                motion = motion.add(0.0D, 0.275D, 0.0D);
                player.setMotion(motion);
            }
        }
    }
}
