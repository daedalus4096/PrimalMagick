package com.verdantartifice.primalmagick.common.blocks.misc;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.util.VoxelShapeUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Block definition for a pillar of any type (e.g. marble).  Pillars are decorative blocks that change
 * their shape depending on whether other pillar blocks are placed immediately above or below them.
 * 
 * @author Daedalus4096
 */
public class PillarBlock extends Block {
    protected static final VoxelShape SHAPE_BASE = VoxelShapeUtils.fromModel(PrimalMagick.resource("block/pillar"));
    protected static final VoxelShape SHAPE_BOTTOM = VoxelShapeUtils.fromModel(PrimalMagick.resource("block/pillar_bottom"));
    protected static final VoxelShape SHAPE_TOP = VoxelShapeUtils.fromModel(PrimalMagick.resource("block/pillar_top"));

    public static final EnumProperty<Type> PROPERTY_TYPE = EnumProperty.create("type", Type.class);
    
    public PillarBlock(Block.Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(PROPERTY_TYPE, Type.BASE));
    }
    
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        // Determine the block's shape based on its "type" blockstate property
        switch (state.getValue(PROPERTY_TYPE)) {
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
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        builder.add(PROPERTY_TYPE);
    }
    
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        // Determine the appropriate pillar state when this block is placed in the world
        return this.getCurrentState(context.getLevel(), context.getClickedPos());
    }
    
    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        // Determine the appropriate pillar state when one of this block's neighbors is updated
        return this.getCurrentState(worldIn, currentPos);
    }
    
    protected BlockState getCurrentState(LevelAccessor world, BlockPos pos) {
        boolean up = (world.getBlockState(pos.above()).getBlock() instanceof PillarBlock);
        boolean down = (world.getBlockState(pos.below()).getBlock() instanceof PillarBlock);
        
        if (up && down) {
            // If there are pillar blocks both above and below this block, use the base pillar type
            return this.defaultBlockState().setValue(PROPERTY_TYPE, Type.BASE);
        } else if (up) {
            // If there is a pillar block above this block but not below, use the bottom pillar type
            return this.defaultBlockState().setValue(PROPERTY_TYPE, Type.BOTTOM);
        } else if (down) {
            // If there is a pillar block below this block but not above, use the top pillar type
            return this.defaultBlockState().setValue(PROPERTY_TYPE, Type.TOP);
        } else {
            // If there are no pillar blocks immediately above or below this block, use the base pillar type
            return this.defaultBlockState().setValue(PROPERTY_TYPE, Type.BASE);
        }
    }
    
    public static enum Type implements StringRepresentable {
        BASE("base"),
        BOTTOM("bottom"),
        TOP("top");
        
        private final String name;
        
        private Type(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
        
        @Override
        public String toString() {
            return this.getSerializedName();
        }
    }
}
