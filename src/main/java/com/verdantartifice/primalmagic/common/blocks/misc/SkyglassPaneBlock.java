package com.verdantartifice.primalmagic.common.blocks.misc;

import java.util.Map;

import com.google.common.collect.Maps;
import com.verdantartifice.primalmagic.common.blockstates.properties.SkyglassPaneSide;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Block definition for a skyglass pane.  Skyglass is completely transparent except at the edge,
 * connecting to other adjacent skyglass blocks.  It also is not destroyed upon harvesting.
 * 
 * @author Daedalus4096
 * @see {@link net.minecraft.block.PaneBlock}
 */
public class SkyglassPaneBlock extends Block implements IWaterLoggable {
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
        this.setDefaultState(this.getDefaultState().with(NORTH, SkyglassPaneSide.NONE).with(EAST, SkyglassPaneSide.NONE).with(SOUTH, SkyglassPaneSide.NONE).with(WEST, SkyglassPaneSide.NONE).with(UP, SkyglassPaneSide.NONE).with(DOWN, SkyglassPaneSide.NONE).with(WATERLOGGED, Boolean.valueOf(false)));
    }
    
    protected VoxelShape[] makeShapes(float nodeWidth, float extensionWidth, float p_196408_3_, float p_196408_4_, float p_196408_5_) {
        float f = 8.0F - nodeWidth;
        float f1 = 8.0F + nodeWidth;
        float f2 = 8.0F - extensionWidth;
        float f3 = 8.0F + extensionWidth;
        VoxelShape voxelshape = Block.makeCuboidShape((double)f, 0.0D, (double)f, (double)f1, (double)p_196408_3_, (double)f1);
        VoxelShape voxelshape1 = Block.makeCuboidShape((double)f2, (double)p_196408_4_, 0.0D, (double)f3, (double)p_196408_5_, (double)f3);
        VoxelShape voxelshape2 = Block.makeCuboidShape((double)f2, (double)p_196408_4_, (double)f2, (double)f3, (double)p_196408_5_, 16.0D);
        VoxelShape voxelshape3 = Block.makeCuboidShape(0.0D, (double)p_196408_4_, (double)f2, (double)f3, (double)p_196408_5_, (double)f3);
        VoxelShape voxelshape4 = Block.makeCuboidShape((double)f2, (double)p_196408_4_, (double)f2, 16.0D, (double)p_196408_5_, (double)f3);
        VoxelShape voxelshape5 = VoxelShapes.or(voxelshape1, voxelshape4);
        VoxelShape voxelshape6 = VoxelShapes.or(voxelshape2, voxelshape3);
        VoxelShape[] avoxelshape = new VoxelShape[]{VoxelShapes.empty(), voxelshape2, voxelshape3, voxelshape6, voxelshape1, VoxelShapes.or(voxelshape2, voxelshape1), VoxelShapes.or(voxelshape3, voxelshape1), VoxelShapes.or(voxelshape6, voxelshape1), voxelshape4, VoxelShapes.or(voxelshape2, voxelshape4), VoxelShapes.or(voxelshape3, voxelshape4), VoxelShapes.or(voxelshape6, voxelshape4), voxelshape5, VoxelShapes.or(voxelshape2, voxelshape5), VoxelShapes.or(voxelshape3, voxelshape5), VoxelShapes.or(voxelshape6, voxelshape5)};

        for (int i = 0; i < avoxelshape.length; i++) {
            avoxelshape[i] = VoxelShapes.or(voxelshape, avoxelshape[i]);
        }
        
        return avoxelshape;
    }
    
    @Override
    protected void fillStateContainer(Builder<Block, BlockState> builder) {
        builder.add(UP, DOWN, NORTH, EAST, WEST, SOUTH, WATERLOGGED);
    }
    
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return this.shapes[this.getShapeIndex(state)];
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return this.collisionShapes[this.getShapeIndex(state)];
    }
    
    protected int getShapeIndex(BlockState state) {
        return this.stateShapeIndices.computeIntIfAbsent(state, (s) -> {
            int index = 0;
            if (s.get(NORTH) == SkyglassPaneSide.GLASS || s.get(NORTH) == SkyglassPaneSide.OTHER) {
                index |= getDirectionMask(Direction.NORTH);
            }
            if (s.get(SOUTH) == SkyglassPaneSide.GLASS || s.get(SOUTH) == SkyglassPaneSide.OTHER) {
                index |= getDirectionMask(Direction.SOUTH);
            }
            if (s.get(WEST) == SkyglassPaneSide.GLASS || s.get(WEST) == SkyglassPaneSide.OTHER) {
                index |= getDirectionMask(Direction.WEST);
            }
            if (s.get(EAST) == SkyglassPaneSide.GLASS || s.get(EAST) == SkyglassPaneSide.OTHER) {
                index |= getDirectionMask(Direction.EAST);
            }
            return index;
        });
    }
    
    protected static int getDirectionMask(Direction dir) {
        return 1 << dir.getHorizontalIndex();
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        // Determine the block's connections when it is placed into the world
        return this.getCurrentState(this.getDefaultState(), context.getWorld(), context.getPos());
    }
    
    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (stateIn.get(WATERLOGGED)) {
            worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
        }
        
        // Determine the block's connections when one of its neighbors is updated
        return this.getCurrentState(stateIn, worldIn, currentPos);
    }
    
    protected BlockState getCurrentState(BlockState state, IWorld world, BlockPos pos) {
        return this.getDefaultState()
                .with(UP, this.getSideConnection(state, world, pos, Direction.UP))
                .with(DOWN, this.getSideConnection(state, world, pos, Direction.DOWN))
                .with(NORTH, this.getSideConnection(state, world, pos, Direction.NORTH))
                .with(SOUTH, this.getSideConnection(state, world, pos, Direction.SOUTH))
                .with(WEST, this.getSideConnection(state, world, pos, Direction.WEST))
                .with(EAST, this.getSideConnection(state, world, pos, Direction.EAST))
                .with(WATERLOGGED, world.getFluidState(pos).getFluid() == Fluids.WATER);
    }
    
    protected SkyglassPaneSide getSideConnection(BlockState state, IWorld world, BlockPos pos, Direction dir) {
        BlockState adjacent = world.getBlockState(pos.offset(dir));
        if (adjacent == null || cannotAttach(adjacent.getBlock())) {
            return SkyglassPaneSide.NONE;
        } else if (state.getBlock() == adjacent.getBlock()) {
            return SkyglassPaneSide.GLASS;
        } else if (adjacent.isSolidSide(world, pos.offset(dir), dir.getOpposite())) {
            return SkyglassPaneSide.OTHER;
        } else {
            return SkyglassPaneSide.NONE;
        }
    }
    
    @SuppressWarnings("deprecation")
    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isSideInvisible(BlockState state, BlockState adjacentBlockState, Direction side) {
        if (adjacentBlockState.getBlock() == state.getBlock()) {
            if (!side.getAxis().isHorizontal()) {
                return true;
            }
            if ( state.get(FACING_TO_PROPERTY_MAP.get(side)) == SkyglassPaneSide.GLASS &&
                 adjacentBlockState.get(FACING_TO_PROPERTY_MAP.get(side.getOpposite())) == SkyglassPaneSide.GLASS ) {
                return true;
            }
        }
        return super.isSideInvisible(state, adjacentBlockState, side);
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public IFluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }
    
    @Override
    public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
        return false;
    }
    
    @Override
    public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
        return !state.get(WATERLOGGED);
    }
    
    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        switch (rot) {
        case CLOCKWISE_180:
            return state.with(NORTH, state.get(SOUTH)).with(EAST, state.get(WEST)).with(SOUTH, state.get(NORTH)).with(WEST, state.get(EAST));
        case CLOCKWISE_90:
            return state.with(NORTH, state.get(WEST)).with(EAST, state.get(NORTH)).with(SOUTH, state.get(EAST)).with(WEST, state.get(SOUTH));
        case COUNTERCLOCKWISE_90:
            return state.with(NORTH, state.get(EAST)).with(EAST, state.get(SOUTH)).with(SOUTH, state.get(WEST)).with(WEST, state.get(NORTH));
        default:
            return state;
        }
    }
    
    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        switch (mirrorIn) {
        case LEFT_RIGHT:
            return state.with(NORTH, state.get(SOUTH)).with(SOUTH, state.get(NORTH));
        case FRONT_BACK:
            return state.with(EAST, state.get(WEST)).with(WEST, state.get(EAST));
        default:
            return state;
        }
    }
}
