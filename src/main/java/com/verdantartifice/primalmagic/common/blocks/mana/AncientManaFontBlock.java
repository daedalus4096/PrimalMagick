package com.verdantartifice.primalmagic.common.blocks.mana;

import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.tiles.mana.AncientManaFontTileEntity;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;

/**
 * Block definition for the ancient mana font.  Ancient mana fonts are found in shrines placed into the
 * world at generation time, and may be drained with a wand to power it with mana.
 * 
 * @author Daedalus4096
 */
public class AncientManaFontBlock extends Block {
    protected static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D);

    protected Source source;
    
    public AncientManaFontBlock(Source source) {
        super(Block.Properties.of(Material.STONE).strength(-1.0F, 3600000.0F).sound(SoundType.STONE).lightLevel((state) -> { return 15; }).noDrops());
        this.source = source;
    }
    
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
    
    public Source getSource() {
        return this.source;
    }
    
    @Override
    public BlockEntity createTileEntity(BlockState state, BlockGetter world) {
        return new AncientManaFontTileEntity();
    }
    
    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public boolean triggerEvent(BlockState state, Level worldIn, BlockPos pos, int id, int param) {
        // Pass any received events on to the tile entity and let it decide what to do with it
        super.triggerEvent(state, worldIn, pos, id, param);
        BlockEntity tile = worldIn.getBlockEntity(pos);
        return (tile == null) ? false : tile.triggerEvent(id, param);
    }
}
