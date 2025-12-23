package com.verdantartifice.primalmagick.common.events;

import com.verdantartifice.primalmagick.Constants;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.listener.Priority;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
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
        BlockEvents.onBlockBreak(event.getPlayer(), level, event.getPos(), event.getState());
    }
    
    @SubscribeEvent(priority=Priority.LOWEST)
    public static void onBlockBreakLowest(BlockEvent.BreakEvent event) {
        BlockEvents.onBlockBreakLowest(event.getPlayer(), event.getLevel(), event.getPos(), event.getState());
    }
    
    @SubscribeEvent
    public static boolean onBlockRightClick(PlayerInteractEvent.RightClickBlock event) {
        InteractionResult result = BlockEvents.onBlockRightClick(event.getEntity(), event.getLevel(), event.getHitVec(), event.getHand());
        if (result.consumesAction()) {
            event.setCancellationResult(result);
            return true;
        } else {
            return false;
        }
    }
}
