package com.verdantartifice.primalmagick.common.events;

import com.verdantartifice.primalmagick.Constants;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

/**
 * Neoforge listeners for block related events.
 * 
 * @author Daedalus4096
 */
@EventBusSubscriber(modid = Constants.MOD_ID)
public class BlockEventListeners {
    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        Level level = (event.getLevel() instanceof Level l) ? l : null;
        if (!event.isCanceled()) {
            BlockEvents.onBlockBreak(event.getPlayer(), level, event.getPos(), event.getState());
        }
    }
    
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onBlockBreakLowest(BlockEvent.BreakEvent event) {
        if (!event.isCanceled()) {
            BlockEvents.onBlockBreakLowest(event.getPlayer(), event.getLevel(), event.getPos(), event.getState());
        }
    }
    
    @SubscribeEvent
    public static void onBlockRightClick(PlayerInteractEvent.RightClickBlock event) {
        InteractionResult result = BlockEvents.onBlockRightClick(event.getEntity(), event.getLevel(), event.getHitVec(), event.getHand());
        if (result.consumesAction()) {
            event.setCanceled(true);
            event.setCancellationResult(result);
        }
    }
}
