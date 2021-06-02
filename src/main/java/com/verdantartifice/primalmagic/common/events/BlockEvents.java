package com.verdantartifice.primalmagic.common.events;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.stats.StatsManager;
import com.verdantartifice.primalmagic.common.stats.StatsPM;

import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Handlers for block related events.
 * 
 * @author Daedalus4096
 */
@Mod.EventBusSubscriber(modid=PrimalMagic.MODID)
public class BlockEvents {
    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        // Record the block break statistic
        if (!event.isCanceled() && event.getState().getBlockHardness(event.getWorld(), event.getPos()) >= 2.0F && event.getPlayer().getHeldItemMainhand().isEmpty() && 
                event.getPlayer().getHeldItemOffhand().isEmpty()) {
            StatsManager.incrementValue(event.getPlayer(), StatsPM.BLOCKS_BROKEN_BAREHANDED);
        }
    }
}
