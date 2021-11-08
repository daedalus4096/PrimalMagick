package com.verdantartifice.primalmagick.common.events;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.verdantartifice.primalmagick.PrimalMagic;
import com.verdantartifice.primalmagick.common.attunements.AttunementManager;
import com.verdantartifice.primalmagick.common.attunements.AttunementThreshold;
import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.blockstates.properties.TimePhase;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerAttunements;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerCompanions;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerCooldowns;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerStats;
import com.verdantartifice.primalmagick.common.capabilities.PrimalMagicCapabilities;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerCooldowns.CooldownType;
import com.verdantartifice.primalmagick.common.crafting.recipe_book.ArcaneRecipeBookManager;
import com.verdantartifice.primalmagick.common.effects.EffectsPM;
import com.verdantartifice.primalmagick.common.enchantments.EnchantmentHelperPM;
import com.verdantartifice.primalmagick.common.enchantments.EnchantmentsPM;
import com.verdantartifice.primalmagick.common.enchantments.VerdantEnchantment;
import com.verdantartifice.primalmagick.common.entities.companions.CompanionManager;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.items.misc.DreamVisionTalismanItem;
import com.verdantartifice.primalmagick.common.misc.InteractionRecord;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.fx.PlayClientSoundPacket;
import com.verdantartifice.primalmagick.common.network.packets.misc.ResetFallDistancePacket;
import com.verdantartifice.primalmagick.common.research.ResearchManager;
import com.verdantartifice.primalmagick.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagick.common.sounds.SoundsPM;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.stats.StatsManager;
import com.verdantartifice.primalmagick.common.util.InventoryUtils;
import com.verdantartifice.primalmagick.common.util.ItemUtils;

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Handlers for player related events.
 * 
 * @author Daedalus4096
 */
@SuppressWarnings("deprecation")
@Mod.EventBusSubscriber(modid=PrimalMagic.MODID)
public class PlayerEvents {
    public static final Map<UUID, InteractionRecord> LAST_BLOCK_LEFT_CLICK = new HashMap<>();
    
    private static final Map<UUID, Float> PREV_STEP_HEIGHTS = new HashMap<>();
    private static final Map<UUID, Boolean> DOUBLE_JUMP_ALLOWED = new HashMap<>();
    private static final Set<UUID> NEAR_DEATH_ELIGIBLE = new HashSet<>();
    private static final SimpleResearchKey NDE_RESEARCH_KEY = SimpleResearchKey.parse("m_near_death_experience");
    private static final Logger LOGGER = LogManager.getLogger();

    @SubscribeEvent
    public static void livingTick(LivingEvent.LivingUpdateEvent event) {
        if (!event.getEntity().level.isClientSide && (event.getEntity() instanceof ServerPlayer)) {
            ServerPlayer player = (ServerPlayer)event.getEntity();
            checkNearDeathExperience(player);
            if (player.tickCount % 5 == 0) {
                // Apply any earned buffs for attunements
                applyAttunementBuffs(player);
                refreshWeakenedSoul(player);
            }
            if (player.tickCount % 10 == 0) {
                // Check to see if any players need their capabilities synced to their clients
                doScheduledSyncs(player, false);
            }
            if (player.tickCount % 20 == 0) {
                // Periodically check to see if attuned players should drop a light source or if regrowing equipment should mend
                handleLightDrop(player);
                handleRegrowth(player);
            }
            if (player.tickCount % 200 == 0) {
                // Periodically check for environmentally-triggered research entries and for photosynthesis
                checkEnvironmentalResearch(player);
                checkVanillaStatistics(player);
                handlePhotosynthesis(player);
            }
            if (player.tickCount % 1200 == 0) {
                // Periodically decay temporary attunements on the player
                AttunementManager.decayTemporaryAttunements(player);
            }
        }
        if (event.getEntity().level.isClientSide && (event.getEntity() instanceof Player)) {
            // If this is a client-side player, handle any step-height changes and double jumps from attunement bonuses
            Player player = (Player)event.getEntity();
            handleStepHeightChange(player);
            handleDoubleJump(player);
        }
    }
    
