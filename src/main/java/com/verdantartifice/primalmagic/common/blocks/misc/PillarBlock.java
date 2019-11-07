package com.verdantartifice.primalmagic.common.blocks.misc;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.util.Direction;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

public class PillarBlock extends Block {
    protected static final VoxelShape PART_CENTRAL = Block.makeCuboidShape(2, 0, 2, 14, 16, 14);
    protected static final VoxelShape PART_BOTTOM = Block.makeCuboidShape(0, 0, 0, 16, 4, 16);
    protected static final VoxelShape PART_TOP = Block.makeCuboidShape(0, 12, 0, 16, 16, 16);
    protected static final VoxelShape SHAPE_BASE = PART_CENTRAL;
    protected static final VoxelShape SHAPE_BOTTOM = VoxelShapes.or(PART_CENTRAL, PART_BOTTOM);
    protected static final VoxelShape SHAPE_TOP = VoxelShapes.or(PART_CENTRAL, PART_TOP);
    
    public static final EnumProperty<Type> PROPERTY_TYPE = EnumProperty.create("type", Type.class);
    
    public PillarBlock(Block.Properties properties) {
        super(properties);
        this.setDefaultState(this.getDefaultState().with(PROPERTY_TYPE, Type.BASE));
    }
    
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        switch (state.get(PROPERTY_TYPE)) {
        case BOTTOM:
            return SHAPE_BOTTOM;
        case TOP:
            return SHAPE_TOP;
        case BASE:
        default:
            return SHAPE_BASE;
        }
    }
    
    @Override
    protected void fillStateContainer(Builder<Block, BlockState> builder) {
        builder.add(PROPERTY_TYPE);
    }
    
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getCurrentState(context.getWorld(), context.getPos());
    }
    
    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        return this.getCurrentState(worldIn, currentPos);
    }
    
    protected BlockState getCurrentState(IWorld world, BlockPos pos) {
        boolean up = (world.getBlockState(pos.up()).getBlock() instanceof PillarBlock);
        boolean down = (world.getBlockState(pos.down()).getBlock() instanceof PillarBlock);
        
        if (up && down) {
            return this.getDefaultState().with(PROPERTY_TYPE, Type.BASE);
        } else if (up) {
            return this.getDefaultState().with(PROPERTY_TYPE, Type.BOTTOM);
        } else if (down) {
            return this.getDefaultState().with(PROPERTY_TYPE, Type.TOP);
        } else {
            return this.getDefaultState().with(PROPERTY_TYPE, Type.BASE);
        }
    }
    
    public static enum Type implements IStringSerializable {
        BASE("base"),
        BOTTOM("bottom"),
        TOP("top");
        
        private final String name;
        
        private Type(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return this.name;
        }
        
        @Override
        public String toString() {
            return this.name;
        }
    }
}
