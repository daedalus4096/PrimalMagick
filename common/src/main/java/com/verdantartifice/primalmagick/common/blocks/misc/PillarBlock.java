package com.verdantartifice.primalmagick.common.blocks.misc;

import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import com.verdantartifice.primalmagick.common.util.VoxelShapeUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

/**
 * Block definition for a pillar of any type (e.g. marble).  Pillars are decorative blocks that change
 * their shape depending on whether other pillar blocks are placed immediately above or below them.
 * 
 * @author Daedalus4096
 */
public class PillarBlock extends Block {
    protected static final VoxelShape SHAPE_BASE = VoxelShapeUtils.fromModel(ResourceUtils.loc("block/pillar"));
    protected static final VoxelShape SHAPE_BOTTOM = VoxelShapeUtils.fromModel(ResourceUtils.loc("block/pillar_bottom"));
    protected static final VoxelShape SHAPE_TOP = VoxelShapeUtils.fromModel(ResourceUtils.loc("block/pillar_top"));

    public static final EnumProperty<Type> PROPERTY_TYPE = EnumProperty.create("type", Type.class);
    
    public PillarBlock(Block.Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(PROPERTY_TYPE, Type.BASE));
    }
    
    @Override
    @NotNull
    public VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter worldIn, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        // Determine the block's shape based on its "type" block state property
        return switch (state.getValue(PROPERTY_TYPE)) {
            case BOTTOM -> SHAPE_BOTTOM;
            case TOP -> SHAPE_TOP;
            default -> SHAPE_BASE;
        };
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
    @NotNull
    public BlockState updateShape(@NotNull BlockState state, @NotNull LevelReader level, @NotNull ScheduledTickAccess scheduledTickAccess,
                                  @NotNull BlockPos pos, @NotNull Direction direction, @NotNull BlockPos neighborPos, @NotNull BlockState neighborState,
                                  @NotNull RandomSource random) {
        // Determine the appropriate pillar state when one of this block's neighbors is updated
        return this.getCurrentState(level, pos);
    }
    
    protected BlockState getCurrentState(LevelReader world, BlockPos pos) {
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
    
    public enum Type implements StringRepresentable {
        BASE("base"),
        BOTTOM("bottom"),
        TOP("top");
        
        private final String name;
        
        Type(String name) {
            this.name = name;
        }

        @Override
        @NotNull
        public String getSerializedName() {
            return this.name;
        }
        
        @Override
        public String toString() {
            return this.getSerializedName();
        }
    }
}
