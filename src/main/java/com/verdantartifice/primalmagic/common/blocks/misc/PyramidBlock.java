package com.verdantartifice.primalmagic.common.blocks.misc;

import java.util.Map;

import com.google.common.collect.Maps;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.util.VoxelShapeUtils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

public class PyramidBlock extends Block {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    
    protected static final VoxelShape BASE_SHAPE = VoxelShapeUtils.fromModel(new ResourceLocation(PrimalMagic.MODID, "block/pyramid"));
    protected static final Map<Direction, VoxelShape> SHAPES = Util.make(Maps.newEnumMap(Direction.class), (map) -> {
        map.put(Direction.NORTH, BASE_SHAPE);
        map.put(Direction.SOUTH, VoxelShapeUtils.rotate(BASE_SHAPE, Direction.Axis.Y, Rotation.CLOCKWISE_180));
        map.put(Direction.WEST, VoxelShapeUtils.rotate(BASE_SHAPE, Direction.Axis.Y, Rotation.CLOCKWISE_90));
        map.put(Direction.EAST, VoxelShapeUtils.rotate(BASE_SHAPE, Direction.Axis.Y, Rotation.COUNTERCLOCKWISE_90));
        map.put(Direction.UP, VoxelShapeUtils.rotate(BASE_SHAPE, Direction.Axis.X, Rotation.CLOCKWISE_90));
        map.put(Direction.DOWN, VoxelShapeUtils.rotate(BASE_SHAPE, Direction.Axis.X, Rotation.COUNTERCLOCKWISE_90));
    });
    
    public PyramidBlock() {
        super(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.5F).sound(SoundType.STONE));
        this.setDefaultState(this.getDefaultState().with(FACING, Direction.NORTH));
    }
    
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        if (state.has(FACING)) {
            return SHAPES.get(state.get(FACING));
        } else {
            return BASE_SHAPE;
        }
    }
    
    @Override
    protected void fillStateContainer(Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(FACING);
    }
    
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        // Make the block face the player when placed
        return this.getDefaultState().with(FACING, context.getNearestLookingDirection().getOpposite());
    }
    
    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.with(FACING, rot.rotate(state.get(FACING)));
    }
    
    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.toRotation(state.get(FACING)));
    }
}
