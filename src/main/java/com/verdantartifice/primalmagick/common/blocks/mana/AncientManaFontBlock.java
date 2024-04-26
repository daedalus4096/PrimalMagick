package com.verdantartifice.primalmagick.common.blocks.mana;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.misc.DeviceTier;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.tiles.TileEntityTypesPM;
import com.verdantartifice.primalmagick.common.tiles.mana.AncientManaFontTileEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Block definition for an ancient mana font.  Ancient mana fonts are found in shrines placed into the
 * world at generation time.
 * 
 * @author Daedalus4096
 */
public class AncientManaFontBlock extends AbstractManaFontBlock {
    public static final MapCodec<AncientManaFontBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Source.CODEC.fieldOf("source").forGetter(b -> b.source),
            propertiesCodec()
    ).apply(instance, AncientManaFontBlock::new));
    
    public AncientManaFontBlock(Source source, Block.Properties properties) {
        super(source, DeviceTier.BASIC, properties);
    }
    
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AncientManaFontTileEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, TileEntityTypesPM.ANCIENT_MANA_FONT.get(), AncientManaFontTileEntity::tick);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}
