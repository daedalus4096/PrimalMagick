package com.verdantartifice.primalmagick.common.blocks.trees;

import com.mojang.logging.LogUtils;
import com.verdantartifice.primalmagick.common.blockstates.properties.TimePhase;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

/**
 * Base definition for slab blocks that phase in and out over time.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractPhasingSlabBlock extends SlabBlock implements IPhasingBlock {
    protected static final Logger LOGGER = LogUtils.getLogger();

    public AbstractPhasingSlabBlock(Block.Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(PHASE, TimePhase.FULL));
    }

    @Override
    protected void createBlockStateDefinition(@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(PHASE);
    }
    
    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        // Set the block's phase upon placement
        BlockState state = super.getStateForPlacement(context);
        TimePhase phase = this.getCurrentPhase(context.getLevel());
        return state != null ? state.setValue(PHASE, phase) : null;
    }
    
    @Override
    public void randomTick(@NotNull BlockState state, @NotNull ServerLevel worldIn, @NotNull BlockPos pos, @NotNull RandomSource random) {
        // Periodically check to see if the block's phase needs to be updated
        super.randomTick(state, worldIn, pos, random);
        TimePhase newPhase = this.getCurrentPhase(worldIn);
        if (newPhase != state.getValue(PHASE)) {
            worldIn.setBlock(pos, state.setValue(PHASE, newPhase), Block.UPDATE_ALL);
        }
    }
    
    @Override
    @NotNull
    public BlockState updateShape(@NotNull BlockState state, @NotNull LevelReader level, @NotNull ScheduledTickAccess scheduledTickAccess,
                                  @NotNull BlockPos pos, @NotNull Direction direction, @NotNull BlockPos neighborPos, @NotNull BlockState neighborState,
                                  @NotNull RandomSource random) {
        // Immediately check to see if the block's phase needs to be updated when one of its neighbors changes
        BlockState newState = super.updateShape(state, level, scheduledTickAccess, pos, direction, neighborPos, neighborState, random);
        if (this.canPhase(level)) {
            newState = this.updateBlockPhase(newState, level);
        } else {
            LOGGER.warn("Attempting to change time phase with incompatible level type {}", level);
        }
        return newState;
    }
}
