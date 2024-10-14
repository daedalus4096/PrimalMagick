package com.verdantartifice.primalmagick.common.events;

import com.verdantartifice.primalmagick.common.misc.BlockBreaker;
import com.verdantartifice.primalmagick.common.misc.BlockSwapper;
import com.verdantartifice.primalmagick.common.misc.EntitySwapper;
import net.minecraft.world.level.Level;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

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
        tickEntitySwappers(level);
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
    
    protected static void tickEntitySwappers(Level level) {
        Queue<EntitySwapper> swapperQueue = EntitySwapper.getWorldSwappers(level);
        if (swapperQueue != null) {
            // Execute each pending entity swapper in turn
            Queue<EntitySwapper> newQueue = new LinkedBlockingQueue<>();
            while (!swapperQueue.isEmpty()) {
                EntitySwapper swapper = swapperQueue.poll();
                if (swapper != null) {
                    if (swapper.isReady()) {
                        EntitySwapper newSwapper = swapper.execute(level);
                        if (newSwapper != null) {
                            // If a return swap is triggered by this swap, queue up the new swapper
                            newQueue.offer(newSwapper);
                        }
                    } else {
                        // If the swapper isn't ready yet, re-queue it with a shorter delay
                        swapper.decrementDelay();
                        newQueue.offer(swapper);
                    }
                }
            }
            EntitySwapper.setWorldSwapperQueue(level, newQueue);
        }
    }
}
