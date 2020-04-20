package com.verdantartifice.primalmagic.common.blocks.misc;

import net.minecraft.block.AbstractGlassBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

/**
 * Block definition for skyglass.  Skyglass is completely transparent except at the edge, connecting
 * to other adjacent skyglass blocks.  It also is not destroyed upon harvesting.
 * 
 * @author Daedalus4096
 */
public class SkyglassBlock extends AbstractGlassBlock {
    public static final BooleanProperty UP = BooleanProperty.create("up");
    public static final BooleanProperty DOWN = BooleanProperty.create("down");
    public static final BooleanProperty NORTH = BooleanProperty.create("north");
    public static final BooleanProperty SOUTH = BooleanProperty.create("south");
    public static final BooleanProperty WEST = BooleanProperty.create("west");
    public static final BooleanProperty EAST = BooleanProperty.create("east");
    
    public SkyglassBlock(Block.Properties properties) {
        super(properties);
        this.setDefaultState(this.getDefaultState().with(UP, Boolean.FALSE).with(DOWN, Boolean.FALSE).with(NORTH, Boolean.FALSE).with(SOUTH, Boolean.FALSE).with(WEST, Boolean.FALSE).with(EAST, Boolean.FALSE));
    }
    
    @Override
    protected void fillStateContainer(Builder<Block, BlockState> builder) {
        builder.add(UP, DOWN, NORTH, SOUTH, WEST, EAST);
    }
    
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        // Determine the block's connections when it is placed into the world
        return this.getCurrentState(this.getDefaultState(), context.getWorld(), context.getPos());
    }
    
    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        // Determine the block's connections when one of its neighbors is updated
        return this.getCurrentState(stateIn, worldIn, currentPos);
    }
    
    protected BlockState getCurrentState(BlockState state, IWorld world, BlockPos pos) {
        return this.getDefaultState()
                .with(UP, this.isSideConnected(state, world, pos, Direction.UP))
                .with(DOWN, this.isSideConnected(state, world, pos, Direction.DOWN))
                .with(NORTH, this.isSideConnected(state, world, pos, Direction.NORTH))
                .with(SOUTH, this.isSideConnected(state, world, pos, Direction.SOUTH))
                .with(WEST, this.isSideConnected(state, world, pos, Direction.WEST))
                .with(EAST, this.isSideConnected(state, world, pos, Direction.EAST));
    }
    
    protected boolean isSideConnected(BlockState state, IWorld world, BlockPos pos, Direction dir) {
        BlockState adjacent = world.getBlockState(pos.offset(dir));
        return adjacent != null && state.getBlock() == adjacent.getBlock();
    }
}
