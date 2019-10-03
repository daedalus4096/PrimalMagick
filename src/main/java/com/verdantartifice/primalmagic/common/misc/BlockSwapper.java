package com.verdantartifice.primalmagic.common.misc;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.PrimalMagic;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockSwapper {
    protected static final Map<Integer, Queue<BlockSwapper>> REGISTRY = new HashMap<>();
    
    protected final BlockPos pos;
    protected final BlockState source;
    protected final ItemStack target;
    protected final PlayerEntity player;
    
    public BlockSwapper(BlockPos pos, BlockState source, ItemStack target, PlayerEntity player) {
        this.pos = pos;
        this.source = source;
        this.target = target;
        this.player = player;
    }
    
    public static boolean enqueue(@Nonnull World world, @Nullable BlockSwapper swapper) {
        if (swapper == null) {
            return false;
        } else {
            return getWorldSwappers(world).offer(swapper);
        }
    }
    
    @Nonnull
    public static Queue<BlockSwapper> getWorldSwappers(@Nonnull World world) {
        int dim = world.getDimension().getType().getId();
        Queue<BlockSwapper> swapperQueue = REGISTRY.get(Integer.valueOf(dim));
        if (swapperQueue == null) {
            swapperQueue = new LinkedBlockingQueue<>();
            REGISTRY.put(Integer.valueOf(dim), swapperQueue);
        }
        return swapperQueue;
    }
    
    public void execute(World world) {
        PrimalMagic.LOGGER.debug("Executing block swapper at position {}", this.pos.toString());
    }
}
