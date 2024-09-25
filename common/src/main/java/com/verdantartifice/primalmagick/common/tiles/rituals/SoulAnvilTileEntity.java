package com.verdantartifice.primalmagick.common.tiles.rituals;

import com.verdantartifice.primalmagick.common.tiles.TileEntityTypesPM;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Definition of a soul anvil tile entity.
 * 
 * @author Daedalus4096
 */
public class SoulAnvilTileEntity extends AbstractRitualPropTileEntity {
    public SoulAnvilTileEntity(BlockPos pos, BlockState state) {
        super(TileEntityTypesPM.SOUL_ANVIL.get(), pos, state);
    }
}
