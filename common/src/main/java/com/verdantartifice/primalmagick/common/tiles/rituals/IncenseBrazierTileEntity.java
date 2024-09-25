package com.verdantartifice.primalmagick.common.tiles.rituals;

import com.verdantartifice.primalmagick.common.tiles.TileEntityTypesPM;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Definition of an incense brazier tile entity.
 * 
 * @author Daedalus4096
 */
public class IncenseBrazierTileEntity extends AbstractRitualPropTileEntity {
    public IncenseBrazierTileEntity(BlockPos pos, BlockState state) {
        super(TileEntityTypesPM.INCENSE_BRAZIER.get(), pos, state);
    }
}
