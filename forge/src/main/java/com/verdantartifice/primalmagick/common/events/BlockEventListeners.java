package com.verdantartifice.primalmagick.common.events;

import com.verdantartifice.primalmagick.Constants;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Forge listeners for block related events.
 * 
 * @author Daedalus4096
 */
@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public class BlockEventListeners {
    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        Level level = (event.getLevel() instanceof Level l) ? l : null;
        if (!event.isCanceled()) {
            BlockEvents.onBlockBreak(event.getPlayer(), level, event.getPos(), event.getState());
        }
    }
    
    @SubscribeEvent(priority=EventPriority.LOWEST)
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
