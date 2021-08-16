package com.verdantartifice.primalmagic.common.blocks.mana;

import com.verdantartifice.primalmagic.common.misc.DeviceTier;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.tiles.TileEntityTypesPM;
import com.verdantartifice.primalmagic.common.tiles.mana.ArtificialManaFontTileEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Block definition for an artificial mana font.  Artificial mana fonts may be constructed by players
 * and contain sources other than the terrestrial five.
 * 
 * @author Daedalus4096
 */
public class ArtificialManaFontBlock extends AbstractManaFontBlock {
    public ArtificialManaFontBlock(Source source, DeviceTier tier, Block.Properties properties) {
        super(source, tier, properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ArtificialManaFontTileEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, TileEntityTypesPM.ARTIFICIAL_MANA_FONT.get(), ArtificialManaFontTileEntity::tick);
    }
}
