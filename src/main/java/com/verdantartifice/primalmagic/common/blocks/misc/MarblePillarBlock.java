package com.verdantartifice.primalmagic.common.blocks.misc;

import com.verdantartifice.primalmagic.PrimalMagic;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

public class MarblePillarBlock extends Block {
    protected static final VoxelShape PART_CENTRAL = Block.makeCuboidShape(2, 0, 2, 14, 16, 14);
    protected static final VoxelShape SHAPE_BASE = PART_CENTRAL;
    
    public MarblePillarBlock() {
        super(Block.Properties.create(Material.ROCK, MaterialColor.QUARTZ).hardnessAndResistance(1.5F, 6.0F).sound(SoundType.STONE));
        this.setRegistryName(PrimalMagic.MODID, "marble_pillar");
    }
    
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE_BASE;
    }
}
