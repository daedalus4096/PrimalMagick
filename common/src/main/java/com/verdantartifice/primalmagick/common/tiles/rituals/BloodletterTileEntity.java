package com.verdantartifice.primalmagick.common.tiles.rituals;

import com.verdantartifice.primalmagick.common.tiles.BlockEntityTypesPM;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Definition of a bloodletter tile entity.
 * 
 * @author Daedalus4096
 */
public class BloodletterTileEntity extends AbstractRitualPropTileEntity {
    public BloodletterTileEntity(BlockPos pos, BlockState state) {
        super(BlockEntityTypesPM.BLOODLETTER.get(), pos, state);
    }
}
