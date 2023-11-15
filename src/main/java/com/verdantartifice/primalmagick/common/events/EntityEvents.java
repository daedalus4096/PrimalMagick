package com.verdantartifice.primalmagick.common.events;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.blocks.misc.EnderwardBlock;
import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.effects.EffectsPM;
import com.verdantartifice.primalmagick.common.enchantments.EnchantmentsPM;
import com.verdantartifice.primalmagick.common.items.armor.WardingModuleItem;
import com.verdantartifice.primalmagick.common.research.ResearchManager;
import com.verdantartifice.primalmagick.common.research.ResearchNames;
import com.verdantartifice.primalmagick.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagick.common.stats.StatsManager;
import com.verdantartifice.primalmagick.common.stats.StatsPM;
import com.verdantartifice.primalmagick.common.tags.DamageTypeTagsPM;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraftforge.event.entity.living.AnimalTameEvent;
import net.minecraftforge.event.entity.living.BabyEntitySpawnEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.LootingLevelEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Handlers for miscellaneous entity events.
 * 
 * @author Daedalus4096
 */
@Mod.EventBusSubscriber(modid=PrimalMagick.MODID)
public class EntityEvents {
    @SubscribeEvent
    public static void onEnderEntityTeleport(EntityTeleportEvent.EnderEntity event) {
        // Prevent the teleport if the teleporter is afflicted with Enderlock
        if (event.isCancelable() && event.getEntityLiving().hasEffect(EffectsPM.ENDERLOCK.get())) {
            event.setCanceled(true);
        }
        
        // Check to see if an enderward blocks the teleport
        checkEnderward(event, event.getEntityLiving());
    }
    
    @SubscribeEvent
    public static void onEnderPearlTeleport(EntityTeleportEvent.EnderPearl event) {
        // Prevent the teleport if the teleporter is afflicted with Enderlock
        if (event.isCancelable() && event.getPlayer().hasEffect(EffectsPM.ENDERLOCK.get())) {
            event.setCanceled(true);
        }
        
        // Check to see if an enderward blocks the teleport
        checkEnderward(event, event.getPlayer());
    }
    
    @SubscribeEvent
    public static void onChorusFruitTeleport(EntityTeleportEvent.ChorusFruit event) {
        // Prevent the teleport if the teleporter is afflicted with Enderlock
        if (event.isCancelable() && event.getEntityLiving().hasEffect(EffectsPM.ENDERLOCK.get())) {
            event.setCanceled(true);
        }
        
        // Check to see if an enderward blocks the teleport
        checkEnderward(event, event.getEntityLiving());
    }
    
    private static void checkEnderward(EntityTeleportEvent event, LivingEntity entity) {
        if (event.isCancelable() && !event.isCanceled()) {
            double edgeLength = 2D * EnderwardBlock.EFFECT_RADIUS;
            AABB searchAABB = AABB.ofSize(event.getTarget(), edgeLength, edgeLength, edgeLength);
            if (BlockPos.betweenClosedStream(searchAABB).anyMatch(pos -> entity.level().getBlockState(pos).is(BlocksPM.ENDERWARD.get()))) {
                event.setCanceled(true);
                if (entity instanceof Player player) {
                    player.displayClientMessage(Component.translatable("event.primalmagick.enderward.block").withStyle(ChatFormatting.RED), true);
                }
            }
        }
    }
    
    @SubscribeEvent(priority=EventPriority.LOWEST)
    public static void onEnderPearlTeleportLowest(EntityTeleportEvent.EnderPearl event) {
        // Keep track of the distance teleported for stats
        Player entity = event.getPlayer();
        Vec3 start = entity.position();
        Vec3 end = new Vec3(event.getTargetX(), event.getTargetY(), event.getTargetZ());
        StatsManager.incrementValue((Player)entity, StatsPM.DISTANCE_TELEPORTED_CM, (int)(100 * start.distanceTo(end)));
    }
    
