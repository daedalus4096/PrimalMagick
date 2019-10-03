package com.verdantartifice.primalmagic.common.events;

import java.util.Queue;

import com.verdantartifice.primalmagic.common.misc.BlockSwapper;

import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ServerEvents {
    @SubscribeEvent
    public static void serverWorldTick(TickEvent.WorldTickEvent event) {
        if (event.side == LogicalSide.CLIENT) {
            return;
        }
        if (event.phase != TickEvent.Phase.START) {
            tickBlockSwappers(event.world);
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
}
