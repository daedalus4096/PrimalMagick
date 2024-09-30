package com.verdantartifice.primalmagick.common.events;

import com.mojang.datafixers.util.Pair;
import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.attunements.AttunementManager;
import com.verdantartifice.primalmagick.common.attunements.AttunementThreshold;
import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.blocks.misc.GlowFieldBlock;
import com.verdantartifice.primalmagick.common.blockstates.properties.TimePhase;
import com.verdantartifice.primalmagick.common.books.BookLanguagesPM;
import com.verdantartifice.primalmagick.common.books.BooksPM;
import com.verdantartifice.primalmagick.common.books.LinguisticsManager;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerAttunements;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerCompanions;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerCooldowns;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerCooldowns.CooldownType;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerStats;
import com.verdantartifice.primalmagick.common.capabilities.ManaStorage;
import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.components.DataComponentsPM;
import com.verdantartifice.primalmagick.common.crafting.recipe_book.ArcaneRecipeBookManager;
import com.verdantartifice.primalmagick.common.effects.EffectsPM;
import com.verdantartifice.primalmagick.common.enchantments.EnchantmentHelperPM;
import com.verdantartifice.primalmagick.common.enchantments.EnchantmentsPM;
import com.verdantartifice.primalmagick.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagick.common.entities.companions.CompanionManager;
import com.verdantartifice.primalmagick.common.entities.misc.FriendlyWitchEntity;
import com.verdantartifice.primalmagick.common.items.ItemRegistration;
import com.verdantartifice.primalmagick.common.items.armor.WardingModuleItem;
import com.verdantartifice.primalmagick.common.items.books.StaticBookItem;
import com.verdantartifice.primalmagick.common.items.misc.DreamVisionTalismanItem;
import com.verdantartifice.primalmagick.common.misc.EntitySwapper;
import com.verdantartifice.primalmagick.common.misc.InteractionRecord;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.fx.PlayClientSoundPacket;
import com.verdantartifice.primalmagick.common.network.packets.misc.ResetFallDistancePacket;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.ResearchManager;
import com.verdantartifice.primalmagick.common.research.keys.ResearchStageKey;
import com.verdantartifice.primalmagick.common.research.keys.StackCraftedKey;
import com.verdantartifice.primalmagick.common.research.keys.TagCraftedKey;
import com.verdantartifice.primalmagick.common.sounds.SoundsPM;
import com.verdantartifice.primalmagick.common.sources.Sources;
import com.verdantartifice.primalmagick.common.stats.StatsManager;
import com.verdantartifice.primalmagick.common.util.EntityUtils;
import com.verdantartifice.primalmagick.common.util.InventoryUtils;
import com.verdantartifice.primalmagick.common.util.ItemUtils;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.NameTagItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Handlers for player related events.
 * 
 * @author Daedalus4096
 */
@Mod.EventBusSubscriber(modid= Constants.MOD_ID)
public class PlayerEvents {
    public static final Map<UUID, InteractionRecord> LAST_BLOCK_LEFT_CLICK = new HashMap<>();
    
    private static final Map<UUID, Boolean> DOUBLE_JUMP_ALLOWED = new HashMap<>();
    private static final Set<UUID> NEAR_DEATH_ELIGIBLE = new HashSet<>();
    private static final ResourceLocation STEP_MODIFIER_EARTH_ID = ResourceUtils.loc("step_modifier");
    private static final AttributeModifier STEP_MODIFIER_EARTH = new AttributeModifier(STEP_MODIFIER_EARTH_ID, 0.4D, AttributeModifier.Operation.ADD_VALUE);
    private static final ResearchStageKey SOURCE_EARTH_START = new ResearchStageKey(ResearchEntries.SOURCE_EARTH, 1);
    private static final ResearchStageKey SOURCE_EARTH_END = new ResearchStageKey(ResearchEntries.SOURCE_EARTH, 2);
    private static final ResearchStageKey SOURCE_SEA_START = new ResearchStageKey(ResearchEntries.SOURCE_SEA, 1);
    private static final ResearchStageKey SOURCE_SEA_END = new ResearchStageKey(ResearchEntries.SOURCE_SEA, 2);
    private static final ResearchStageKey SOURCE_SKY_START = new ResearchStageKey(ResearchEntries.SOURCE_SKY, 1);
    private static final ResearchStageKey SOURCE_SKY_END = new ResearchStageKey(ResearchEntries.SOURCE_SKY, 2);
    private static final ResearchStageKey SOURCE_SUN_START = new ResearchStageKey(ResearchEntries.SOURCE_SUN, 1);
    private static final ResearchStageKey SOURCE_SUN_END = new ResearchStageKey(ResearchEntries.SOURCE_SUN, 2);
    private static final ResearchStageKey SOURCE_MOON_START = new ResearchStageKey(ResearchEntries.SOURCE_MOON, 1);
    private static final ResearchStageKey SOURCE_MOON_END = new ResearchStageKey(ResearchEntries.SOURCE_MOON, 2);
    private static final Logger LOGGER = LogManager.getLogger();

