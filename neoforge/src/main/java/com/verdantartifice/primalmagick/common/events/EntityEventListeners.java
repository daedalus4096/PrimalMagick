package com.verdantartifice.primalmagick.common.events;

import com.verdantartifice.primalmagick.Constants;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityTeleportEvent;
import net.neoforged.neoforge.event.entity.living.AnimalTameEvent;
import net.neoforged.neoforge.event.entity.living.BabyEntitySpawnEvent;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;

/**
 * Neoforge listeners for miscellaneous entity events.
 * 
 * @author Daedalus4096
 */
@EventBusSubscriber(modid = Constants.MOD_ID)
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
    
    @SubscribeEvent(priority= EventPriority.LOWEST)
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
        EntityEvents.onLivingEntityUseItemTick(event.getEntity(), event.getItem(), event.getDuration());
    }
    
    @SubscribeEvent
    public static void onLivingEquipmentChange(LivingEquipmentChangeEvent event) {
        EntityEvents.onLivingEquipmentChange(event.getEntity(), event.getFrom(), event.getTo());
    }
}
