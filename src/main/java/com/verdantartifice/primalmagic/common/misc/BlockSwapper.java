package com.verdantartifice.primalmagic.common.misc;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.common.network.PacketHandler;
import com.verdantartifice.primalmagic.common.network.packets.fx.WandPoofPacket;
import com.verdantartifice.primalmagic.common.tiles.base.IOwnedTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.ForgeEventFactory;

/**
 * Definition of a block swapper data structure.  Processed during server ticks to replace a given source
 * block with a target block/item.  If the target is a block, it is placed into the world; otherwise, an
 * item entity is spawned.  Also tracks all active block swappers in a static registry.
 * 
 * @author Daedalus4096
 */
public class BlockSwapper {
    protected static final Map<ResourceLocation, Queue<BlockSwapper>> REGISTRY = new HashMap<>();
    
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
            // Don't allow null swappers in the queue
            return false;
        } else {
            return getWorldSwappers(world).offer(swapper);
        }
    }
    
    @Nonnull
    public static Queue<BlockSwapper> getWorldSwappers(@Nonnull World world) {
    	return REGISTRY.computeIfAbsent(world.getDimensionKey().getLocation(), (key) -> {
    		return new LinkedBlockingQueue<>();
    	});
    }
    
    public void execute(World world) {
        BlockState state = world.getBlockState(this.pos);
        
        // Only allow the swap if the source block could normally be removed by the player and there's a difference to be had
        boolean allow = (state.getBlockHardness(world, this.pos) >= 0.0F) && (this.source == null || this.source.equals(state));
        if (allow && world.isBlockModifiable(this.player, this.pos) && this.isTargetDifferent(state) && this.canPlace(world, state)) {
            if (this.target == null || this.target.isEmpty()) {
                // If the swap target is empty, just remove the source block
                world.removeBlock(this.pos, false);
            } else {
                Block targetBlock = Block.getBlockFromItem(this.target.getItem());
                if (targetBlock != null && targetBlock != Blocks.AIR) {
                    // If the target is a block, set its state, with facing if applicable, in the world
                    BlockState targetState = targetBlock.getDefaultState();
                    if (state.hasProperty(BlockStateProperties.FACING)) {
                        targetState = targetState.with(BlockStateProperties.FACING, state.get(BlockStateProperties.FACING));
                    } else if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                        targetState = targetState.with(BlockStateProperties.HORIZONTAL_FACING, state.get(BlockStateProperties.HORIZONTAL_FACING));
                    }
                    world.setBlockState(this.pos, targetState, Constants.BlockFlags.DEFAULT);
                    
                    // Set the owner of the new block's tile entity, if applicable
                    TileEntity tile = world.getTileEntity(this.pos);
                    if (tile instanceof IOwnedTileEntity) {
                        ((IOwnedTileEntity)tile).setTileOwner(this.player);
                    }
                } else {
                    // Otherwise, spawn an item in the block's place
                    world.removeBlock(this.pos, false);
                    ItemEntity entity = new ItemEntity(world, this.pos.getX() + 0.5D, this.pos.getY(), this.pos.getZ() + 0.5D, this.target.copy());
                    entity.setMotion(0.0D, 0.0D, 0.0D);
                    world.addEntity(entity);
                }
                PacketHandler.sendToAllAround(new WandPoofPacket(this.pos, Color.WHITE.getRGB(), true, Direction.UP), world.getDimensionKey(), this.pos, 32.0D);
            }
        }
    }

    protected boolean isTargetDifferent(BlockState sourceState) {
        return this.target == null || this.target.isEmpty() || !this.target.isItemEqual(new ItemStack(sourceState.getBlock()));
    }
    
    protected boolean canPlace(World world, BlockState state) {
        return !ForgeEventFactory.onBlockPlace(this.player, BlockSnapshot.create(world.getDimensionKey(), world, this.pos), Direction.UP);
    }
}
