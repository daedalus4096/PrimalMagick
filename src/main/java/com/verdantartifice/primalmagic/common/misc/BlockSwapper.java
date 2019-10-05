package com.verdantartifice.primalmagic.common.misc;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.common.network.PacketHandler;
import com.verdantartifice.primalmagic.common.network.packets.fx.BlockPoofPacket;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.ForgeEventFactory;

public class BlockSwapper {
    protected static final Map<Integer, Queue<BlockSwapper>> REGISTRY = new HashMap<>();
    
    protected final BlockPos pos;
    protected final BlockState source;
    protected final ItemStack target;
    protected final PlayerEntity player;
    
    public BlockSwapper(@Nonnull BlockPos pos, @Nullable BlockState source, @Nullable ItemStack target, @Nonnull PlayerEntity player) {
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
        BlockState state = world.getBlockState(this.pos);
        
        // Only allow the swap if the source block could normally be removed by the player and there's a difference to be had
        boolean allow = (state.getBlockHardness(world, this.pos) >= 0.0F) && (this.source == null || this.source.equals(state));
        if (allow && world.canMineBlockBody(this.player, this.pos) && this.isTargetDifferent(state) && this.canPlace(world, state)) {
            if (this.target == null || this.target.isEmpty()) {
                // If the swap target is empty, just remove the source block
                world.removeBlock(this.pos, false);
            } else {
                Block targetBlock = Block.getBlockFromItem(this.target.getItem());
                if (targetBlock != null && targetBlock != Blocks.AIR) {
                    // If the target is a block, set its default state in the world
                    world.setBlockState(this.pos, targetBlock.getDefaultState(), 0x3);
                } else {
                    // Otherwise, spawn an item in the block's place
                    world.removeBlock(this.pos, false);
                    ItemEntity entity = new ItemEntity(world, this.pos.getX() + 0.5D, this.pos.getY(), this.pos.getZ() + 0.5D, this.target.copy());
                    entity.setMotion(0.0D, 0.0D, 0.0D);
                    world.addEntity(entity);
                }
                PacketHandler.sendToAllAround(new BlockPoofPacket(this.pos, Color.WHITE.getRGB(), true, Direction.UP), world.getDimension().getType(), this.pos, 32.0D);
            }
        }
    }

    protected boolean isTargetDifferent(BlockState sourceState) {
        return this.target == null || this.target.isEmpty() || !this.target.isItemEqual(new ItemStack(sourceState.getBlock()));
    }
    
    protected boolean canPlace(World world, BlockState state) {
        return !ForgeEventFactory.onBlockPlace(this.player, new BlockSnapshot(world, this.pos, state), Direction.UP);
    }
}
