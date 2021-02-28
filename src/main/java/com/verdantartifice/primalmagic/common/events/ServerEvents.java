package com.verdantartifice.primalmagic.common.events;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.misc.BlockBreaker;
import com.verdantartifice.primalmagic.common.misc.BlockSwapper;
import com.verdantartifice.primalmagic.common.misc.EntitySwapper;

import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

/**
 * Handlers for server related events.
 * 
 * @author Daedalus4096
 */
@Mod.EventBusSubscriber(modid=PrimalMagic.MODID)
public class ServerEvents {
    @SubscribeEvent
    public static void serverWorldTick(TickEvent.WorldTickEvent event) {
        if (event.side == LogicalSide.CLIENT) {
            return;
        }
        if (event.phase != TickEvent.Phase.START) {
            // Process any pending world modifiers
            tickBlockSwappers(event.world);
            tickBlockBreakers(event.world);
            tickEntitySwappers(event.world);
        }
    }
    
    protected static void tickBlockSwappers(World world) {
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
    
    protected static void tickBlockBreakers(World world) {
        Queue<BlockBreaker> breakerQueue = BlockBreaker.getWorldBreakers(world);
        if (breakerQueue != null) {
            // Execute each pending block breaker in turn
            Queue<BlockBreaker> newQueue = new LinkedBlockingQueue<>();
            while (!breakerQueue.isEmpty()) {
                BlockBreaker breaker = breakerQueue.poll();
                if (breaker != null) {
                    BlockBreaker newBreaker = breaker.execute(world);
                    if (newBreaker != null) {
                        // If the block breaking isn't done, re-queue the new breaker
                        newQueue.offer(newBreaker);
                    }
                }
            }
            BlockBreaker.setWorldBreakerQueue(world, newQueue);
        }
    }
    
    protected static void tickEntitySwappers(World world) {
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
