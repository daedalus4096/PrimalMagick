package com.verdantartifice.primalmagic.common.blocks.mana;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.blocks.base.BlockTilePM;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.tiles.mana.AncientManaFontTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class AncientManaFontBlock extends BlockTilePM<AncientManaFontTileEntity> {
    protected static final VoxelShape PART_LOWER = Block.makeCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 2.0D, 14.0D);
    protected static final VoxelShape PART_UPPER = Block.makeCuboidShape(2.0D, 14.0D, 2.0D, 14.0D, 16.0D, 14.0D);
    protected static final VoxelShape SHAPE = VoxelShapes.or(PART_LOWER, PART_UPPER);
    
    protected Source source;
    
    public AncientManaFontBlock(Source source) {
        super("ancient_font_" + source.getTag(), AncientManaFontTileEntity.class, Block.Properties.create(Material.ROCK).hardnessAndResistance(-1.0F, 3600000.0F).sound(SoundType.STONE).lightValue(15).noDrops());
        this.source = source;
    }
    
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }
    
    public Source getSource() {
        return this.source;
    }
    
    @Override
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (worldIn.isRemote && worldIn.getTileEntity(pos) instanceof AncientManaFontTileEntity) {
            AncientManaFontTileEntity tile = (AncientManaFontTileEntity)worldIn.getTileEntity(pos);
            PrimalMagic.LOGGER.info("Mana: {} / {}", tile.getMana(), tile.getManaCapacity());
        }
        return true;
    }
}
