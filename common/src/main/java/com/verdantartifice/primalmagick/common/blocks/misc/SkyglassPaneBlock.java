package com.verdantartifice.primalmagick.common.blocks.misc;

import com.google.common.collect.Maps;
import com.verdantartifice.primalmagick.common.blockstates.properties.SkyglassPaneSide;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Map;
import java.util.function.ToIntFunction;

/**
 * Block definition for a skyglass pane.  Skyglass is completely transparent except at the edge,
 * connecting to other adjacent skyglass blocks.  It also is not destroyed upon harvesting.
 * 
 * @author Daedalus4096
 * @see {@link net.minecraft.block.PaneBlock}
 */
public class SkyglassPaneBlock extends Block implements SimpleWaterloggedBlock {
    public static final EnumProperty<SkyglassPaneSide> NORTH = EnumProperty.create("north", SkyglassPaneSide.class);
    public static final EnumProperty<SkyglassPaneSide> EAST = EnumProperty.create("east", SkyglassPaneSide.class);
    public static final EnumProperty<SkyglassPaneSide> SOUTH = EnumProperty.create("south", SkyglassPaneSide.class);
    public static final EnumProperty<SkyglassPaneSide> WEST = EnumProperty.create("west", SkyglassPaneSide.class);
    public static final EnumProperty<SkyglassPaneSide> UP = EnumProperty.create("up", SkyglassPaneSide.class);
    public static final EnumProperty<SkyglassPaneSide> DOWN = EnumProperty.create("down", SkyglassPaneSide.class);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    
    protected static final Map<Direction, EnumProperty<SkyglassPaneSide>> FACING_TO_PROPERTY_MAP = Util.make(Maps.newEnumMap(Direction.class), (map) -> {
        map.put(Direction.NORTH, NORTH);
        map.put(Direction.EAST, EAST);
        map.put(Direction.SOUTH, SOUTH);
        map.put(Direction.WEST, WEST);
        map.put(Direction.UP, UP);
        map.put(Direction.DOWN, DOWN);
    });

    protected final VoxelShape[] collisionShapes;
    protected final VoxelShape[] shapes;
    protected final Object2IntMap<BlockState> stateShapeIndices = new Object2IntOpenHashMap<>();

    public SkyglassPaneBlock(Block.Properties properties) {
        super(properties);
        this.shapes = this.makeShapes(1.0F, 1.0F, 16.0F, 0.0F, 16.0F);
        this.collisionShapes = this.makeShapes(1.0F, 1.0F, 16.0F, 0.0F, 16.0F);
        this.registerDefaultState(this.defaultBlockState().setValue(NORTH, SkyglassPaneSide.NONE).setValue(EAST, SkyglassPaneSide.NONE).setValue(SOUTH, SkyglassPaneSide.NONE).setValue(WEST, SkyglassPaneSide.NONE).setValue(UP, SkyglassPaneSide.NONE).setValue(DOWN, SkyglassPaneSide.NONE).setValue(WATERLOGGED, Boolean.valueOf(false)));
    }
    
