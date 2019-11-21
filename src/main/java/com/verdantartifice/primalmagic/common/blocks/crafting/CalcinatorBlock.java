package com.verdantartifice.primalmagic.common.blocks.crafting;

import com.verdantartifice.primalmagic.PrimalMagic;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

public class CalcinatorBlock extends Block {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    
    protected static final VoxelShape PART_LEG1 = Block.makeCuboidShape(2, 0, 2, 5, 2, 5);
    protected static final VoxelShape PART_LEG2 = Block.makeCuboidShape(11, 0, 2, 14, 2, 5);
    protected static final VoxelShape PART_LEG3 = Block.makeCuboidShape(11, 0, 11, 14, 2, 14);
    protected static final VoxelShape PART_LEG4 = Block.makeCuboidShape(2, 0, 11, 5, 2, 14);
    protected static final VoxelShape PART_BODY = Block.makeCuboidShape(2, 2, 2, 14, 11, 14);
    protected static final VoxelShape PART_TOP1 = Block.makeCuboidShape(3, 11, 3, 13, 12, 13);
    protected static final VoxelShape PART_TOP2 = Block.makeCuboidShape(4, 12, 4, 12, 13, 12);
    protected static final VoxelShape PART_STACK = Block.makeCuboidShape(6, 13, 6, 10, 16, 10);
    protected static final VoxelShape SHAPE = VoxelShapes.or(PART_LEG1, PART_LEG2, PART_LEG3, PART_LEG4, PART_BODY, PART_TOP1, PART_TOP2, PART_STACK);
    
    public CalcinatorBlock() {
        super(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.5F).lightValue(13).sound(SoundType.STONE));
        this.setRegistryName(PrimalMagic.MODID, "calcinator");
        this.setDefaultState(this.getDefaultState().with(FACING, Direction.NORTH).with(LIT, Boolean.valueOf(false)));
    }
    
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }
    
    @Override
    protected void fillStateContainer(Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(FACING, LIT);
    }
    
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
    }
    
    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.with(FACING, rot.rotate(state.get(FACING)));
    }
    
    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.toRotation(state.get(FACING)));
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public int getLightValue(BlockState state) {
        return state.get(LIT) ? super.getLightValue(state) : 0;
    }
}
