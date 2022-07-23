package com.verdantartifice.primalmagick.common.events;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.misc.BlockBreaker;
import com.verdantartifice.primalmagick.common.misc.BlockSwapper;
import com.verdantartifice.primalmagick.common.misc.EntitySwapper;

import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

/**
 * Handlers for server related events.
 * 
 * @author Daedalus4096
 */
@Mod.EventBusSubscriber(modid=PrimalMagick.MODID)
public class ServerEvents {
    @SubscribeEvent
    public static void serverWorldTick(TickEvent.LevelTickEvent event) {
        if (event.side == LogicalSide.CLIENT) {
            return;
        }
        if (event.phase != TickEvent.Phase.START) {
            // Process any pending world modifiers
            tickBlockSwappers(event.level);
            tickBlockBreakers(event.level);
            tickEntitySwappers(event.level);
        }
    }
    
    protected static void tickBlockSwappers(Level world) {
        Queue<BlockSwapper> swapperQueue = BlockSwapper.getWorldSwappers(world);
        if (swapperQueue != null) {
            // Execute each pending block swapper in turn
            while (!swapperQueue.isEmpty()) {
                BlockSwapper swapper = swapperQueue.poll();
                if (swapper != null) {
                    swapper.execute(world);
                }
            }
        }
    }
    
    protected static void tickBlockBreakers(Level world) {
        Iterable<BlockBreaker> breakers = BlockBreaker.tick(world);
        for (BlockBreaker breaker : breakers) {
            // Execute each pending block breaker in turn
            BlockBreaker newBreaker = breaker.execute(world);
            if (newBreaker != null) {
                BlockBreaker.schedule(world, 1, newBreaker);
            }
        }
    }
    
    protected static void tickEntitySwappers(Level world) {
        Queue<EntitySwapper> swapperQueue = EntitySwapper.getWorldSwappers(world);
        if (swapperQueue != null) {
            // Execute each pending entity swapper in turn
            Queue<EntitySwapper> newQueue = new LinkedBlockingQueue<>();
            while (!swapperQueue.isEmpty()) {
                EntitySwapper swapper = swapperQueue.poll();
                if (swapper != null) {
                    if (swapper.isReady()) {
                        EntitySwapper newSwapper = swapper.execute(world);
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
            EntitySwapper.setWorldSwapperQueue(world, newQueue);
        }
    }
}