    @SubscribeEvent
    public static void livingTick(LivingEvent.LivingTickEvent event) {
        Level level = event.getEntity().level();
        if (!level.isClientSide && (event.getEntity() instanceof ServerPlayer player)) {
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
                handleWardRegeneration(player);
            }
            if (player.tickCount % 200 == 0) {
                // Periodically check for environmentally-triggered research entries and for photosynthesis
                checkEnvironmentalResearch(player);
                handlePhotosynthesis(player);
            }
            if (player.tickCount % 1200 == 0) {
                // Periodically decay temporary attunements on the player
                AttunementManager.decayTemporaryAttunements(player);
            }
        }
        if (level.isClientSide && (event.getEntity() instanceof Player player)) {
            // If this is a client-side player, handle any double jumps from attunement bonuses
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
             ResearchManager.isResearchComplete(player, ResearchEntries.FIRST_STEPS) ) {
            if (!ResearchManager.isResearchComplete(player, ResearchEntries.NEAR_DEATH_EXPERIENCE)) {
                ResearchManager.completeResearch(player, ResearchEntries.NEAR_DEATH_EXPERIENCE);
            }
            NEAR_DEATH_ELIGIBLE.remove(playerId);
        }
    }

    protected static void applyAttunementBuffs(ServerPlayer player) {
        if (AttunementManager.meetsThreshold(player, Sources.SEA, AttunementThreshold.GREATER)) {
            // Apply Water Breathing for 30.5s if the player has greater sea attunement
            player.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 610, 0, true, false, true));
        }
        if (AttunementManager.meetsThreshold(player, Sources.MOON, AttunementThreshold.GREATER)) {
            // Apply Night Vision for 30.5s if the player has greater moon attunement
            player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 610, 0, true, false, true));
        }
        
        AttributeInstance stepHeightAttribute = player.getAttribute(Attributes.STEP_HEIGHT);
        stepHeightAttribute.removeModifier(STEP_MODIFIER_EARTH.id());
        if (!player.isShiftKeyDown() && AttunementManager.meetsThreshold(player, Sources.EARTH, AttunementThreshold.GREATER)) {
            // If the player has greater earth attunement and is not sneaking, boost their step height
            stepHeightAttribute.addTransientModifier(STEP_MODIFIER_EARTH);
        }
    }

    protected static void refreshWeakenedSoul(ServerPlayer player) {
        IPlayerCooldowns cooldowns = PrimalMagickCapabilities.getCooldowns(player);
        if (cooldowns != null) {
            long remaining = cooldowns.getRemainingCooldown(CooldownType.DEATH_SAVE);
            if (remaining > 0 && !player.hasEffect(EffectsPM.WEAKENED_SOUL.getHolder().get())) {
                // If the player's death save is on cooldown but they've cleared their marker debuff, reapply it
                player.addEffect(new MobEffectInstance(EffectsPM.WEAKENED_SOUL.getHolder().get(), Mth.ceil(remaining / 50.0F), 0, true, false, true));
            }
        }
    }

    protected static void doScheduledSyncs(ServerPlayer player, boolean immediate) {
        if (immediate || ResearchManager.isSyncScheduled(player)) {
            PrimalMagickCapabilities.getKnowledge(player).ifPresent(knowledge -> {
                knowledge.sync(player);
            });
        }
        if (immediate || StatsManager.isSyncScheduled(player)) {
            IPlayerStats stats = PrimalMagickCapabilities.getStats(player);
            if (stats != null) {
                stats.sync(player);
            }
        }
        if (immediate || AttunementManager.isSyncScheduled(player)) {
            IPlayerAttunements attunements = PrimalMagickCapabilities.getAttunements(player);
            if (attunements != null) {
                attunements.sync(player);
            }
        }
        if (immediate || CompanionManager.isSyncScheduled(player)) {
            IPlayerCompanions companions = PrimalMagickCapabilities.getCompanions(player);
            if (companions != null) {
                companions.sync(player);
            }
        }
        if (immediate || ArcaneRecipeBookManager.isSyncScheduled(player)) {
            PrimalMagickCapabilities.getArcaneRecipeBook(player).ifPresent(recipeBook -> {
                recipeBook.sync(player);
            });
        }
        if (immediate || LinguisticsManager.isSyncScheduled(player)) {
            PrimalMagickCapabilities.getLinguistics(player).ifPresent(linguistics -> {
                linguistics.sync(player);
            });
        }
        if (immediate) {
            // Cooldowns and wards don't do scheduled syncs, so only sync if it needs to be done immediately
            IPlayerCooldowns cooldowns = PrimalMagickCapabilities.getCooldowns(player);
            if (cooldowns != null) {
                cooldowns.sync(player);
            }
            PrimalMagickCapabilities.getWard(player).ifPresent(wardCap -> {
                wardCap.sync(player);
            });
        }
    }
    
    protected static void checkEnvironmentalResearch(ServerPlayer player) {
        PrimalMagickCapabilities.getKnowledge(player).ifPresent(knowledge -> {
            Level level = player.level();
            if (!ResearchManager.isResearchStarted(player, ResearchEntries.FIRST_STEPS)) {
                // Only check environmental research if the player has started progression
                return;
            }
            
            Holder<Biome> biomeHolder = level.getBiome(player.blockPosition());
            boolean inOverworld = level.dimension().equals(Level.OVERWORLD);
            
            if (!ResearchManager.isResearchStarted(player, ResearchEntries.DISCOVER_INFERNAL) && biomeHolder.is(BiomeTags.IS_NETHER)) {
                // If the player is in a Nether-based biome, discover the Infernal source
                ResearchManager.completeResearch(player, ResearchEntries.DISCOVER_INFERNAL);
                player.displayClientMessage(Component.translatable("event.primalmagick.discover_source.infernal").withStyle(ChatFormatting.GREEN), false);
            }
            if (!ResearchManager.isResearchStarted(player, ResearchEntries.DISCOVER_VOID) && biomeHolder.is(BiomeTags.IS_END)) {
                // If the player is in an End-based biome, discover the Void source
                ResearchManager.completeResearch(player, ResearchEntries.DISCOVER_VOID);
                player.displayClientMessage(Component.translatable("event.primalmagick.discover_source.void").withStyle(ChatFormatting.GREEN), false);
            }
            
            // If the player is working on the Earth Source research, check if they're far enough down
            if (SOURCE_EARTH_START.isKnownBy(player) && !SOURCE_EARTH_END.isKnownBy(player)) {
                if (player.position().y < -16.0D && inOverworld && !ResearchManager.isResearchStarted(player, ResearchEntries.ENV_EARTH)) {
                    ResearchManager.completeResearch(player, ResearchEntries.ENV_EARTH);
                    player.displayClientMessage(Component.translatable("event.primalmagick.env_earth").withStyle(ChatFormatting.GREEN), false);
                }
            }
            
            // If the player is working on the Sea Source research, check if they're in the ocean
            if (SOURCE_SEA_START.isKnownBy(player) && !SOURCE_SEA_END.isKnownBy(player)) {
                if (biomeHolder.is(BiomeTags.IS_OCEAN) && !ResearchManager.isResearchStarted(player, ResearchEntries.ENV_SEA)) {
                    ResearchManager.completeResearch(player, ResearchEntries.ENV_SEA);
                    player.displayClientMessage(Component.translatable("event.primalmagick.env_sea").withStyle(ChatFormatting.GREEN), false);
                }
            }
            
            // If the player is working on the Sky Source research, check if they're high up enough
            if (SOURCE_SKY_START.isKnownBy(player) && !SOURCE_SKY_END.isKnownBy(player)) {
                if (player.position().y > 128.0D && inOverworld && !ResearchManager.isResearchStarted(player, ResearchEntries.ENV_SKY)) {
                    ResearchManager.completeResearch(player, ResearchEntries.ENV_SKY);
                    player.displayClientMessage(Component.translatable("event.primalmagick.env_sky").withStyle(ChatFormatting.GREEN), false);
                }
            }
            
            // If the player is working on the Sun Source research, check if they're in the desert during the daytime
            if (SOURCE_SUN_START.isKnownBy(player) && !SOURCE_SUN_END.isKnownBy(player)) {
                if ((biomeHolder.is(Biomes.DESERT) || biomeHolder.is(BiomeTags.IS_BADLANDS)) && TimePhase.getSunPhase(level) == TimePhase.FULL && !ResearchManager.isResearchStarted(player, ResearchEntries.ENV_SUN)) {
                    ResearchManager.completeResearch(player, ResearchEntries.ENV_SUN);
                    player.displayClientMessage(Component.translatable("event.primalmagick.env_sun").withStyle(ChatFormatting.GREEN), false);
                }
            }
            
            // If the player is working on the Moon Source research, check if they're in the forest during the night-time
            if (SOURCE_MOON_START.isKnownBy(player) && !SOURCE_MOON_END.isKnownBy(player)) {
                if (biomeHolder.is(BiomeTags.IS_FOREST) && TimePhase.getMoonPhase(level) == TimePhase.FULL && !ResearchManager.isResearchStarted(player, ResearchEntries.ENV_MOON)) {
                    ResearchManager.completeResearch(player, ResearchEntries.ENV_MOON);
                    player.displayClientMessage(Component.translatable("event.primalmagick.env_moon").withStyle(ChatFormatting.GREEN), false);
                }
            }
        });
    }
    
    @SuppressWarnings("deprecation")
    protected static void handlePhotosynthesis(ServerPlayer player) {
        Level level = player.level();
        if (AttunementManager.meetsThreshold(player, Sources.SUN, AttunementThreshold.LESSER) && level.isDay() &&
                player.getLightLevelDependentMagicValue() > 0.5F && level.canSeeSky(player.blockPosition())) {
            // If an attuned player is outdoors during the daytime, restore some hunger
            player.getFoodData().eat(1, 0.3F);
        }
    }

    protected static void handleLightDrop(ServerPlayer player) {
        BlockPos pos = player.blockPosition();
        Level world = player.level();
        if (world.random.nextDouble() < 0.1D && 
                AttunementManager.meetsThreshold(player, Sources.SUN, AttunementThreshold.GREATER) && 
                !player.isShiftKeyDown() && 
                world.isEmptyBlock(pos) && 
                world.getBlockState(pos) != BlocksPM.GLOW_FIELD.get().defaultBlockState() &&
                world.getBrightness(LightLayer.BLOCK, pos) < 11) {
            // If an attuned, non-sneaking player is in a dark area, they have a chance to drop a sparkling glow field
            world.setBlock(pos, BlocksPM.GLOW_FIELD.get().defaultBlockState().setValue(GlowFieldBlock.SPARKLING, Boolean.TRUE), Block.UPDATE_ALL);
        }
    }

    protected static void handleDoubleJump(Player player) {
        Minecraft mc = Minecraft.getInstance();
        Level level = player.level();

        boolean jumpPressed = mc.options.keyJump.consumeClick();
        if (jumpPressed && !DOUBLE_JUMP_ALLOWED.containsKey(player.getUUID())) {
            DOUBLE_JUMP_ALLOWED.put(player.getUUID(), Boolean.TRUE);
        }
        if (jumpPressed && !player.onGround() && !player.isInWater() && 
                DOUBLE_JUMP_ALLOWED.getOrDefault(player.getUUID(), Boolean.FALSE).booleanValue() && 
                AttunementManager.meetsThreshold(player, Sources.SKY, AttunementThreshold.GREATER)) {
            // If the conditions are right, execute the second jump
            level.playLocalSound(player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, 
                    SoundSource.PLAYERS, 0.1F, 1.0F + (0.05F * (float)level.random.nextGaussian()), false);
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
        if (player.onGround() && DOUBLE_JUMP_ALLOWED.containsKey(player.getUUID())) {
            // Reset double jump permissions upon touching the ground
            DOUBLE_JUMP_ALLOWED.remove(player.getUUID());
        }
    }
    
    protected static void handleRegrowth(Player player) {
        if (player.level() instanceof ServerLevel serverLevel) {
            for (ItemStack stack : player.getAllSlots()) {
                if (stack.isDamaged() && EnchantmentHelperPM.hasRegrowth(stack, player.level().registryAccess())) {
                    stack.hurtAndBreak(-1, serverLevel, player instanceof ServerPlayer serverPlayer ? serverPlayer : null, item -> {});
                }
            }
        }
    }
    
    protected static void handleWardRegeneration(ServerPlayer player) {
        PrimalMagickCapabilities.getWard(player).ifPresent(wardCap -> {
            if (wardCap.isRegenerating()) {
                for (EquipmentSlot slot : wardCap.getApplicableSlots()) {
                    ItemStack slotStack = player.getItemBySlot(slot);
                    if (!slotStack.isEmpty()) {
                        ManaStorage manaCap = slotStack.get(DataComponentsPM.CAPABILITY_MANA_STORAGE.get());
                        if (manaCap != null && manaCap.getManaStored(Sources.EARTH) >= WardingModuleItem.REGEN_COST) {
                            // Consume mana from warded armor stacks to regenerate a single point of ward
                            manaCap.extractMana(Sources.EARTH, WardingModuleItem.REGEN_COST, false);
                            slotStack.set(DataComponentsPM.CAPABILITY_MANA_STORAGE.get(), manaCap);
                            wardCap.incrementCurrentWard();
                            wardCap.sync(player);
                            player.connection.send(new ClientboundSetEquipmentPacket(player.getId(), List.of(Pair.of(slot, slotStack.copy()))));
                            break;
                        }
                    }
                }
            }
        });
    }
    
    @SubscribeEvent
    public static void playerJoinEvent(EntityJoinLevelEvent event) {
        Level world = event.getLevel();
        if (!world.isClientSide && (event.getEntity() instanceof ServerPlayer player)) {
            // When a player first joins a world, sync that player's capabilities to their client
            doScheduledSyncs(player, true);
            
            // Also sync their arcane recipe book contents with their research
            ArcaneRecipeBookManager.syncRecipesWithResearch(player);
        }
    }
    
    @SuppressWarnings("deprecation")
    @SubscribeEvent
    public static void playerCloneEvent(PlayerEvent.Clone event) {
        // Preserve player capability data between deaths or returns from the End
        event.getOriginal().reviveCaps();   // FIXME Workaround for a Forge issue
        
        RegistryAccess registryAccess = event.getEntity().registryAccess();
        
        try {
            CompoundTag nbtKnowledge = PrimalMagickCapabilities.getKnowledge(event.getOriginal()).orElseThrow(IllegalArgumentException::new).serializeNBT(registryAccess);
            PrimalMagickCapabilities.getKnowledge(event.getEntity()).orElseThrow(IllegalArgumentException::new).deserializeNBT(registryAccess, nbtKnowledge);
        } catch (Exception e) {
            LOGGER.error("Failed to clone player {} knowledge", event.getOriginal().getName().getString());
        }
        
        try {
            CompoundTag nbtCooldowns = PrimalMagickCapabilities.getCooldowns(event.getOriginal()).serializeNBT(registryAccess);
            PrimalMagickCapabilities.getCooldowns(event.getEntity()).deserializeNBT(registryAccess, nbtCooldowns);
        } catch (Exception e) {
            LOGGER.error("Failed to clone player {} cooldowns", event.getOriginal().getName().getString());
        }
        
        try {
            CompoundTag nbtStats = PrimalMagickCapabilities.getStats(event.getOriginal()).serializeNBT(registryAccess);
            PrimalMagickCapabilities.getStats(event.getEntity()).deserializeNBT(registryAccess, nbtStats);
        } catch (Exception e) {
            LOGGER.error("Failed to clone player {} stats", event.getOriginal().getName().getString());
        }
        
        try {
            CompoundTag nbtAttunements = PrimalMagickCapabilities.getAttunements(event.getOriginal()).serializeNBT(registryAccess);
            PrimalMagickCapabilities.getAttunements(event.getEntity()).deserializeNBT(registryAccess, nbtAttunements);
        } catch (Exception e) {
            LOGGER.error("Failed to clone player {} attunements", event.getOriginal().getName().getString());
        }
        
        try {
            CompoundTag nbtCompanions = PrimalMagickCapabilities.getCompanions(event.getOriginal()).serializeNBT(registryAccess);
            PrimalMagickCapabilities.getCompanions(event.getEntity()).deserializeNBT(registryAccess, nbtCompanions);
        } catch (Exception e) {
            LOGGER.error("Failed to clone player {} companions", event.getOriginal().getName().getString());
        }
        
        try {
            CompoundTag nbtRecipeBook = PrimalMagickCapabilities.getArcaneRecipeBook(event.getOriginal()).orElseThrow(IllegalArgumentException::new).serializeNBT(registryAccess);
            PrimalMagickCapabilities.getArcaneRecipeBook(event.getEntity()).orElseThrow(IllegalArgumentException::new).deserializeNBT(registryAccess, nbtRecipeBook, event.getEntity().level().getRecipeManager());
        } catch (Exception e) {
            LOGGER.error("Failed to clone player {} arcane recipe book", event.getOriginal().getName().getString());
        }
        
        try {
            CompoundTag nbtWard = PrimalMagickCapabilities.getWard(event.getOriginal()).orElseThrow(IllegalArgumentException::new).serializeNBT(registryAccess);
            PrimalMagickCapabilities.getWard(event.getEntity()).orElseThrow(IllegalArgumentException::new).deserializeNBT(registryAccess, nbtWard);
        } catch (Exception e) {
            LOGGER.error("Failed to clone player {} ward", event.getOriginal().getName().getString());
        }
        
        try {
            CompoundTag nbtLinguistics = PrimalMagickCapabilities.getLinguistics(event.getOriginal()).orElseThrow(IllegalArgumentException::new).serializeNBT(registryAccess);
            PrimalMagickCapabilities.getLinguistics(event.getEntity()).orElseThrow(IllegalArgumentException::new).deserializeNBT(registryAccess, nbtLinguistics);
        } catch (Exception e) {
            LOGGER.error("Failed to clone player {} linguistics", event.getOriginal().getName().getString());
        }
        
        event.getOriginal().invalidateCaps();   // FIXME Remove when the reviveCaps call is removed
        
        // If the player died, refresh any attunement attribute modifiers they may have had
        if (event.isWasDeath()) {
            AttunementManager.refreshAttributeModifiers(event.getEntity());
        }
    }
    
    @SubscribeEvent
    public static void onCrafting(PlayerEvent.ItemCraftedEvent event) {
        registerItemCrafted(event.getEntity(), event.getCrafting().copy());
    }
    
    @SubscribeEvent
    public static void onSmelting(PlayerEvent.ItemSmeltedEvent event) {
        registerItemCrafted(event.getEntity(), event.getSmelting().copy());
    }
    
    protected static void registerItemCrafted(Player player, ItemStack stack) {
        if (player != null && !player.level().isClientSide) {
            // If a research entry requires crafting the item that was just crafted, grant the appropriate research
            if (ResearchManager.getAllCraftingReferences().contains(ItemUtils.getHashCode(stack))) {
                ResearchManager.completeResearch(player, new StackCraftedKey(stack));
            }
            
            // If a research entry requires crafting the a tag containing the item that was just crafted, grant the appropriate research
            stack.getTags().filter(tag -> tag != null).forEach(tagKey -> {
                int tagHash = tagKey.hashCode();
                if (ResearchManager.getAllCraftingReferences().contains(tagHash)) {
                    ResearchManager.completeResearch(player, new TagCraftedKey(tagKey));
                }
            });
        }
    }
    
    @SubscribeEvent
    public static void onWakeUp(PlayerWakeUpEvent event) {
        Player player = event.getEntity();
        if (player != null && !player.level().isClientSide) {
            if ( ResearchManager.isResearchComplete(player, ResearchEntries.FOUND_SHRINE) &&
                 !ResearchManager.isResearchComplete(player, ResearchEntries.GOT_DREAM) ) {
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
                    player.displayClientMessage(Component.translatable("event.primalmagick.dream_vision_talisman.drained").withStyle(ChatFormatting.GREEN), false);
                    if (player instanceof ServerPlayer serverPlayer) {
                        PacketHandler.sendToPlayer(new PlayClientSoundPacket(SoundsPM.WRITING.get(), 1.0F, 1.0F + (float)player.getRandom().nextGaussian() * 0.05F), serverPlayer);
                    }
                }
            }
        }
    }
    
    protected static void grantDreamJournal(Player player) {
        // First grant the appropriate research entry to continue FTUX
        ResearchManager.completeResearch(player, ResearchEntries.GOT_DREAM);
        
        // Construct the dream journal item
        ItemStack journal = StaticBookItem.builder(ItemRegistration.STATIC_BOOK, player.registryAccess()).book(BooksPM.DREAM_JOURNAL).language(BookLanguagesPM.DEFAULT).author(player.getName().getString()).build();
        
        // Give the dream journal to the player and announce it
        if (!player.addItem(journal)) {
            player.drop(journal, false);
        }
        player.sendSystemMessage(Component.translatable("event.primalmagick.got_dream").withStyle(ChatFormatting.GREEN));
    }
    
    @SubscribeEvent
    public static void onJump(LivingEvent.LivingJumpEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player)event.getEntity();
            if (AttunementManager.meetsThreshold(player, Sources.SKY, AttunementThreshold.GREATER)) {
                // Boost the player's vertical motion on jump if they have greater sky attunement
                Vec3 motion = player.getDeltaMovement();
                motion = motion.add(0.0D, 0.275D, 0.0D);
                player.setDeltaMovement(motion);
            }
        }
    }
    
    @SubscribeEvent
    public static void onPlayerInteractLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        LAST_BLOCK_LEFT_CLICK.put(event.getEntity().getUUID(), new InteractionRecord(event.getEntity(), event.getHand(), event.getPos(), event.getFace()));
    }
    
    @SubscribeEvent
    public static void onPickupExperience(PlayerXpEvent.PickupXp event) {
        Player player = event.getEntity();
        Level level = player.level();
        if (player != null && !level.isClientSide) {
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
    public static void onUseHoe(BlockEvent.BlockToolModificationEvent event) {
        UseOnContext context = event.getContext();
        ItemStack stack = context.getItemInHand();
        int enchantLevel = EnchantmentHelperPM.getEnchantmentLevel(stack, EnchantmentsPM.VERDANT, context.getPlayer().registryAccess());
        if (!event.isSimulated() && event.getToolAction().equals(ToolActions.HOE_TILL) && enchantLevel > 0) {
            Player player = event.getPlayer();
            Level level = context.getLevel();
            BlockPos pos = context.getClickedPos();
            BlockState state = level.getBlockState(pos);
            if (!player.isShiftKeyDown() && state.getBlock() instanceof BonemealableBlock mealBlock) {
                if (mealBlock.isValidBonemealTarget(level, pos, state)) {
                    if (level instanceof ServerLevel serverLevel) {
                        if (mealBlock.isBonemealSuccess(level, level.random, pos, state)) {
                            mealBlock.performBonemeal(serverLevel, level.random, pos, state);
                        }
                        
                        // Damage the stack and cancel the rest of the hoe functionality.
                        final int baseDamage = 8;
                        int damage = (baseDamage >> (enchantLevel - 1));
                        if (damage > 0) {
                            stack.hurtAndBreak(damage, player, LivingEntity.getSlotForHand(context.getHand()));
                        }
                        
                        // Explicitly set the final block state in the event so that the hoe's useOn method does not return PASS and
                        // the appropriate client/server syncing is performed.
                        event.setFinalState(level.getBlockState(pos));
                    }
                    if (!level.isClientSide) {
                        level.levelEvent(1505, pos, 0);
                    }
                }
            }
        }
    }
    
    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        ItemStack stack = event.getEntity().getItemInHand(event.getHand());
        Entity target = event.getTarget();
        Level level = target.level();
        
        // Befriend the targeted witch, if appropriate
        if ( !level.isClientSide && 
             target.getType() == EntityType.WITCH && 
             stack.getItem() instanceof NameTagItem && 
             stack.getHoverName().getString().equals(FriendlyWitchEntity.HONORED_NAME)) {
            CompoundTag originalData = target.saveWithoutId(new CompoundTag());
            EntitySwapper.enqueue(level, new EntitySwapper(target.getUUID(), EntityTypesPM.FRIENDLY_WITCH.get(), originalData, Optional.empty(), 0));
            List<Player> nearby = EntityUtils.getEntitiesInRange(level, target.position(), null, Player.class, 32.0D);
            for (Player player : nearby) {
                player.sendSystemMessage(Component.translatable("event.primalmagick.friendly_witch.spawn", FriendlyWitchEntity.HONORED_NAME));
            }
        }
    }
}
