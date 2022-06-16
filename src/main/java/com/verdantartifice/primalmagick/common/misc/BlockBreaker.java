package com.verdantartifice.primalmagick.common.misc;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.common.enchantments.EnchantmentsPM;
import com.verdantartifice.primalmagick.common.wands.IWand;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CommandBlock;
import net.minecraft.world.level.block.JigsawBlock;
import net.minecraft.world.level.block.StructureBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
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
    protected static final Map<ResourceLocation, ConcurrentNavigableMap<Integer, Map<BlockPos, BlockBreaker>>> SCHEDULE = new ConcurrentHashMap<>();
    
    protected final float power;
    protected final BlockPos pos;
    protected final BlockState targetBlock;
    protected final float currentDurability;
    protected final float maxDurability;
    protected final Player player;
    protected final ItemStack tool;
    protected final boolean oneShot;
    protected final boolean skipEvent;
    protected final boolean alwaysDrop;
    protected final Optional<Boolean> silkTouchOverride;
    protected final Optional<Integer> fortuneOverride;
    
    protected BlockBreaker(float power, @Nonnull BlockPos pos, @Nonnull BlockState targetBlock, float currentDurability, float maxDurability, @Nonnull Player player, 
            ItemStack tool, boolean oneShot, boolean skipEvent, boolean alwaysDrop, Optional<Boolean> silkTouchOverride, Optional<Integer> fortuneOverride) {
        this.power = power;
        this.pos = pos;
        this.targetBlock = targetBlock;
        this.currentDurability = currentDurability;
        this.maxDurability = maxDurability;
        this.player = player;
        this.tool = tool;
        this.oneShot = oneShot;
        this.skipEvent = skipEvent;
        this.alwaysDrop = alwaysDrop;
        this.silkTouchOverride = silkTouchOverride;
        this.fortuneOverride = fortuneOverride;
    }
    
    /**
     * Schedules the given block breaker to be run on the given world after a given delay.
     * 
     * @param world the world in which to run the block breaker
     * @param delayTicks the delay, in ticks, to wait before running the breaker, minimum zero
     * @param breaker the block breaker to be run
     * @return true if the breaker was successfully scheduled, false otherwise
     */
    public static boolean schedule(@Nonnull Level world, int delayTicks, @Nullable BlockBreaker breaker) {
        if (breaker == null) {
            // Don't allow null breakers in the schedule
            return false;
        } else {
            int delay = Math.max(0, delayTicks);
            SCHEDULE.computeIfAbsent(world.dimension().location(), key -> {
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
    public static Iterable<BlockBreaker> tick(@Nonnull Level world) {
        ConcurrentNavigableMap<Integer, Map<BlockPos, BlockBreaker>> tree = SCHEDULE.get(world.dimension().location());
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
                SCHEDULE.put(world.dimension().location(), newTree);
            }
            return retVal;
        }
    }
    
    public static boolean hasBreakerQueued(@Nonnull Level world, @Nonnull BlockPos pos) {
        ConcurrentNavigableMap<Integer, Map<BlockPos, BlockBreaker>> tree = SCHEDULE.get(world.dimension().location());
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
    public BlockBreaker execute(@Nonnull Level world) {
        BlockBreaker retVal = null;
        BlockState state = world.getBlockState(this.pos);
        if (state == this.targetBlock) {
            // Only allow block breakers to act on blocks that could normally be broken by a player
            if (world.mayInteract(this.player, this.pos) && state.getDestroySpeed(world, this.pos) >= 0.0F) {
                // Send packets showing the visual effects of the block breaker's progress
                world.destroyBlockProgress(this.pos.hashCode(), this.pos, (int)((1.0F - this.currentDurability / this.maxDurability) * 10.0F));
                
                // Calculate new block durability and check to see if the block breaking is done
                float newDurability = this.currentDurability - this.power;
                if (newDurability <= 0.0F) {
                    // Do block break
                    this.doHarvest(world);
                    world.destroyBlockProgress(this.pos.hashCode(), this.pos, -1);
                } else if (!this.oneShot) {
                    // Queue up another round of breaking progress
                    retVal = new BlockBreaker.Builder(this).currentDurability(newDurability).build();
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
    protected boolean doHarvest(@Nonnull Level world) {
        if (world.isClientSide || !(this.player instanceof ServerPlayer)) {
            return false;
        }
        ServerPlayer serverPlayer = (ServerPlayer)this.player;
        ServerLevel serverWorld = (ServerLevel)world;
        int exp = this.skipEvent ? 0 : ForgeHooks.onBlockBreakEvent(world, serverPlayer.gameMode.getGameModeForPlayer(), serverPlayer, this.pos);
        if (exp == -1) {
            return false;
        } else {
            BlockEntity tile = world.getBlockEntity(this.pos);
            BlockState state = world.getBlockState(this.pos);
            Block block = state.getBlock();
            
            // If the experience for the block was zero because the player is using a Break spell and thus the wrong tool type, recalculate
            if (exp == 0 && !ForgeHooks.isCorrectToolForDrops(state, this.player)) {
                exp = state.getExpDrop(world, world.random, this.pos, this.getFortuneLevel(), this.getSilkTouchLevel());
            }
            
            if ((block instanceof CommandBlock || block instanceof StructureBlock || block instanceof JigsawBlock) && !serverPlayer.canUseGameMasterBlocks()) {
                world.sendBlockUpdated(this.pos, state, state, Block.UPDATE_ALL);
                return false;
            } else if (serverPlayer.getMainHandItem().onBlockStartBreak(this.pos, serverPlayer)) {
                return false;
            } else if (serverPlayer.blockActionRestricted(world, this.pos, serverPlayer.gameMode.getGameModeForPlayer())) {
                return false;
            } else {
                world.levelEvent(null, 2001, this.pos, Block.getId(state));
                if (serverPlayer.gameMode.isCreative()) {
                    this.removeBlock(world, false);
                    return true;
                } else {
                    boolean canHarvest = (this.alwaysDrop || state.canHarvestBlock(world, this.pos, serverPlayer));
                    boolean success = this.removeBlock(world, canHarvest);
                    if (success && canHarvest) {
                        block.playerDestroy(world, serverPlayer, this.pos, state, tile, this.getHarvestTool(serverPlayer));
                    }
                    if (success && exp > 0) {
                        block.popExperience(serverWorld, this.pos, exp);
                    }
                    return true;
                }
            }
        }
    }
    
    protected int getFortuneLevel() {
        if (this.fortuneOverride.isPresent()) {
            return this.fortuneOverride.get();
        } else if (!this.tool.isEmpty()) {
            if (this.tool.getItem() instanceof IWand) {
                return this.tool.getEnchantmentLevel(EnchantmentsPM.TREASURE.get());
            } else {
                return this.tool.getEnchantmentLevel(Enchantments.BLOCK_FORTUNE);
            }
        } else {
            return 0;
        }
    }
    
    protected int getSilkTouchLevel() {
        if (this.silkTouchOverride.isPresent()) {
            return this.silkTouchOverride.get() ? 1 : 0;
        } else if (!this.tool.isEmpty()) {
            return this.tool.getEnchantmentLevel(Enchantments.SILK_TOUCH);
        } else {
            return 0;
        }
    }
    
    /**
     * Get a copy of the tool the player is using to trigger the breaker, applying any enchantment overrides.
     * 
     * @param player the player whose tool to get
     * @return a copy of the triggering tool
     */
    protected ItemStack getHarvestTool(Player player) {
        ItemStack stack = this.tool.copy();
        if (stack.isEmpty()) {
            stack = player.getMainHandItem().copy();
        }
        if (this.silkTouchOverride.isPresent() || this.fortuneOverride.isPresent()) {
            Map<Enchantment, Integer> enchantMap = EnchantmentHelper.getEnchantments(stack);
            this.silkTouchOverride.ifPresent(silk -> {
                if (silk) {
                    enchantMap.put(Enchantments.SILK_TOUCH, 1);
                }
            });
            this.fortuneOverride.ifPresent(fortune -> {
                int newFortune = Math.max(fortune, enchantMap.getOrDefault(Enchantments.BLOCK_FORTUNE, 0));
                if (newFortune > 0) {
                    enchantMap.put(Enchantments.BLOCK_FORTUNE, newFortune);
                }
            });
            EnchantmentHelper.setEnchantments(enchantMap, stack);
        }
        return stack;
    }
    
    /**
     * Actually remove this breaker's block from the give world and, if specified, do its drops.
     * 
     * @param world the world to break in
     * @param canHarvest whether the player is able to harvest this block for drops
     * @return true if the block is successfully removed, false otherwise
     * @see {@link net.minecraft.server.management.PlayerInteractionManager#removeBlock}
     */
    protected boolean removeBlock(@Nonnull Level world, boolean canHarvest) {
        BlockState state = world.getBlockState(this.pos);
        boolean removed = state.onDestroyedByPlayer(world, this.pos, this.player, canHarvest, world.getFluidState(this.pos));
        if (removed) {
            state.getBlock().destroy(world, this.pos, state);
        }
        return removed;
    }

    public static class Builder {
        protected float power = 0.0F;
        protected BlockPos pos = BlockPos.ZERO;
        protected BlockState targetBlock = null;
        protected float currentDurability = 0.0F;
        protected float maxDurability = 0.0F;
        protected Player player = null;
        protected ItemStack tool = ItemStack.EMPTY;
        protected boolean oneShot = false;
        protected boolean skipEvent = false;
        protected boolean alwaysDrop = false;
        protected Optional<Boolean> silkTouchOverride = Optional.empty();
        protected Optional<Integer> fortuneOverride = Optional.empty();
        
        public Builder() {}
        
        public Builder(BlockBreaker existing) {
            this.power = existing.power;
            this.pos = existing.pos;
            this.targetBlock = existing.targetBlock;
            this.currentDurability = existing.currentDurability;
            this.maxDurability = existing.maxDurability;
            this.player = existing.player;
            this.tool = existing.tool;
            this.oneShot = existing.oneShot;
            this.skipEvent = existing.skipEvent;
            this.alwaysDrop = existing.alwaysDrop;
            this.silkTouchOverride = existing.silkTouchOverride;
            this.fortuneOverride = existing.fortuneOverride;
        }
        
        public Builder power(float power) {
            this.power = power;
            return this;
        }
        
        public Builder target(BlockPos pos, BlockState targetBlock) {
            this.pos = pos;
            this.targetBlock = targetBlock;
            return this;
        }
        
        public Builder durability(float max) {
            return this.durability(max, max);
        }
        
        public Builder durability(float current, float max) {
            this.currentDurability = current;
            this.maxDurability = max;
            return this;
        }
        
        public Builder currentDurability(float current) {
            this.currentDurability = current;
            return this;
        }
        
        public Builder player(Player player) {
            this.player = player;
            return this;
        }
        
        public Builder tool(ItemStack tool) {
            this.tool = tool;
            return this;
        }
        
        public Builder oneShot() {
            this.oneShot = true;
            return this;
        }
        
        public Builder skipEvent() {
            this.skipEvent = true;
            return this;
        }
        
        public Builder alwaysDrop() {
            this.alwaysDrop = true;
            return this;
        }
        
        public Builder silkTouch(boolean silk) {
            this.silkTouchOverride = Optional.of(Boolean.valueOf(silk));
            return this;
        }
        
        public Builder fortune(int fortune) {
            this.fortuneOverride = Optional.of(Integer.valueOf(fortune));
            return this;
        }
        
        private void validate() {
            if (this.targetBlock == null) {
                throw new IllegalStateException("Missing target block in BlockBreaker builder!");
            }
            if (this.player == null) {
                throw new IllegalStateException("Missing player in BlockBreaker builder!");
            }
            if (this.tool == null) {
                throw new IllegalStateException("Invalid tool in BlockBreaker builder!");
            }
        }
        
        public BlockBreaker build() {
            this.validate();
            return new BlockBreaker(this.power, this.pos, this.targetBlock, this.currentDurability, this.maxDurability, this.player, this.tool, this.oneShot, this.skipEvent, this.alwaysDrop, 
                    this.silkTouchOverride, this.fortuneOverride);
        }
    }
}
