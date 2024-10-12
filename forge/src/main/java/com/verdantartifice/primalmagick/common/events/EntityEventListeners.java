package com.verdantartifice.primalmagick.common.events;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.blocks.misc.EnderwardBlock;
import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.effects.EffectsPM;
import com.verdantartifice.primalmagick.common.enchantments.EnchantmentHelperPM;
import com.verdantartifice.primalmagick.common.enchantments.EnchantmentsPM;
import com.verdantartifice.primalmagick.common.items.armor.WardingModuleItem;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.ResearchManager;
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
 * Forge listeners for miscellaneous entity events.
 * 
 * @author Daedalus4096
 */
@Mod.EventBusSubscriber(modid= Constants.MOD_ID)
public class EntityEventListeners {
    @SubscribeEvent
    public static void onEnderEntityTeleport(EntityTeleportEvent.EnderEntity event) {
        if (!event.isCanceled() && EntityEvents.onEnderTeleport(event.getEntityLiving(), event.getTarget())) {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent
    public static void onEnderPearlTeleport(EntityTeleportEvent.EnderPearl event) {
        if (!event.isCanceled() && EntityEvents.onEnderTeleport(event.getPlayer(), event.getTarget())) {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent
    public static void onChorusFruitTeleport(EntityTeleportEvent.ChorusFruit event) {
        if (!event.isCanceled() && EntityEvents.onEnderTeleport(event.getEntityLiving(), event.getTarget())) {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent(priority=EventPriority.LOWEST)
    public static void onEnderPearlTeleportLowest(EntityTeleportEvent.EnderPearl event) {
        EntityEvents.onEnderTeleportLowest(event.getPlayer(), event.getTarget());
    }
    
    @SubscribeEvent(priority=EventPriority.LOWEST)
    public static void onChorusFruitTeleportLowest(EntityTeleportEvent.ChorusFruit event) {
        if (!event.isCanceled() && event.getEntityLiving() instanceof Player player) {
            EntityEvents.onEnderTeleportLowest(player, event.getTarget());
        }
    }
    
    @SubscribeEvent(priority=EventPriority.LOWEST)
    public static void onAnimalTameLowest(AnimalTameEvent event) {
        if (!event.isCanceled()) {
            EntityEvents.onAnimalTameLowest(event.getTamer(), event.getAnimal());
        }
    }
    
    @SubscribeEvent(priority=EventPriority.LOWEST)
    public static void onBabyEntitySpawnLowest(BabyEntitySpawnEvent event) {
        if (!event.isCanceled()) {
            EntityEvents.onBabyEntitySpawnLowest(event.getCausedByPlayer());
        }
    }
    
    @SubscribeEvent
    public static void onLivingEntityUseItemTick(LivingEntityUseItemEvent.Tick event) {
        // Stack up resistance on the wielders of shields with the Bulwark enchantment
        LivingEntity entity = event.getEntity();
        ItemStack stack = event.getItem();
        int currentDuration = event.getDuration();
        int maxDuration = stack.getUseDuration(entity);
        int delta = maxDuration - currentDuration;
        int enchantLevel = EnchantmentHelperPM.getEnchantmentLevel(stack, EnchantmentsPM.BULWARK, entity.registryAccess());
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
                int treasureLevel = EnchantmentHelperPM.getEquippedEnchantmentLevel(living, EnchantmentsPM.TREASURE);
                event.setLootingLevel(Math.max(event.getLootingLevel(), treasureLevel));
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
