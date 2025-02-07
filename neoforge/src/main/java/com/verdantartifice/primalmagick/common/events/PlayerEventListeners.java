package com.verdantartifice.primalmagick.common.events;

import com.verdantartifice.primalmagick.Constants;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.entity.player.PlayerWakeUpEvent;
import net.neoforged.neoforge.event.entity.player.PlayerXpEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

/**
 * Neoforge listeners for player related events.
 * 
 * @author Daedalus4096
 */
@EventBusSubscriber(modid = Constants.MOD_ID)
public class PlayerEventListeners {
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
        if (!event.isSimulated() && event.getItemAbility().equals(ItemAbilities.HOE_TILL)) {
            PlayerEvents.onUseHoe(event.getPlayer(), event.getContext(), event::setFinalState);
        }
    }
    
    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        PlayerEvents.onEntityInteract(event.getEntity(), event.getHand(), event.getTarget());
    }
}
