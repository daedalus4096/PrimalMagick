package com.verdantartifice.primalmagic.common.blocks.misc;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.util.VoxelShapeUtils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.util.Direction;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

/**
 * Block definition for a pillar of any type (e.g. marble).  Pillars are decorative blocks that change
 * their shape depending on whether other pillar blocks are placed immediately above or below them.
 * 
 * @author Daedalus4096
 */
public class PillarBlock extends Block {
    protected static final VoxelShape SHAPE_BASE = VoxelShapeUtils.fromModel(new ResourceLocation(PrimalMagic.MODID, "block/pillar"));
    protected static final VoxelShape SHAPE_BOTTOM = VoxelShapeUtils.fromModel(new ResourceLocation(PrimalMagic.MODID, "block/pillar_bottom"));
    protected static final VoxelShape SHAPE_TOP = VoxelShapeUtils.fromModel(new ResourceLocation(PrimalMagic.MODID, "block/pillar_top"));

    public static final EnumProperty<Type> PROPERTY_TYPE = EnumProperty.create("type", Type.class);
    
    public PillarBlock(Block.Properties properties) {
        super(properties);
        this.setDefaultState(this.getDefaultState().with(PROPERTY_TYPE, Type.BASE));
    }
    
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        // Determine the block's shape based on its "type" blockstate property
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
        // Determine the appropriate pillar state when this block is placed in the world
        return this.getCurrentState(context.getWorld(), context.getPos());
    }
    
    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        // Determine the appropriate pillar state when one of this block's neighbors is updated
        return this.getCurrentState(worldIn, currentPos);
    }
    
    protected BlockState getCurrentState(IWorld world, BlockPos pos) {
        boolean up = (world.getBlockState(pos.up()).getBlock() instanceof PillarBlock);
        boolean down = (world.getBlockState(pos.down()).getBlock() instanceof PillarBlock);
        
        if (up && down) {
            // If there are pillar blocks both above and below this block, use the base pillar type
            return this.getDefaultState().with(PROPERTY_TYPE, Type.BASE);
        } else if (up) {
            // If there is a pillar block above this block but not below, use the bottom pillar type
            return this.getDefaultState().with(PROPERTY_TYPE, Type.BOTTOM);
        } else if (down) {
            // If there is a pillar block below this block but not above, use the top pillar type
            return this.getDefaultState().with(PROPERTY_TYPE, Type.TOP);
        } else {
            // If there are no pillar blocks immediately above or below this block, use the base pillar type
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
        public String getString() {
            return this.name;
        }
        
        @Override
        public String toString() {
            return this.getString();
        }
    }
}
