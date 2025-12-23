package com.verdantartifice.primalmagick.common.events;

import com.verdantartifice.primalmagick.Constants;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraftforge.event.entity.living.AnimalTameEvent;
import net.minecraftforge.event.entity.living.BabyEntitySpawnEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.eventbus.api.listener.Priority;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Forge listeners for miscellaneous entity events.
 * 
 * @author Daedalus4096
 */
@Mod.EventBusSubscriber(modid= Constants.MOD_ID)
public class EntityEventListeners {
    @SubscribeEvent
    public static boolean onEnderEntityTeleport(EntityTeleportEvent.EnderEntity event) {
        return EntityEvents.onEnderTeleport(event.getEntityLiving(), event.getTarget());
    }
    
    @SubscribeEvent
    public static boolean onEnderPearlTeleport(EntityTeleportEvent.EnderPearl event) {
        return EntityEvents.onEnderTeleport(event.getPlayer(), event.getTarget());
    }
    
    @SubscribeEvent
    public static boolean onChorusFruitTeleport(EntityTeleportEvent.ChorusFruit event) {
        return EntityEvents.onEnderTeleport(event.getEntityLiving(), event.getTarget());
    }
    
    @SubscribeEvent(priority=Priority.LOWEST)
    public static void onEnderPearlTeleportLowest(EntityTeleportEvent.EnderPearl event) {
        EntityEvents.onEnderTeleportLowest(event.getPlayer(), event.getTarget());
    }
    
    @SubscribeEvent(priority=Priority.LOWEST)
    public static void onChorusFruitTeleportLowest(EntityTeleportEvent.ChorusFruit event) {
        if (event.getEntityLiving() instanceof Player player) {
            EntityEvents.onEnderTeleportLowest(player, event.getTarget());
        }
    }
    
    @SubscribeEvent(priority=Priority.LOWEST)
    public static void onAnimalTameLowest(AnimalTameEvent event) {
        EntityEvents.onAnimalTameLowest(event.getTamer(), event.getAnimal());
    }
    
    @SubscribeEvent(priority=Priority.LOWEST)
    public static void onBabyEntitySpawnLowest(BabyEntitySpawnEvent event) {
        EntityEvents.onBabyEntitySpawnLowest(event.getCausedByPlayer());
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
