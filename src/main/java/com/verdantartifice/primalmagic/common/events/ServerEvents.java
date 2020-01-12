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

@Mod.EventBusSubscriber(modid=PrimalMagic.MODID)
public class ServerEvents {
    @SubscribeEvent
    public static void serverWorldTick(TickEvent.WorldTickEvent event) {
        if (event.side == LogicalSide.CLIENT) {
            return;
        }
        if (event.phase != TickEvent.Phase.START) {
            tickBlockSwappers(event.world);
            tickBlockBreakers(event.world);
            tickEntitySwappers(event.world);
        }
    }
    
    protected static void tickBlockSwappers(World world) {
        Queue<BlockSwapper> swapperQueue = BlockSwapper.getWorldSwappers(world);
        if (swapperQueue != null) {
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
            Queue<BlockBreaker> newQueue = new LinkedBlockingQueue<>();
            while (!breakerQueue.isEmpty()) {
                BlockBreaker breaker = breakerQueue.poll();
                if (breaker != null) {
                    BlockBreaker newBreaker = breaker.execute(world);
                    if (newBreaker != null) {
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
            Queue<EntitySwapper> newQueue = new LinkedBlockingQueue<>();
            while (!swapperQueue.isEmpty()) {
                EntitySwapper swapper = swapperQueue.poll();
                if (swapper != null) {
                    if (swapper.isReady()) {
                        EntitySwapper newSwapper = swapper.execute(world);
                        if (newSwapper!= null) {
                            newQueue.offer(newSwapper);
                        }
                    } else {
                        swapper.decrementDelay();
                        newQueue.offer(swapper);
                    }
                }
            }
            EntitySwapper.setWorldSwapperQueue(world, newQueue);
        }
    }
}
