package com.verdantartifice.primalmagic.common.misc;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.Constants;

/**
 * Definition of a block breaker data structure.  Processed during server ticks to gradually break blocks
 * without continued player interaction.  They decrease a given "durability" value for the block by a 
 * given power value each tick, removing the block when that durability hits zero.  Also tracks all
 * active block breakers in a static registry.
 * 
 * @author Daedalus4096
 */
public class BlockBreaker {
    protected static final Map<ResourceLocation, ConcurrentNavigableMap<Integer, Map<BlockPos, BlockBreaker>>> SCHEDULE = new ConcurrentHashMap<>();
    
    protected final float power;
    protected final BlockPos pos;
    protected final BlockState targetBlock;
    protected final float currentDurability;
    protected final float maxDurability;
    protected final PlayerEntity player;
    protected final boolean oneShot;
    protected boolean skipEvent;
    
    public BlockBreaker(float power, @Nonnull BlockPos pos, @Nonnull BlockState targetBlock, float currentDurability, float maxDurability, @Nonnull PlayerEntity player, boolean oneShot, boolean skipEvent) {
        this.power = power;
        this.pos = pos;
        this.targetBlock = targetBlock;
        this.currentDurability = currentDurability;
        this.maxDurability = maxDurability;
        this.player = player;
        this.oneShot = oneShot;
        this.skipEvent = skipEvent;
    }
    
    public BlockBreaker(float power, @Nonnull BlockPos pos, @Nonnull BlockState targetBlock, float currentDurability, float maxDurability, @Nonnull PlayerEntity player) {
        this(power, pos, targetBlock, currentDurability, maxDurability, player, false, false);
    }
    
    /**
     * Schedules the given block breaker to be run on the given world after a given delay.
     * 
     * @param world the world in which to run the block breaker
     * @param delayTicks the delay, in ticks, to wait before running the breaker, minimum zero
     * @param breaker the block breaker to be run
     * @return true if the breaker was successfully scheduled, false otherwise
     */
    public static boolean schedule(@Nonnull World world, int delayTicks, @Nullable BlockBreaker breaker) {
        if (breaker == null) {
            // Don't allow null breakers in the schedule
            return false;
        } else {
            int delay = Math.max(0, delayTicks);
            SCHEDULE.computeIfAbsent(world.getDimensionKey().getLocation(), key -> {
                return new ConcurrentSkipListMap<>();
            }).computeIfAbsent(delay, key -> {
                return new ConcurrentHashMap<>();
            }).put(breaker.pos, breaker);
            return true;
        }
    }
    
    /**
     * Advances the block breaker schedule by a tick, returning the breakers to be run now
     * 
     * @param world the world for which to run
     * @return the collection of block breakers that should be executed now
     */
    public static Iterable<BlockBreaker> tick(@Nonnull World world) {
        ConcurrentNavigableMap<Integer, Map<BlockPos, BlockBreaker>> tree = SCHEDULE.get(world.getDimensionKey().getLocation());
        if (tree == null) {
            return Collections.emptyList();
        } else {
            Iterable<BlockBreaker> retVal = Collections.emptyList();
            ConcurrentNavigableMap<Integer, Map<BlockPos, BlockBreaker>> newTree = new ConcurrentSkipListMap<>();
            while (!tree.isEmpty()) {
                Map.Entry<Integer, Map<BlockPos, BlockBreaker>> entry = tree.pollFirstEntry();
                if (entry.getKey() <= 0) {
                    retVal = entry.getValue().values();
                } else {
                    newTree.put(entry.getKey() - 1, entry.getValue());
                }
            }
            if (!newTree.isEmpty()) {
                SCHEDULE.put(world.getDimensionKey().getLocation(), newTree);
            }
            return retVal;
        }
    }
    
    public static boolean hasBreakerQueued(@Nonnull World world, @Nonnull BlockPos pos) {
        ConcurrentNavigableMap<Integer, Map<BlockPos, BlockBreaker>> tree = SCHEDULE.get(world.getDimensionKey().getLocation());
        if (tree != null) {
            for (Map<BlockPos, BlockBreaker> tickMap : tree.values()) {
                if (tickMap.keySet().contains(pos)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    @Nullable
    public BlockBreaker execute(@Nonnull World world) {
        BlockBreaker retVal = null;
        BlockState state = world.getBlockState(this.pos);
        if (state == this.targetBlock) {
            // Only allow block breakers to act on blocks that could normally be broken by a player
            if (world.isBlockModifiable(this.player, this.pos) && state.getBlockHardness(world, this.pos) >= 0.0F) {
                // Send packets showing the visual effects of the block breaker's progress
                world.sendBlockBreakProgress(this.pos.hashCode(), this.pos, (int)((1.0F - this.currentDurability / this.maxDurability) * 10.0F));
                
                // Calculate new block durability and check to see if the block breaking is done
                float newDurability = this.currentDurability - this.power;
                if (newDurability <= 0.0F) {
                    // Do block break
                    this.doHarvest(world);
                    world.sendBlockBreakProgress(this.pos.hashCode(), this.pos, -1);
                } else if (!this.oneShot) {
                    // Queue up another round of breaking progress
                    retVal = new BlockBreaker(this.power, this.pos, this.targetBlock, newDurability, this.maxDurability, this.player, this.oneShot, this.skipEvent);
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
        ServerWorld serverWorld = (ServerWorld)world;
        int exp = this.skipEvent ? 0 : ForgeHooks.onBlockBreakEvent(world, serverPlayer.interactionManager.getGameType(), serverPlayer, this.pos);
        if (exp == -1) {
            return false;
        } else {
            TileEntity tile = world.getTileEntity(this.pos);
            BlockState state = world.getBlockState(this.pos);
            Block block = state.getBlock();
            if ((block instanceof CommandBlockBlock || block instanceof StructureBlock || block instanceof JigsawBlock) && !serverPlayer.canUseCommandBlock()) {
                world.notifyBlockUpdate(this.pos, state, state, Constants.BlockFlags.DEFAULT);
                return false;
            } else if (serverPlayer.getHeldItemMainhand().onBlockStartBreak(this.pos, serverPlayer)) {
                return false;
            } else if (serverPlayer.blockActionRestricted(world, this.pos, serverPlayer.interactionManager.getGameType())) {
                return false;
            } else {
                world.playEvent(null, 2001, this.pos, Block.getStateId(state));
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
                        block.dropXpOnBlockBreak(serverWorld, this.pos, exp);
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
