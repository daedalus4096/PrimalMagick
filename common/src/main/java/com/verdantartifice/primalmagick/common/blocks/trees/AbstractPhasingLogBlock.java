package com.verdantartifice.primalmagick.common.blocks.trees;

import com.mojang.logging.LogUtils;
import com.verdantartifice.primalmagick.client.fx.FxDispatcher;
import com.verdantartifice.primalmagick.common.blockstates.properties.TimePhase;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

/**
 * Base definition for log blocks that phase in and out over time.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractPhasingLogBlock extends StrippableLogBlock implements IPhasingBlock {
    public static final BooleanProperty PULSING = BooleanProperty.create("pulsing");

    protected static final Logger LOGGER = LogUtils.getLogger();

    public AbstractPhasingLogBlock(Block stripped, Block.Properties properties) {
        super(stripped, properties);
        this.registerDefaultState(this.defaultBlockState().setValue(PHASE, TimePhase.FULL).setValue(PULSING, false));
    }
    
    /**
     * Get the color of particles to be emitted during animation if this block is pulsing.
     * 
     * @return the intended particle color
     */
    public abstract int getPulseColor();
    
    @Override
    protected void createBlockStateDefinition(@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(PHASE, PULSING);
    }
    
    @Override
    @NotNull
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        // Set the block's phase upon placement
        TimePhase phase = this.getCurrentPhase(context.getLevel());
        return super.getStateForPlacement(context).setValue(PHASE, phase);
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
    
    @Override
    protected BlockState getDefaultStrippedState(BlockState originalState) {
        return super.getDefaultStrippedState(originalState).setValue(PHASE, originalState.getValue(PHASE)).setValue(PULSING, originalState.getValue(PULSING));
    }

    @Override
    public void animateTick(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        super.animateTick(state, level, pos, random);
        if (state.getValue(PULSING) && random.nextInt(4) == 0) {
            FxDispatcher.INSTANCE.spellImpact(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 2, this.getPulseColor());
        }
    }
}
