package com.verdantartifice.primalmagic.common.misc;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CommandBlockBlock;
import net.minecraft.block.JigsawBlock;
import net.minecraft.block.StructureBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

/**
 * Definition of a block breaker data structure.  Processed during server ticks to gradually break blocks
 * without continued player interaction.  They decrease a given "durability" value for the block by a 
 * given power value each tick, removing the block when that durability hits zero.  Also tracks all
 * active block breakers in a static registry.
 * 
 * @author Daedalus4096
 */
public class BlockBreaker {
    protected static final Map<Integer, Queue<BlockBreaker>> REGISTRY = new HashMap<>();
    
    protected final float power;
    protected final BlockPos pos;
    protected final BlockState targetBlock;
    protected final float currentDurability;
    protected final float maxDurability;
    protected final PlayerEntity player;
    
    public BlockBreaker(float power, @Nonnull BlockPos pos, @Nonnull BlockState targetBlock, float currentDurability, float maxDurability, @Nonnull PlayerEntity player) {
        this.power = power;
        this.pos = pos;
        this.targetBlock = targetBlock;
        this.currentDurability = currentDurability;
        this.maxDurability = maxDurability;
        this.player = player;
    }
    
    public static boolean enqueue(@Nonnull World world, @Nullable BlockBreaker breaker) {
        if (breaker == null) {
            // Don't allow null breakers in the queue
            return false;
        } else {
            return getWorldBreakers(world).offer(breaker);
        }
    }
    
    @Nonnull
    public static Queue<BlockBreaker> getWorldBreakers(@Nonnull World world) {
        int dim = world.getDimension().getType().getId();
        Queue<BlockBreaker> breakerQueue = REGISTRY.get(Integer.valueOf(dim));
        if (breakerQueue == null) {
            // If no breaker queue is defined for the world, create one
            breakerQueue = new LinkedBlockingQueue<>();
            REGISTRY.put(Integer.valueOf(dim), breakerQueue);
        }
        return breakerQueue;
    }
    
    public static void setWorldBreakerQueue(@Nonnull World world, @Nonnull Queue<BlockBreaker> breakerQueue) {
        // Replace the world's breaker queue with the given one
        int dim = world.getDimension().getType().getId();
        REGISTRY.put(Integer.valueOf(dim), breakerQueue);
    }
    
    @Nullable
    public BlockBreaker execute(@Nonnull World world) {
        BlockBreaker retVal = null;
        BlockState state = world.getBlockState(this.pos);
        if (state == this.targetBlock) {
            // Only allow block breakers to act on blocks that could normally be broken by a player
            if (world.canMineBlockBody(this.player, this.pos) && state.getBlockHardness(world, this.pos) >= 0.0F) {
                // Send packets showing the visual effects of the block breaker's progress
                world.sendBlockBreakProgress(this.pos.hashCode(), this.pos, (int)((1.0F - this.currentDurability / this.maxDurability) * 10.0F));
                
                // Calculate new block durability and check to see if the block breaking is done
                float newDurability = this.currentDurability - this.power;
                if (newDurability <= 0.0F) {
                    // Do block break
                    this.doHarvest(world);
                    world.sendBlockBreakProgress(this.pos.hashCode(), this.pos, -1);
                } else {
                    // Queue up another round of breaking progress
                    retVal = new BlockBreaker(this.power, this.pos, this.targetBlock, newDurability, this.maxDurability, this.player);
                }
            }
        }
        return retVal;
    }
    
    /**
     * Attempt to harvest this breaker's block in the given world
     * 
     * @param world the world to break in
     * @return true if the block was successfully harvested, false otherwise
     * @see {@link net.minecraft.server.management.PlayerInteractionManager#tryHarvestBlock(BlockPos)}
     */
    protected boolean doHarvest(@Nonnull World world) {
        if (world.isRemote || !(this.player instanceof ServerPlayerEntity)) {
            return false;
        }
        ServerPlayerEntity serverPlayer = (ServerPlayerEntity)this.player;
        int exp = ForgeHooks.onBlockBreakEvent(world, serverPlayer.interactionManager.getGameType(), serverPlayer, this.pos);
        if (exp == -1) {
            return false;
        } else {
            TileEntity tile = world.getTileEntity(this.pos);
            BlockState state = world.getBlockState(this.pos);
            Block block = state.getBlock();
            if ((block instanceof CommandBlockBlock || block instanceof StructureBlock || block instanceof JigsawBlock) && !serverPlayer.canUseCommandBlock()) {
                world.notifyBlockUpdate(this.pos, state, state, 0x3);
                return false;
            } else if (serverPlayer.getHeldItemMainhand().onBlockStartBreak(this.pos, serverPlayer)) {
                return false;
            } else if (serverPlayer.func_223729_a(world, this.pos, serverPlayer.interactionManager.getGameType())) {
                return false;
            } else {
                if (serverPlayer.interactionManager.isCreative()) {
                    this.removeBlock(world, false);
                    return true;
                } else {
                    // TODO handle fortune and silk touch
                    ItemStack stack = serverPlayer.getHeldItemMainhand();
                    boolean canHarvest = true;
                    boolean success = this.removeBlock(world, true);
                    if (success && canHarvest) {
                        block.harvestBlock(world, serverPlayer, this.pos, state, tile, stack.copy());
                    }
                    if (success && exp > 0) {
                        block.dropXpOnBlockBreak(world, this.pos, exp);
                    }
                    return true;
                }
            }
        }
    }
    
    /**
     * Actually remove this breaker's block from the give world and, if specified, do its drops.
     * 
     * @param world the world to break in
     * @param canHarvest whether the player is able to harvest this block for drops
     * @return true if the block is successfully removed, false otherwise
     * @see {@link net.minecraft.server.management.PlayerInteractionManager#removeBlock}
     */
    protected boolean removeBlock(@Nonnull World world, boolean canHarvest) {
        BlockState state = world.getBlockState(this.pos);
        boolean removed = state.removedByPlayer(world, this.pos, this.player, canHarvest, world.getFluidState(this.pos));
        if (removed) {
            state.getBlock().onPlayerDestroy(world, this.pos, state);
        }
        return removed;
    }
}
