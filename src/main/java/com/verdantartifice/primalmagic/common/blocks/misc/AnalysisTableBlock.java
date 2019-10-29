package com.verdantartifice.primalmagic.common.blocks.misc;

import com.verdantartifice.primalmagic.PrimalMagic;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

public class AnalysisTableBlock extends Block {
    protected static final VoxelShape PART_TOP = Block.makeCuboidShape(0.0D, 12.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape PART_BASE = Block.makeCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 2.0D, 13.0D);
    protected static final VoxelShape PART_STAND = Block.makeCuboidShape(6.0D, 2.0D, 6.0D, 10.0D, 12.0D, 10.0D);
    protected static final VoxelShape SHAPE = VoxelShapes.or(PART_TOP, PART_BASE, PART_STAND);

    public AnalysisTableBlock() {
        super(Block.Properties.create(Material.WOOD).hardnessAndResistance(1.5F, 6.0F).sound(SoundType.WOOD));
        this.setRegistryName(PrimalMagic.MODID, "analysis_table");
    }
    
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }
}
