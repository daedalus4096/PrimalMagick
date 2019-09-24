package com.verdantartifice.primalmagic.common.blocks.mana;

import com.verdantartifice.primalmagic.common.blocks.base.BlockPM;
import com.verdantartifice.primalmagic.common.sources.Source;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

public class AncientManaFont extends BlockPM {
    protected static final VoxelShape PART_LOWER = Block.makeCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 2.0D, 14.0D);
    protected static final VoxelShape PART_UPPER = Block.makeCuboidShape(2.0D, 14.0D, 2.0D, 14.0D, 16.0D, 14.0D);
    protected static final VoxelShape SHAPE = VoxelShapes.or(PART_LOWER, PART_UPPER);
    
    public AncientManaFont(Source source) {
        super("ancient_font_" + source.getTag(), Block.Properties.create(Material.ROCK).hardnessAndResistance(-1.0F, 3600000.0F).sound(SoundType.STONE).noDrops());
    }
    
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }
}
