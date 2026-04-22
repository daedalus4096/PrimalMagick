package com.verdantartifice.primalmagick.common.blocks.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TransparentBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.NotNull;

/**
 * Block definition for skyglass.  Skyglass is completely transparent except at the edge, connecting
 * to other adjacent skyglass blocks.  It also is not destroyed upon harvesting.
 * 
 * @author Daedalus4096
 */
public class SkyglassBlock extends TransparentBlock {
    public static final BooleanProperty UP = BooleanProperty.create("up");
    public static final BooleanProperty DOWN = BooleanProperty.create("down");
    public static final BooleanProperty NORTH = BooleanProperty.create("north");
    public static final BooleanProperty SOUTH = BooleanProperty.create("south");
    public static final BooleanProperty WEST = BooleanProperty.create("west");
    public static final BooleanProperty EAST = BooleanProperty.create("east");
    
    public SkyglassBlock(Block.Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(UP, Boolean.FALSE).setValue(DOWN, Boolean.FALSE).setValue(NORTH, Boolean.FALSE).setValue(SOUTH, Boolean.FALSE).setValue(WEST, Boolean.FALSE).setValue(EAST, Boolean.FALSE));
    }
    
    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        builder.add(UP, DOWN, NORTH, SOUTH, WEST, EAST);
    }
    
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        // Determine the block's connections when it is placed into the world
        return this.getCurrentState(this.defaultBlockState(), context.getLevel(), context.getClickedPos());
    }

    @Override
    @NotNull
    public BlockState updateShape(@NotNull BlockState state, @NotNull LevelReader level, @NotNull ScheduledTickAccess scheduledTickAccess,
                                  @NotNull BlockPos pos, @NotNull Direction direction, @NotNull BlockPos neighborPos, @NotNull BlockState neighborState,
                                  @NotNull RandomSource random) {
        // Determine the block's connections when one of its neighbors is updated
        return this.getCurrentState(state, level, pos);
    }
    
    protected BlockState getCurrentState(BlockState state, LevelReader world, BlockPos pos) {
        return this.defaultBlockState()
                .setValue(UP, this.isSideConnected(state, world, pos, Direction.UP))
                .setValue(DOWN, this.isSideConnected(state, world, pos, Direction.DOWN))
                .setValue(NORTH, this.isSideConnected(state, world, pos, Direction.NORTH))
                .setValue(SOUTH, this.isSideConnected(state, world, pos, Direction.SOUTH))
                .setValue(WEST, this.isSideConnected(state, world, pos, Direction.WEST))
                .setValue(EAST, this.isSideConnected(state, world, pos, Direction.EAST));
    }
    
    protected boolean isSideConnected(BlockState state, LevelReader world, BlockPos pos, Direction dir) {
        BlockState adjacent = world.getBlockState(pos.relative(dir));
        return adjacent.is(state.getBlock());
    }
}
