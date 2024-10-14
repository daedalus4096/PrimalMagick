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
import com.verdantartifice.primalmagick.common.items.ItemsPM;
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
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.entity.player.PlayerWakeUpEvent;
import net.neoforged.neoforge.event.entity.player.PlayerXpEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
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
 * Neoforge listeners for player related events.
 * 
 * @author Daedalus4096
 */
@EventBusSubscriber(modid = Constants.MOD_ID)
public class PlayerEventListeners {
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
    public static void livingTick(EntityTickEvent.Post event) {
        PlayerEvents.livingTick(event.getEntity());
    }
    
    @SubscribeEvent
    public static void playerJoinEvent(EntityJoinLevelEvent event) {
        PlayerEvents.playerJoinEvent(event.getEntity(), event.getLevel());
    }
    
    @SubscribeEvent
    public static void playerCloneEvent(PlayerEvent.Clone event) {
        PlayerEvents.playerCloneEvent(event.getOriginal(), event.getEntity(), event.isWasDeath());
    }
    
    @SubscribeEvent
    public static void onCrafting(PlayerEvent.ItemCraftedEvent event) {
        PlayerEvents.registerItemCrafted(event.getEntity(), event.getCrafting().copy());
    }
    
    @SubscribeEvent
    public static void onSmelting(PlayerEvent.ItemSmeltedEvent event) {
        PlayerEvents.registerItemCrafted(event.getEntity(), event.getSmelting().copy());
    }
    
    @SubscribeEvent
    public static void onWakeUp(PlayerWakeUpEvent event) {
        PlayerEvents.onWakeUp(event.getEntity());
    }
    
    @SubscribeEvent
    public static void onJump(LivingEvent.LivingJumpEvent event) {
        PlayerEvents.onJump(event.getEntity());
    }
    
    @SubscribeEvent
    public static void onPlayerInteractLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        PlayerEvents.onPlayerInteractLeftClickBlock(event.getEntity(), event.getHand(), event.getPos(), event.getFace());
    }
    
    @SubscribeEvent
    public static void onPickupExperience(PlayerXpEvent.PickupXp event) {
        PlayerEvents.onPickupExperience(event.getEntity(), event.getOrb());
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
