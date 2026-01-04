package com.verdantartifice.primalmagick.common.blocks.misc;

import com.google.common.collect.Maps;
import com.verdantartifice.primalmagick.common.blockstates.properties.SkyglassPaneSide;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
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
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.ToIntFunction;

/**
 * Block definition for a skyglass pane.  Skyglass is completely transparent except at the edge,
 * connecting to other adjacent skyglass blocks.  It also is not destroyed upon harvesting.
 * 
 * @author Daedalus4096
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
        this.registerDefaultState(this.defaultBlockState().setValue(NORTH, SkyglassPaneSide.NONE).setValue(EAST, SkyglassPaneSide.NONE).setValue(SOUTH, SkyglassPaneSide.NONE).setValue(WEST, SkyglassPaneSide.NONE).setValue(UP, SkyglassPaneSide.NONE).setValue(DOWN, SkyglassPaneSide.NONE).setValue(WATERLOGGED, Boolean.FALSE));
    }
    
    protected VoxelShape[] makeShapes(float nodeWidth, float extensionWidth, float nodeHeight, float extensionBottom, float extensionHeight) {
        float f = 8.0F - nodeWidth;
        float f1 = 8.0F + nodeWidth;
        float f2 = 8.0F - extensionWidth;
        float f3 = 8.0F + extensionWidth;
        VoxelShape voxelShape = Block.box(f, 0.0D, f, f1, nodeHeight, f1);
        VoxelShape voxelShape1 = Block.box(f2, extensionBottom, 0.0D, f3, extensionHeight, f3);
        VoxelShape voxelShape2 = Block.box(f2, extensionBottom, f2, f3, extensionHeight, 16.0D);
        VoxelShape voxelShape3 = Block.box(0.0D, extensionBottom, f2, f3, extensionHeight, f3);
        VoxelShape voxelShape4 = Block.box(f2, extensionBottom, f2, 16.0D, extensionHeight, f3);
        VoxelShape voxelShape5 = Shapes.or(voxelShape1, voxelShape4);
        VoxelShape voxelShape6 = Shapes.or(voxelShape2, voxelShape3);
        VoxelShape[] aVoxelShape = new VoxelShape[]{Shapes.empty(), voxelShape2, voxelShape3, voxelShape6, voxelShape1, Shapes.or(voxelShape2, voxelShape1), Shapes.or(voxelShape3, voxelShape1), Shapes.or(voxelShape6, voxelShape1), voxelShape4, Shapes.or(voxelShape2, voxelShape4), Shapes.or(voxelShape3, voxelShape4), Shapes.or(voxelShape6, voxelShape4), voxelShape5, Shapes.or(voxelShape2, voxelShape5), Shapes.or(voxelShape3, voxelShape5), Shapes.or(voxelShape6, voxelShape5)};

        for (int i = 0; i < aVoxelShape.length; i++) {
            aVoxelShape[i] = Shapes.or(voxelShape, aVoxelShape[i]);
        }
        
        return aVoxelShape;
    }
    
    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        builder.add(UP, DOWN, NORTH, EAST, WEST, SOUTH, WATERLOGGED);
    }
    
    @Override
    @NotNull
    public VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter worldIn, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return this.shapes[this.getShapeIndex(state)];
    }

    @Override
    @NotNull
    public VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter worldIn, @NotNull BlockPos pos, @NotNull CollisionContext context) {
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
    @NotNull
    public BlockState updateShape(@NotNull BlockState state, @NotNull LevelReader level, @NotNull ScheduledTickAccess scheduledTickAccess,
                                  @NotNull BlockPos pos, @NotNull Direction direction, @NotNull BlockPos neighborPos, @NotNull BlockState neighborState,
                                  @NotNull RandomSource random) {
        if (state.getValue(BlockStateProperties.WATERLOGGED)) {
            scheduledTickAccess.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        
        // Determine the block's connections when one of its neighbors is updated
        return this.getCurrentState(state, level, pos);
    }
    
    protected BlockState getCurrentState(BlockState state, LevelReader world, BlockPos pos) {
        return this.defaultBlockState()
                .setValue(UP, this.getSideConnection(state, world, pos, Direction.UP))
                .setValue(DOWN, this.getSideConnection(state, world, pos, Direction.DOWN))
                .setValue(NORTH, this.getSideConnection(state, world, pos, Direction.NORTH))
                .setValue(SOUTH, this.getSideConnection(state, world, pos, Direction.SOUTH))
                .setValue(WEST, this.getSideConnection(state, world, pos, Direction.WEST))
                .setValue(EAST, this.getSideConnection(state, world, pos, Direction.EAST))
                .setValue(WATERLOGGED, world.getFluidState(pos).is(Fluids.WATER));
    }
    
    protected SkyglassPaneSide getSideConnection(BlockState state, LevelReader world, BlockPos pos, Direction dir) {
        BlockState adjacent = world.getBlockState(pos.relative(dir));
        if (isExceptionForConnection(adjacent)) {
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
    public boolean skipRendering(@NotNull BlockState state, @NotNull BlockState adjacentBlockState, @NotNull Direction side) {
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
    @NotNull
    public FluidState getFluidState(@NotNull BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }
    
    @Override
    public boolean isPathfindable(@NotNull BlockState state, @NotNull PathComputationType type) {
        return false;
    }
    
    @Override
    public boolean propagatesSkylightDown(BlockState state) {
        return !state.getValue(WATERLOGGED);
    }
    
    @Override
    @NotNull
    public BlockState rotate(@NotNull BlockState state, @NotNull Rotation rot) {
        return switch (rot) {
            case CLOCKWISE_180 ->
                    state.setValue(NORTH, state.getValue(SOUTH)).setValue(EAST, state.getValue(WEST)).setValue(SOUTH, state.getValue(NORTH)).setValue(WEST, state.getValue(EAST));
            case CLOCKWISE_90 ->
                    state.setValue(NORTH, state.getValue(WEST)).setValue(EAST, state.getValue(NORTH)).setValue(SOUTH, state.getValue(EAST)).setValue(WEST, state.getValue(SOUTH));
            case COUNTERCLOCKWISE_90 ->
                    state.setValue(NORTH, state.getValue(EAST)).setValue(EAST, state.getValue(SOUTH)).setValue(SOUTH, state.getValue(WEST)).setValue(WEST, state.getValue(NORTH));
            default -> state;
        };
    }
    
    @Override
    @NotNull
    public BlockState mirror(@NotNull BlockState state, @NotNull Mirror mirrorIn) {
        return switch (mirrorIn) {
            case LEFT_RIGHT -> state.setValue(NORTH, state.getValue(SOUTH)).setValue(SOUTH, state.getValue(NORTH));
            case FRONT_BACK -> state.setValue(EAST, state.getValue(WEST)).setValue(WEST, state.getValue(EAST));
            default -> state;
        };
    }
}