    protected static void checkNearDeathExperience(ServerPlayer player) {
        float health = player.getHealth();
        UUID playerId = player.getUUID();
        if (health > 0.0F && health <= 6.0F && !NEAR_DEATH_ELIGIBLE.contains(playerId)) {
            NEAR_DEATH_ELIGIBLE.add(playerId);
        }
        if (health <= 0.0F && NEAR_DEATH_ELIGIBLE.contains(playerId)) {
            NEAR_DEATH_ELIGIBLE.remove(playerId);
        }
        if ( NEAR_DEATH_ELIGIBLE.contains(playerId) && 
             health >= player.getMaxHealth() &&
             ResearchManager.isResearchComplete(player, SimpleResearchKey.FIRST_STEPS) ) {
            if (!ResearchManager.isResearchComplete(player, NDE_RESEARCH_KEY)) {
                ResearchManager.completeResearch(player, NDE_RESEARCH_KEY);
            }
            NEAR_DEATH_ELIGIBLE.remove(playerId);
        }
    }

    protected static void applyAttunementBuffs(ServerPlayer player) {
        if (AttunementManager.meetsThreshold(player, Source.SEA, AttunementThreshold.LESSER)) {
            // Apply Dolphin's Grace for 30.5s if the player has lesser sea attunement
            player.addEffect(new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 610, 0, true, false, true));
        }
        if (AttunementManager.meetsThreshold(player, Source.SEA, AttunementThreshold.GREATER)) {
            // Apply Water Breathing for 30.5s if the player has greater sea attunement
            player.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 610, 0, true, false, true));
        }
        if (AttunementManager.meetsThreshold(player, Source.MOON, AttunementThreshold.GREATER)) {
            // Apply Night Vision for 30.5s if the player has greater moon attunement
            player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 610, 0, true, false, true));
        }
    }

    protected static void refreshWeakenedSoul(ServerPlayer player) {
        IPlayerCooldowns cooldowns = PrimalMagicCapabilities.getCooldowns(player);
        if (cooldowns != null) {
            long remaining = cooldowns.getRemainingCooldown(CooldownType.DEATH_SAVE);
            if (remaining > 0 && !player.hasEffect(EffectsPM.WEAKENED_SOUL.get())) {
                // If the player's death save is on cooldown but they've cleared their marker debuff, reapply it
                player.addEffect(new MobEffectInstance(EffectsPM.WEAKENED_SOUL.get(), Mth.ceil(remaining / 50.0F), 0, true, false, true));
            }
        }
    }

    protected static void doScheduledSyncs(ServerPlayer player, boolean immediate) {
        if (immediate || ResearchManager.isSyncScheduled(player)) {
            PrimalMagicCapabilities.getKnowledge(player).ifPresent(knowledge -> {
                knowledge.sync(player);
            });
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
        if (immediate || CompanionManager.isSyncScheduled(player)) {
            IPlayerCompanions companions = PrimalMagicCapabilities.getCompanions(player);
            if (companions != null) {
                companions.sync(player);
            }
        }
        if (immediate || ArcaneRecipeBookManager.isSyncScheduled(player)) {
            PrimalMagicCapabilities.getArcaneRecipeBook(player).ifPresent(recipeBook -> {
                recipeBook.sync(player);
            });
        }
        if (immediate) {
            // Cooldowns don't do scheduled syncs, so only sync if it needs to be done immediately
            IPlayerCooldowns cooldowns = PrimalMagicCapabilities.getCooldowns(player);
            if (cooldowns != null) {
                cooldowns.sync(player);
            }
        }
    }
    
    protected static void checkEnvironmentalResearch(ServerPlayer player) {
        PrimalMagicCapabilities.getKnowledge(player).ifPresent(knowledge -> {
            if (!knowledge.isResearchKnown(SimpleResearchKey.FIRST_STEPS)) {
                // Only check environmental research if the player has started progression
                return;
            }
            
            Biome biome = player.level.getBiome(player.blockPosition());
            boolean inOverworld = player.level.dimension().equals(Level.OVERWORLD);
            
            if (!knowledge.isResearchKnown(Source.INFERNAL.getDiscoverKey()) && Biome.BiomeCategory.NETHER.equals(biome.getBiomeCategory())) {
                // If the player is in a Nether-based biome, discover the Infernal source
                ResearchManager.completeResearch(player, Source.INFERNAL.getDiscoverKey());
                ResearchManager.completeResearch(player, SimpleResearchKey.parse("t_discover_forbidden"));
                player.displayClientMessage(new TranslatableComponent("event.primalmagic.discover_source.infernal").withStyle(ChatFormatting.GREEN), false);
            }
            if (!knowledge.isResearchKnown(Source.VOID.getDiscoverKey()) && Biome.BiomeCategory.THEEND.equals(biome.getBiomeCategory())) {
                // If the player is in an End-based biome, discover the Void source
                ResearchManager.completeResearch(player, Source.VOID.getDiscoverKey());
                ResearchManager.completeResearch(player, SimpleResearchKey.parse("t_discover_forbidden"));
                player.displayClientMessage(new TranslatableComponent("event.primalmagic.discover_source.void").withStyle(ChatFormatting.GREEN), false);
            }
            
            // If the player is working on the Earth Source research, check if they're far enough down
            if (knowledge.isResearchKnown(SimpleResearchKey.parse("SOURCE_EARTH@1")) && !knowledge.isResearchKnown(SimpleResearchKey.parse("SOURCE_EARTH@2"))) {
                SimpleResearchKey key = SimpleResearchKey.parse("m_env_earth");
                if (player.position().y < 10.0D && inOverworld && !knowledge.isResearchKnown(key)) {
                    ResearchManager.completeResearch(player, key);
                    player.displayClientMessage(new TranslatableComponent("event.primalmagic.env_earth").withStyle(ChatFormatting.GREEN), false);
                }
            }
            
            // If the player is working on the Sea Source research, check if they're in the ocean
            if (knowledge.isResearchKnown(SimpleResearchKey.parse("SOURCE_SEA@1")) && !knowledge.isResearchKnown(SimpleResearchKey.parse("SOURCE_SEA@2"))) {
                SimpleResearchKey key = SimpleResearchKey.parse("m_env_sea");
                if (Biome.BiomeCategory.OCEAN.equals(biome.getBiomeCategory()) && !knowledge.isResearchKnown(key)) {
                    ResearchManager.completeResearch(player, key);
                    player.displayClientMessage(new TranslatableComponent("event.primalmagic.env_sea").withStyle(ChatFormatting.GREEN), false);
                }
            }
            
            // If the player is working on the Sky Source research, check if they're high up enough
            if (knowledge.isResearchKnown(SimpleResearchKey.parse("SOURCE_SKY@1")) && !knowledge.isResearchKnown(SimpleResearchKey.parse("SOURCE_SKY@2"))) {
                SimpleResearchKey key = SimpleResearchKey.parse("m_env_sky");
                if (player.position().y > 100.0D && inOverworld && !knowledge.isResearchKnown(key)) {
                    ResearchManager.completeResearch(player, key);
                    player.displayClientMessage(new TranslatableComponent("event.primalmagic.env_sky").withStyle(ChatFormatting.GREEN), false);
                }
            }
            
            // If the player is working on the Sun Source research, check if they're in the desert during the daytime
            if (knowledge.isResearchKnown(SimpleResearchKey.parse("SOURCE_SUN@1")) && !knowledge.isResearchKnown(SimpleResearchKey.parse("SOURCE_SUN@2"))) {
                SimpleResearchKey key = SimpleResearchKey.parse("m_env_sun");
                if (Biome.BiomeCategory.DESERT.equals(biome.getBiomeCategory()) && TimePhase.getSunPhase(player.level) == TimePhase.FULL && !knowledge.isResearchKnown(key)) {
                    ResearchManager.completeResearch(player, key);
                    player.displayClientMessage(new TranslatableComponent("event.primalmagic.env_sun").withStyle(ChatFormatting.GREEN), false);
                }
            }
            
            // If the player is working on the Moon Source research, check if they're in the forest during the night-time
            if (knowledge.isResearchKnown(SimpleResearchKey.parse("SOURCE_MOON@1")) && !knowledge.isResearchKnown(SimpleResearchKey.parse("SOURCE_MOON@2"))) {
                SimpleResearchKey key = SimpleResearchKey.parse("m_env_moon");
                if (Biome.BiomeCategory.FOREST.equals(biome.getBiomeCategory()) && TimePhase.getMoonPhase(player.level) == TimePhase.FULL && !knowledge.isResearchKnown(key)) {
                    ResearchManager.completeResearch(player, key);
                    player.displayClientMessage(new TranslatableComponent("event.primalmagic.env_moon").withStyle(ChatFormatting.GREEN), false);
                }
            }
        });
    }
    
    protected static void checkVanillaStatistics(ServerPlayer player) {
        if (!ResearchManager.isResearchComplete(player, SimpleResearchKey.parse("m_fly_elytra")) && player.getStats().getValue(Stats.CUSTOM.get(Stats.AVIATE_ONE_CM)) >= 100000) {
            ResearchManager.completeResearch(player, SimpleResearchKey.parse("m_fly_elytra"));
        }
        if (!ResearchManager.isResearchComplete(player, SimpleResearchKey.parse("b_place_torch_expert")) && player.getStats().getValue(Stats.ITEM_USED.get(Items.TORCH)) >= 100) {
            ResearchManager.completeResearch(player, SimpleResearchKey.parse("b_place_torch_expert"));
        }
        if (!ResearchManager.isResearchComplete(player, SimpleResearchKey.parse("b_place_stone_expert")) &&
                (player.getStats().getValue(Stats.ITEM_USED.get(Items.STONE)) + player.getStats().getValue(Stats.ITEM_USED.get(Items.COBBLESTONE))) >= 100) {
            ResearchManager.completeResearch(player, SimpleResearchKey.parse("b_place_stone_expert"));
        }
    }

    protected static void handlePhotosynthesis(ServerPlayer player) {
        if (AttunementManager.meetsThreshold(player, Source.SUN, AttunementThreshold.LESSER) && player.level.isDay() &&
                player.getBrightness() > 0.5F && player.level.canSeeSky(player.blockPosition())) {
            // If an attuned player is outdoors during the daytime, restore some hunger
            player.getFoodData().eat(1, 0.3F);
        }
    }

    protected static void handleLightDrop(ServerPlayer player) {
        BlockPos pos = player.blockPosition();
        Level world = player.level;
        if (world.random.nextDouble() < 0.1D && 
                AttunementManager.meetsThreshold(player, Source.SUN, AttunementThreshold.GREATER) && 
                !player.isShiftKeyDown() && 
                world.isEmptyBlock(pos) && 
                world.getBlockState(pos) != BlocksPM.GLOW_FIELD.get().defaultBlockState() && 
                world.getBrightness(LightLayer.BLOCK, pos) < 11) {
            // If an attuned, non-sneaking player is in a dark area, they have a chance to drop a glow field
            world.setBlock(pos, BlocksPM.GLOW_FIELD.get().defaultBlockState(), Constants.BlockFlags.DEFAULT);
        }
    }

    protected static void handleStepHeightChange(Player player) {
        if (!player.isShiftKeyDown() && AttunementManager.meetsThreshold(player, Source.EARTH, AttunementThreshold.GREATER)) {
            // If the player has greater earth attunement and is not sneaking, boost their step height and save the old one
            if (!PREV_STEP_HEIGHTS.containsKey(player.getUUID())) {
                PREV_STEP_HEIGHTS.put(player.getUUID(), Float.valueOf(player.maxUpStep));
            }
            player.maxUpStep = Math.max(1.0F, player.maxUpStep);
        } else {
            // Otherwise, check to see if their step height needs to be reset
            if (PREV_STEP_HEIGHTS.containsKey(player.getUUID())) {
                player.maxUpStep = PREV_STEP_HEIGHTS.remove(player.getUUID());
            }
        }
    }

    protected static void handleDoubleJump(Player player) {
        Minecraft mc = Minecraft.getInstance();
        boolean jumpPressed = mc.options.keyJump.consumeClick();
        if (jumpPressed && !DOUBLE_JUMP_ALLOWED.containsKey(player.getUUID())) {
            DOUBLE_JUMP_ALLOWED.put(player.getUUID(), Boolean.TRUE);
        }
        if (jumpPressed && !player.isOnGround() && !player.isInWater() && 
                DOUBLE_JUMP_ALLOWED.getOrDefault(player.getUUID(), Boolean.FALSE).booleanValue() && 
                AttunementManager.meetsThreshold(player, Source.SKY, AttunementThreshold.GREATER)) {
            // If the conditions are right, execute the second jump
            player.level.playLocalSound(player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, 
                    SoundSource.PLAYERS, 0.1F, 1.0F + (0.05F * (float)player.level.random.nextGaussian()), false);
            DOUBLE_JUMP_ALLOWED.put(player.getUUID(), Boolean.FALSE);
            
            // Update motion
            Vec3 oldMotion = player.getDeltaMovement();
            double motionX = oldMotion.x;
            double motionY = 0.75D;
            double motionZ = oldMotion.z;
            if (player.hasEffect(MobEffects.JUMP)) {
                motionY += (0.1D * (1 + player.getEffect(MobEffects.JUMP).getAmplifier()));
            }
            if (player.isSprinting()) {
                float yawRadians = player.getYRot() * (float)(Math.PI / 180.0D);
                motionX -= (0.2D * Mth.sin(yawRadians));
                motionZ += (0.2D * Mth.cos(yawRadians));
            }
            player.setDeltaMovement(motionX, motionY, motionZ);
            
            // Reset fall distance
            player.fallDistance = 0.0F;
            PacketHandler.sendToServer(new ResetFallDistancePacket());
            
            // Trigger jump events
            ForgeHooks.onLivingJump(player);
        }
        if (player.isOnGround() && DOUBLE_JUMP_ALLOWED.containsKey(player.getUUID())) {
            // Reset double jump permissions upon touching the ground
            DOUBLE_JUMP_ALLOWED.remove(player.getUUID());
        }
    }
    
    protected static void handleRegrowth(Player player) {
        for (ItemStack stack : player.getAllSlots()) {
            if (stack.isDamaged() && EnchantmentHelperPM.hasRegrowth(stack)) {
                stack.hurtAndBreak(-1, player, p -> {});
            }
        }
    }
    
    @SubscribeEvent
    public static void playerJoinEvent(EntityJoinWorldEvent event) {
        Level world = event.getWorld();
        if (!world.isClientSide && (event.getEntity() instanceof ServerPlayer player)) {
            // When a player first joins a world, sync that player's capabilities to their client
            doScheduledSyncs(player, true);
            
            // Also sync their arcane recipe book contents with their research
            ArcaneRecipeBookManager.syncRecipesWithResearch(player);
        }
    }
    
    @SubscribeEvent
    public static void playerCloneEvent(PlayerEvent.Clone event) {
        // Preserve player capability data between deaths or returns from the End
        event.getOriginal().reviveCaps();   // FIXME Workaround for a Forge issue
        
        try {
            CompoundTag nbtKnowledge = PrimalMagicCapabilities.getKnowledge(event.getOriginal()).orElseThrow(IllegalArgumentException::new).serializeNBT();
            PrimalMagicCapabilities.getKnowledge(event.getPlayer()).orElseThrow(IllegalArgumentException::new).deserializeNBT(nbtKnowledge);
        } catch (Exception e) {
            LOGGER.error("Failed to clone player {} knowledge", event.getOriginal().getName().getString());
        }
        
        try {
            CompoundTag nbtCooldowns = PrimalMagicCapabilities.getCooldowns(event.getOriginal()).serializeNBT();
            PrimalMagicCapabilities.getCooldowns(event.getPlayer()).deserializeNBT(nbtCooldowns);
        } catch (Exception e) {
            LOGGER.error("Failed to clone player {} cooldowns", event.getOriginal().getName().getString());
        }
        
        try {
            CompoundTag nbtStats = PrimalMagicCapabilities.getStats(event.getOriginal()).serializeNBT();
            PrimalMagicCapabilities.getStats(event.getPlayer()).deserializeNBT(nbtStats);
        } catch (Exception e) {
            LOGGER.error("Failed to clone player {} stats", event.getOriginal().getName().getString());
        }
        
        try {
            CompoundTag nbtAttunements = PrimalMagicCapabilities.getAttunements(event.getOriginal()).serializeNBT();
            PrimalMagicCapabilities.getAttunements(event.getPlayer()).deserializeNBT(nbtAttunements);
        } catch (Exception e) {
            LOGGER.error("Failed to clone player {} attunements", event.getOriginal().getName().getString());
        }
        
        try {
            CompoundTag nbtCompanions = PrimalMagicCapabilities.getCompanions(event.getOriginal()).serializeNBT();
            PrimalMagicCapabilities.getCompanions(event.getPlayer()).deserializeNBT(nbtCompanions);
        } catch (Exception e) {
            LOGGER.error("Failed to clone player {} companions", event.getOriginal().getName().getString());
        }
        
        try {
            CompoundTag nbtRecipeBook = PrimalMagicCapabilities.getArcaneRecipeBook(event.getOriginal()).orElseThrow(IllegalArgumentException::new).serializeNBT();
            PrimalMagicCapabilities.getArcaneRecipeBook(event.getPlayer()).orElseThrow(IllegalArgumentException::new).deserializeNBT(nbtRecipeBook, event.getPlayer().level.getRecipeManager());
        } catch (Exception e) {
            LOGGER.error("Failed to clone player {} arcane recipe book", event.getOriginal().getName().getString());
        }
        
        event.getOriginal().invalidateCaps();   // FIXME Remove when the reviveCaps call is removed
    }
    
    @SubscribeEvent
    public static void onCrafting(PlayerEvent.ItemCraftedEvent event) {
        registerItemCrafted(event.getPlayer(), event.getCrafting().copy());
    }
    
    @SubscribeEvent
    public static void onSmelting(PlayerEvent.ItemSmeltedEvent event) {
        registerItemCrafted(event.getPlayer(), event.getSmelting().copy());
    }
    
    protected static void registerItemCrafted(Player player, ItemStack stack) {
        if (player != null && !player.level.isClientSide) {
            int stackHash = ItemUtils.getHashCode(stack);
            
            // If a research entry requires crafting the item that was just crafted, grant the appropriate research
            if (ResearchManager.getAllCraftingReferences().contains(Integer.valueOf(stackHash))) {
                ResearchManager.completeResearch(player, SimpleResearchKey.parseCrafted(stackHash));
            }
            
            // If a research entry requires crafting the a tag containing the item that was just crafted, grant the appropriate research
            for (ResourceLocation tag : stack.getItem().getTags()) {
                if (tag != null) {
                    int tagHash = ("tag:" + tag.toString()).hashCode();
                    if (ResearchManager.getAllCraftingReferences().contains(Integer.valueOf(tagHash))) {
                        ResearchManager.completeResearch(player, SimpleResearchKey.parseCrafted(tagHash));
                    }
                }
            }
        }
    }
    
    @SubscribeEvent
    public static void onWakeUp(PlayerWakeUpEvent event) {
        Player player = event.getPlayer();
        if (player != null && !player.level.isClientSide) {
            if ( ResearchManager.isResearchComplete(player, SimpleResearchKey.parse("m_found_shrine")) &&
                 !ResearchManager.isResearchComplete(player, SimpleResearchKey.parse("t_got_dream")) ) {
                // If the player is at the appropriate point of the FTUX, grant them the dream journal and research
                grantDreamJournal(player);
            }
            
            NonNullList<ItemStack> foundTalismans = InventoryUtils.find(player, ItemsPM.DREAM_VISION_TALISMAN.get().getDefaultInstance());
            if (!foundTalismans.isEmpty()) {
                // Drain any full Dream Vision Talismans upon waking to grant new observation knowledge
                boolean success = false;
                for (ItemStack talismanStack : foundTalismans) {
                    if (talismanStack.getItem() instanceof DreamVisionTalismanItem talisman && talisman.isActive(talismanStack) && talisman.isReadyToDrain(talismanStack)) {
                        success = success || talisman.doDrain(talismanStack, player);
                    }
                }
                if (success) {
                    // Only show success effects once, regardless of how many talismans were triggered
                    player.displayClientMessage(new TranslatableComponent("event.primalmagic.dream_vision_talisman.drained").withStyle(ChatFormatting.GREEN), false);
                    if (player instanceof ServerPlayer serverPlayer) {
                        PacketHandler.sendToPlayer(new PlayClientSoundPacket(SoundsPM.WRITING.get(), 1.0F, 1.0F + (float)player.getRandom().nextGaussian() * 0.05F), serverPlayer);
                    }
                }
            }
        }
    }
    
    protected static void grantDreamJournal(Player player) {
        // First grant the appropriate research entry to continue FTUX
        ResearchManager.completeResearch(player, SimpleResearchKey.parse("t_got_dream"));
        
        // Construct the dream journal item
        ItemStack journal = new ItemStack(Items.WRITTEN_BOOK);
        CompoundTag contents = new CompoundTag();
        contents.putInt("generation", 3);
        contents.putString("title", new TranslatableComponent("primalmagic.dream_journal.title").getString());
        contents.putString("author", player.getName().getString());
        ListTag pages = new ListTag();
        pages.add(StringTag.valueOf(new TranslatableComponent("primalmagic.dream_journal.text.1").getString()));
        pages.add(StringTag.valueOf(new TranslatableComponent("primalmagic.dream_journal.text.2").getString()));
        pages.add(StringTag.valueOf(new TranslatableComponent("primalmagic.dream_journal.text.3").getString()));
        contents.put("pages", pages);
        journal.setTag(contents);
        
        // Give the dream journal to the player and announce it
        if (!player.addItem(journal)) {
            player.drop(journal, false);
        }
        player.sendMessage(new TranslatableComponent("event.primalmagic.got_dream").withStyle(ChatFormatting.GREEN), Util.NIL_UUID);
    }
    
    @SubscribeEvent
    public static void onJump(LivingEvent.LivingJumpEvent event) {
        if (event.getEntityLiving() instanceof Player) {
            Player player = (Player)event.getEntityLiving();
            if (AttunementManager.meetsThreshold(player, Source.SKY, AttunementThreshold.GREATER)) {
                // Boost the player's vertical motion on jump if they have greater sky attunement
                Vec3 motion = player.getDeltaMovement();
                motion = motion.add(0.0D, 0.275D, 0.0D);
                player.setDeltaMovement(motion);
            }
        }
    }
    
    @SubscribeEvent
    public static void onPlayerInteractLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        LAST_BLOCK_LEFT_CLICK.put(event.getPlayer().getUUID(), new InteractionRecord(event.getPlayer(), event.getHand(), event.getPos(), event.getFace()));
    }
    
    @SubscribeEvent
    public static void onPickupExperience(PlayerXpEvent.PickupXp event) {
        Player player = event.getPlayer();
        if (player != null && !player.level.isClientSide) {
            NonNullList<ItemStack> foundTalismans = InventoryUtils.find(player, ItemsPM.DREAM_VISION_TALISMAN.get().getDefaultInstance());
            if (!foundTalismans.isEmpty()) {
                int xpValue = event.getOrb().value;
                for (ItemStack foundStack : foundTalismans) {
                    if (foundStack.getItem() instanceof DreamVisionTalismanItem talisman && talisman.isActive(foundStack)) {
                        // Add as much experience as possible from the orb to each active talisman until the orb runs out
                        xpValue = talisman.addStoredExp(foundStack, xpValue);
                        if (xpValue <= 0) {
                            event.getOrb().value = 0;
                            return;
                        }
                    }
                }
                
                // If we made it through every talisman with experience left over, update the orb to be the leftover value
                event.getOrb().value = xpValue;
            }
        }
    }
    
    @SubscribeEvent
    public static void onUseHoe(UseHoeEvent event) {
        ItemStack stack = event.getContext().getItemInHand();
        int enchantLevel = EnchantmentHelper.getItemEnchantmentLevel(EnchantmentsPM.VERDANT.get(), stack);
        if (enchantLevel > 0) {
            Player player = event.getPlayer();
            Level level = event.getContext().getLevel();
            BlockPos pos = event.getContext().getClickedPos();
            BlockState state = level.getBlockState(pos);
            if (!player.isShiftKeyDown() && state.getBlock() instanceof BonemealableBlock mealBlock) {
                if (mealBlock.isValidBonemealTarget(level, pos, state, level.isClientSide)) {
                    if (level instanceof ServerLevel serverLevel) {
                        if (mealBlock.isBonemealSuccess(level, level.random, pos, state)) {
                            mealBlock.performBonemeal(serverLevel, level.random, pos, state);
                        }
                        
                        // Damage the stack; do one less damage here than needed, as setting the event result to ALLOW will cause
                        // one damage to be applied automatically.
                        int damage = (VerdantEnchantment.BASE_DAMAGE_PER_USE >> (enchantLevel - 1)) - 1;
                        if (damage > 0) {
                            stack.hurtAndBreak(damage, player, p -> p.broadcastBreakEvent(event.getContext().getHand()));
                        }
                        
                        // Setting an ALLOW result causes the rest of the hoe functionality to be skipped, and one damage to be
                        // applied to the stack.
                        event.setResult(Event.Result.ALLOW);
                    }
                    if (!level.isClientSide) {
                        level.levelEvent(1505, pos, 0);
                    }
                }
            }
        }
    }
}
