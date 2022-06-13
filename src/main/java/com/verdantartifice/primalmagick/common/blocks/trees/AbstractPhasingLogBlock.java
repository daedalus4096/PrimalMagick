package com.verdantartifice.primalmagick.common.blocks.trees;

import com.verdantartifice.primalmagick.client.fx.FxDispatcher;
import com.verdantartifice.primalmagick.common.blockstates.properties.TimePhase;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;

/**
 * Base definition for log blocks that phase in and out over time.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractPhasingLogBlock extends StrippableLogBlock {
    public static final EnumProperty<TimePhase> PHASE = EnumProperty.create("phase", TimePhase.class);
    public static final BooleanProperty PULSING = BooleanProperty.create("pulsing");
    
    public AbstractPhasingLogBlock(Block stripped, Block.Properties properties) {
        super(stripped, properties);
        this.registerDefaultState(this.defaultBlockState().setValue(PHASE, TimePhase.FULL).setValue(PULSING, false));
    }
    
    /**
     * Get the current phase of the block based on the current game time.
     * 
     * @param world the game world
     * @return the block's current phase
     */
    public abstract TimePhase getCurrentPhase(LevelAccessor world);
    
    /**
     * Get the color of particles to be emitted during animation if this block is pulsing.
     * 
     * @return the intended particle color
     */
    public abstract int getPulseColor();
    
    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(PHASE, PULSING);
    }
    
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        // Set the block's phase upon placement
        TimePhase phase = this.getCurrentPhase(context.getLevel());
        return super.getStateForPlacement(context).setValue(PHASE, phase);
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public void randomTick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource random) {
        // Periodically check to see if the block's phase needs to be updated
        super.randomTick(state, worldIn, pos, random);
        TimePhase newPhase = this.getCurrentPhase(worldIn);
        if (newPhase != state.getValue(PHASE)) {
            worldIn.setBlock(pos, state.setValue(PHASE, newPhase), Block.UPDATE_ALL);
        }
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        // Immediately check to see if the block's phase needs to be updated when one of its neighbors changes
        BlockState state = super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
        TimePhase newPhase = this.getCurrentPhase(worldIn);
        if (newPhase != state.getValue(PHASE)) {
            state = state.setValue(PHASE, newPhase);
        }
        return state;
    }
    
    @Override
    protected BlockState getDefaultStrippedState(BlockState originalState) {
        return super.getDefaultStrippedState(originalState).setValue(PHASE, originalState.getValue(PHASE)).setValue(PULSING, originalState.getValue(PULSING));
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        super.animateTick(state, level, pos, random);
        if (state.getValue(PULSING) && random.nextInt(4) == 0) {
            FxDispatcher.INSTANCE.spellImpact(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 2, this.getPulseColor());
        }
    }
}
