package com.verdantartifice.primalmagick.common.events;

import com.verdantartifice.primalmagick.Constants;
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

/**
 * Handlers for player related events.
 * 
 * @author Daedalus4096
 */
@Mod.EventBusSubscriber(modid= Constants.MOD_ID)
public class PlayerEventListeners {
    @SubscribeEvent
    public static void livingTick(LivingEvent.LivingTickEvent event) {
        PlayerEvents.livingTick(event.getEntity());
    }
    
    @SubscribeEvent
    public static void playerJoinEvent(EntityJoinLevelEvent event) {
        PlayerEvents.playerJoinEvent(event.getEntity(), event.getLevel());
    }
    
    @SubscribeEvent
    public static void playerCloneEvent(PlayerEvent.Clone event) {
        // Preserve player capability data between deaths or returns from the End
        event.getOriginal().reviveCaps();   // FIXME Workaround for a Forge issue

        PlayerEvents.playerCloneEvent(event.getOriginal(), event.getEntity(), event.isWasDeath());
        
        event.getOriginal().invalidateCaps();   // FIXME Remove when the reviveCaps call is removed
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
        if (!event.isSimulated() && event.getToolAction().equals(ToolActions.HOE_TILL)) {
            PlayerEvents.onUseHoe(event.getPlayer(), event.getContext(), event::setFinalState);
        }
    }
    
    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        PlayerEvents.onEntityInteract(event.getEntity(), event.getHand(), event.getTarget());
    }
}