    protected VoxelShape[] makeShapes(float nodeWidth, float extensionWidth, float p_196408_3_, float p_196408_4_, float p_196408_5_) {
        float f = 8.0F - nodeWidth;
        float f1 = 8.0F + nodeWidth;
        float f2 = 8.0F - extensionWidth;
        float f3 = 8.0F + extensionWidth;
        VoxelShape voxelshape = Block.box((double)f, 0.0D, (double)f, (double)f1, (double)p_196408_3_, (double)f1);
        VoxelShape voxelshape1 = Block.box((double)f2, (double)p_196408_4_, 0.0D, (double)f3, (double)p_196408_5_, (double)f3);
        VoxelShape voxelshape2 = Block.box((double)f2, (double)p_196408_4_, (double)f2, (double)f3, (double)p_196408_5_, 16.0D);
        VoxelShape voxelshape3 = Block.box(0.0D, (double)p_196408_4_, (double)f2, (double)f3, (double)p_196408_5_, (double)f3);
        VoxelShape voxelshape4 = Block.box((double)f2, (double)p_196408_4_, (double)f2, 16.0D, (double)p_196408_5_, (double)f3);
        VoxelShape voxelshape5 = Shapes.or(voxelshape1, voxelshape4);
        VoxelShape voxelshape6 = Shapes.or(voxelshape2, voxelshape3);
        VoxelShape[] avoxelshape = new VoxelShape[]{Shapes.empty(), voxelshape2, voxelshape3, voxelshape6, voxelshape1, Shapes.or(voxelshape2, voxelshape1), Shapes.or(voxelshape3, voxelshape1), Shapes.or(voxelshape6, voxelshape1), voxelshape4, Shapes.or(voxelshape2, voxelshape4), Shapes.or(voxelshape3, voxelshape4), Shapes.or(voxelshape6, voxelshape4), voxelshape5, Shapes.or(voxelshape2, voxelshape5), Shapes.or(voxelshape3, voxelshape5), Shapes.or(voxelshape6, voxelshape5)};

        for (int i = 0; i < avoxelshape.length; i++) {
            avoxelshape[i] = Shapes.or(voxelshape, avoxelshape[i]);
        }
        
        return avoxelshape;
    }
    
    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        builder.add(UP, DOWN, NORTH, EAST, WEST, SOUTH, WATERLOGGED);
    }
    
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return this.shapes[this.getShapeIndex(state)];
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return this.collisionShapes[this.getShapeIndex(state)];
    }
    
    protected int getShapeIndex(BlockState state) {
        ToIntFunction<BlockState> mappingFunction = (s) -> {
            int index = 0;
            if (s.getValue(NORTH) == SkyglassPaneSide.GLASS || s.getValue(NORTH) == SkyglassPaneSide.OTHER) {
                index |= getDirectionMask(Direction.NORTH);
            }
            if (s.getValue(SOUTH) == SkyglassPaneSide.GLASS || s.getValue(SOUTH) == SkyglassPaneSide.OTHER) {
                index |= getDirectionMask(Direction.SOUTH);
            }
            if (s.getValue(WEST) == SkyglassPaneSide.GLASS || s.getValue(WEST) == SkyglassPaneSide.OTHER) {
                index |= getDirectionMask(Direction.WEST);
            }
            if (s.getValue(EAST) == SkyglassPaneSide.GLASS || s.getValue(EAST) == SkyglassPaneSide.OTHER) {
                index |= getDirectionMask(Direction.EAST);
            }
            return index;
        };
        return this.stateShapeIndices.computeIfAbsent(state, mappingFunction);
    }
    
    protected static int getDirectionMask(Direction dir) {
        return 1 << dir.get2DDataValue();
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        // Determine the block's connections when it is placed into the world
        return this.getCurrentState(this.defaultBlockState(), context.getLevel(), context.getClickedPos());
    }
    
    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (stateIn.getValue(WATERLOGGED)) {
            worldIn.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(worldIn));
        }
        
        // Determine the block's connections when one of its neighbors is updated
        return this.getCurrentState(stateIn, worldIn, currentPos);
    }
    
    protected BlockState getCurrentState(BlockState state, LevelAccessor world, BlockPos pos) {
        return this.defaultBlockState()
                .setValue(UP, this.getSideConnection(state, world, pos, Direction.UP))
                .setValue(DOWN, this.getSideConnection(state, world, pos, Direction.DOWN))
                .setValue(NORTH, this.getSideConnection(state, world, pos, Direction.NORTH))
                .setValue(SOUTH, this.getSideConnection(state, world, pos, Direction.SOUTH))
                .setValue(WEST, this.getSideConnection(state, world, pos, Direction.WEST))
                .setValue(EAST, this.getSideConnection(state, world, pos, Direction.EAST))
                .setValue(WATERLOGGED, world.getFluidState(pos).getType() == Fluids.WATER);
    }
    
    protected SkyglassPaneSide getSideConnection(BlockState state, LevelAccessor world, BlockPos pos, Direction dir) {
        BlockState adjacent = world.getBlockState(pos.relative(dir));
        if (adjacent == null || isExceptionForConnection(adjacent)) {
            return SkyglassPaneSide.NONE;
        } else if (state.getBlock() == adjacent.getBlock()) {
            return SkyglassPaneSide.GLASS;
        } else if (adjacent.isFaceSturdy(world, pos.relative(dir), dir.getOpposite()) || adjacent.getBlock() instanceof SkyglassPaneBlock) {
            return SkyglassPaneSide.OTHER;
        } else {
            return SkyglassPaneSide.NONE;
        }
    }
    
    @Override
    public boolean skipRendering(BlockState state, BlockState adjacentBlockState, Direction side) {
        if (adjacentBlockState.getBlock() == state.getBlock()) {
            if (!side.getAxis().isHorizontal()) {
                return true;
            }
            if ( state.getValue(FACING_TO_PROPERTY_MAP.get(side)) == SkyglassPaneSide.GLASS &&
                 adjacentBlockState.getValue(FACING_TO_PROPERTY_MAP.get(side.getOpposite())) == SkyglassPaneSide.GLASS ) {
                return true;
            }
        }
        return super.skipRendering(state, adjacentBlockState, side);
    }
    
    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }
    
    @Override
    public boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }
    
    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
        return !state.getValue(WATERLOGGED);
    }
    
    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        switch (rot) {
        case CLOCKWISE_180:
            return state.setValue(NORTH, state.getValue(SOUTH)).setValue(EAST, state.getValue(WEST)).setValue(SOUTH, state.getValue(NORTH)).setValue(WEST, state.getValue(EAST));
        case CLOCKWISE_90:
            return state.setValue(NORTH, state.getValue(WEST)).setValue(EAST, state.getValue(NORTH)).setValue(SOUTH, state.getValue(EAST)).setValue(WEST, state.getValue(SOUTH));
        case COUNTERCLOCKWISE_90:
            return state.setValue(NORTH, state.getValue(EAST)).setValue(EAST, state.getValue(SOUTH)).setValue(SOUTH, state.getValue(WEST)).setValue(WEST, state.getValue(NORTH));
        default:
            return state;
        }
    }
    
    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        switch (mirrorIn) {
        case LEFT_RIGHT:
            return state.setValue(NORTH, state.getValue(SOUTH)).setValue(SOUTH, state.getValue(NORTH));
        case FRONT_BACK:
            return state.setValue(EAST, state.getValue(WEST)).setValue(WEST, state.getValue(EAST));
        default:
            return state;
        }
    }
}
