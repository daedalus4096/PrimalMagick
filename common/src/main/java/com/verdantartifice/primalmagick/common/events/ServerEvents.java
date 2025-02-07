package com.verdantartifice.primalmagick.common.events;

import com.verdantartifice.primalmagick.common.misc.BlockBreaker;
import com.verdantartifice.primalmagick.common.misc.BlockSwapper;
import net.minecraft.world.level.Level;

import java.util.Queue;

/**
 * Handlers for server related events.
 * 
 * @author Daedalus4096
 */
public class ServerEvents {
    public static void serverLevelTick(Level level) {
        // Process any pending world modifiers
        tickBlockSwappers(level);
        tickBlockBreakers(level);
    }
    
    protected static void tickBlockSwappers(Level level) {
        // Execute each pending block swapper in turn
        Queue<BlockSwapper> swapperQueue = BlockSwapper.getWorldSwappers(level);
        while (!swapperQueue.isEmpty()) {
            BlockSwapper swapper = swapperQueue.poll();
            if (swapper != null) {
                swapper.execute(level);
            }
        }
    }
    
    protected static void tickBlockBreakers(Level level) {
        Iterable<BlockBreaker> breakers = BlockBreaker.tick(level);
        for (BlockBreaker breaker : breakers) {
            // Execute each pending block breaker in turn
            BlockBreaker newBreaker = breaker.execute(level);
            if (newBreaker != null) {
                BlockBreaker.schedule(level, 1, newBreaker);
            }
        }
    }
}
