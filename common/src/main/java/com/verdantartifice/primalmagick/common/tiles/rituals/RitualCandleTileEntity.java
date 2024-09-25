package com.verdantartifice.primalmagick.common.tiles.rituals;

import com.verdantartifice.primalmagick.common.tiles.TileEntityTypesPM;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Definition of a ritual candle tile entity.
 * 
 * @author Daedalus4096
 */
public class RitualCandleTileEntity extends AbstractRitualPropTileEntity {
    public RitualCandleTileEntity(BlockPos pos, BlockState state) {
        super(TileEntityTypesPM.RITUAL_CANDLE.get(), pos, state);
    }
}
