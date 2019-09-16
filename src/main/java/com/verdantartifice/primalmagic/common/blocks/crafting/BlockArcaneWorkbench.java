package com.verdantartifice.primalmagic.common.blocks.crafting;

import com.verdantartifice.primalmagic.common.blocks.base.BlockPM;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

public class BlockArcaneWorkbench extends BlockPM {
    protected static final VoxelShape PART_UPPER = Block.makeCuboidShape(0.0D, 8.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape PART_POST1 = Block.makeCuboidShape(11.0D, 4.0D, 1.0D, 15.0D, 8.0D, 5.0D);
    protected static final VoxelShape PART_POST2 = Block.makeCuboidShape(11.0D, 4.0D, 11.0D, 15.0D, 8.0D, 15.0D);
    protected static final VoxelShape PART_POST3 = Block.makeCuboidShape(1.0D, 4.0D, 11.0D, 5.0D, 8.0D, 15.0D);
    protected static final VoxelShape PART_POST4 = Block.makeCuboidShape(1.0D, 4.0D, 1.0D, 5.0D, 8.0D, 5.0D);
    protected static final VoxelShape PART_LOWER = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D);
    protected static final VoxelShape SHAPE = VoxelShapes.or(PART_UPPER, PART_POST1, PART_POST2, PART_POST3, PART_POST4, PART_LOWER);

    public BlockArcaneWorkbench() {
        super("arcane_workbench", Block.Properties.create(Material.WOOD).hardnessAndResistance(1.5F, 6.0F));
    }
    
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }
}
