package com.verdantartifice.primalmagick.common.misc;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.fx.WandPoofPacket;
import com.verdantartifice.primalmagick.common.tiles.base.IOwnedTileEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.util.BlockSnapshot;
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
    protected final Player player;
    
    public BlockSwapper(@Nonnull BlockPos pos, @Nullable BlockState source, @Nullable ItemStack target, @Nonnull Player player) {
        this.pos = pos;
        this.source = source;
        this.target = target;
        this.player = player;
    }
    
    public static boolean enqueue(@Nonnull Level world, @Nullable BlockSwapper swapper) {
        if (swapper == null) {
            // Don't allow null swappers in the queue
            return false;
        } else {
            return getWorldSwappers(world).offer(swapper);
        }
    }
    
    @Nonnull
    public static Queue<BlockSwapper> getWorldSwappers(@Nonnull Level world) {
        return REGISTRY.computeIfAbsent(world.dimension().location(), (key) -> {
            return new LinkedBlockingQueue<>();
        });
    }
    
    public void execute(Level world) {
        BlockState state = world.getBlockState(this.pos);
        
        // Only allow the swap if the source block could normally be removed by the player and there's a difference to be had
        boolean allow = (state.getDestroySpeed(world, this.pos) >= 0.0F) && (this.source == null || this.source.equals(state));
        if (allow && world.mayInteract(this.player, this.pos) && this.isTargetDifferent(state) && this.canPlace(world, state)) {
            if (this.target == null || this.target.isEmpty()) {
                // If the swap target is empty, just remove the source block
                world.removeBlock(this.pos, false);
            } else {
                Block targetBlock = Block.byItem(this.target.getItem());
                if (targetBlock != null && targetBlock != Blocks.AIR) {
                    // If the target is a block, set its state, with facing if applicable, in the world
                    BlockState targetState = targetBlock.defaultBlockState();
                    if (state.hasProperty(BlockStateProperties.FACING)) {
                        targetState = targetState.setValue(BlockStateProperties.FACING, state.getValue(BlockStateProperties.FACING));
                    } else if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                        targetState = targetState.setValue(BlockStateProperties.HORIZONTAL_FACING, state.getValue(BlockStateProperties.HORIZONTAL_FACING));
                    }
                    world.setBlock(this.pos, targetState, Block.UPDATE_ALL);
                    
                    // Set the owner of the new block's tile entity, if applicable
                    BlockEntity tile = world.getBlockEntity(this.pos);
                    if (tile instanceof IOwnedTileEntity) {
                        ((IOwnedTileEntity)tile).setTileOwner(this.player);
                    }
                } else {
                    // Otherwise, spawn an item in the block's place
                    world.removeBlock(this.pos, false);
                    ItemEntity entity = new ItemEntity(world, this.pos.getX() + 0.5D, this.pos.getY(), this.pos.getZ() + 0.5D, this.target.copy());
                    entity.setDeltaMovement(0.0D, 0.0D, 0.0D);
                    world.addFreshEntity(entity);
                }
                PacketHandler.sendToAllAround(new WandPoofPacket(this.pos, Color.WHITE.getRGB(), true, Direction.UP), world.dimension(), this.pos, 32.0D);
            }
        }
    }

    protected boolean isTargetDifferent(BlockState sourceState) {
        return this.target == null || this.target.isEmpty() || !ItemStack.isSameItem(this.target, new ItemStack(sourceState.getBlock()));
    }
    
    protected boolean canPlace(Level world, BlockState state) {
        return !ForgeEventFactory.onBlockPlace(this.player, BlockSnapshot.create(world.dimension(), world, this.pos), Direction.UP);
    }
}
