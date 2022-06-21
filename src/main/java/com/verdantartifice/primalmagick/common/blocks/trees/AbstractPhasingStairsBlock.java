package com.verdantartifice.primalmagick.common.blocks.trees;

import java.util.function.Supplier;

import com.verdantartifice.primalmagick.common.blockstates.properties.TimePhase;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.EnumProperty;

/**
 * Base definition for stairs blocks that phase in and out over time.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractPhasingStairsBlock extends StairBlock {
    public static final EnumProperty<TimePhase> PHASE = EnumProperty.create("phase", TimePhase.class);

    public AbstractPhasingStairsBlock(Supplier<BlockState> state, Block.Properties properties) {
        super(state, properties);
        this.registerDefaultState(this.defaultBlockState().setValue(PHASE, TimePhase.FULL));
    }
    
    /**
     * Get the current phase of the block based on the current game time.
     * 
     * @param world the game world
     * @return the block's current phase
     */
    protected abstract TimePhase getCurrentPhase(LevelAccessor world);

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(PHASE);
    }
    
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        // Set the block's phase upon placement
        TimePhase phase = this.getCurrentPhase(context.getLevel());
        return super.getStateForPlacement(context).setValue(PHASE, phase);
    }
    
    @Override
    public void randomTick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource random) {
        // Periodically check to see if the block's phase needs to be updated
        super.randomTick(state, worldIn, pos, random);
        TimePhase newPhase = this.getCurrentPhase(worldIn);
        if (newPhase != state.getValue(PHASE)) {
            worldIn.setBlock(pos, state.setValue(PHASE, newPhase), Block.UPDATE_ALL);
        }
    }
    
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
}