    @SubscribeEvent(priority=EventPriority.LOWEST)
    public static void onChorusFruitTeleportLowest(EntityTeleportEvent.ChorusFruit event) {
        // Keep track of the distance teleported for stats
        LivingEntity entity = event.getEntityLiving();
        if (!event.isCanceled() && entity instanceof Player) {
            Vec3 start = entity.position();
            Vec3 end = new Vec3(event.getTargetX(), event.getTargetY(), event.getTargetZ());
            StatsManager.incrementValue((Player)entity, StatsPM.DISTANCE_TELEPORTED_CM, (int)(100 * start.distanceTo(end)));
        }
    }
    
    @SubscribeEvent(priority=EventPriority.LOWEST)
    public static void onAnimalTameLowest(AnimalTameEvent event) {
        // Grant appropriate research if a player tames a wolf
        SimpleResearchKey tameKey = ResearchNames.INTERNAL_FURRY_FRIEND.get().simpleKey();
        Player player = event.getTamer();
        if ( !event.isCanceled() &&
             event.getAnimal() instanceof Wolf && 
             ResearchManager.isResearchComplete(player, SimpleResearchKey.FIRST_STEPS) && 
             !ResearchManager.isResearchComplete(player, tameKey) ) {
            ResearchManager.completeResearch(player, tameKey);
        }
    }
    
    @SubscribeEvent(priority=EventPriority.LOWEST)
    public static void onBabyEntitySpawnLowest(BabyEntitySpawnEvent event) {
        // Grant appropriate research if a player breeds an animal
        SimpleResearchKey breedKey = ResearchNames.INTERNAL_BREED_ANIMAL.get().simpleKey();
        Player player = event.getCausedByPlayer();
        if ( !event.isCanceled() && 
             player != null &&
             ResearchManager.isResearchComplete(player, SimpleResearchKey.FIRST_STEPS) &&
             !ResearchManager.isResearchComplete(player, breedKey) ) {
            ResearchManager.completeResearch(player, breedKey);
        }
    }
    
    @SubscribeEvent
    public static void onLivingEntityUseItemTick(LivingEntityUseItemEvent.Tick event) {
        // Stack up resistance on the wielders of shields with the Bulwark enchantment
        LivingEntity entity = event.getEntity();
        ItemStack stack = event.getItem();
        int currentDuration = event.getDuration();
        int maxDuration = stack.getUseDuration();
        int delta = maxDuration - currentDuration;
        int enchantLevel = stack.getEnchantmentLevel(EnchantmentsPM.BULWARK.get());
        if (stack.getItem() instanceof ShieldItem && delta > 0 && delta % 5 == 0 && enchantLevel > 0) {
            MobEffectInstance effectInstance = entity.getEffect(MobEffects.DAMAGE_RESISTANCE);
            int amplifier = (effectInstance == null) ? 0 : Mth.clamp(1 + effectInstance.getAmplifier(), 0, enchantLevel - 1);
            entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 10, amplifier));
        }
    }
    
    @SubscribeEvent
    public static void onLootingLevel(LootingLevelEvent event) {
        // If the damage was magickal, apply the Treasure enchantment as a looting modifier if greater than what's already there
        DamageSource source = event.getDamageSource();
        if (source != null && source.is(DamageTypeTagsPM.IS_MAGIC)) {
            Entity caster = source.getEntity();
            if (caster != null && caster instanceof LivingEntity living) {
                event.setLootingLevel(Math.max(event.getLootingLevel(), EnchantmentHelper.getEnchantmentLevel(EnchantmentsPM.TREASURE.get(), living)));
            }
        }
    }
    
    @SubscribeEvent
    public static void onLivingEquipmentChange(LivingEquipmentChangeEvent event) {
        // If a player is donning or removing a warded piece of armor, update their max ward level
        if (event.getEntity() instanceof ServerPlayer serverPlayer &&
                (WardingModuleItem.hasWardAttached(event.getFrom()) || WardingModuleItem.hasWardAttached(event.getTo()) ) ) {
            PrimalMagickCapabilities.getWard(serverPlayer).ifPresent(playerWard -> {
                int newMax = playerWard.getApplicableSlots().stream().map(slot -> serverPlayer.getItemBySlot(slot)).filter(WardingModuleItem::hasWardAttached)
                        .mapToInt(stack -> 1 + WardingModuleItem.getAttachedWardLevel(stack)).sum();
                playerWard.setMaxWard(newMax);
                playerWard.sync(serverPlayer);
            });
        }
    }
}
