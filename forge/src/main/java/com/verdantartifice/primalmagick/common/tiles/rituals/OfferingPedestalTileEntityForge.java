package com.verdantartifice.primalmagick.common.tiles.rituals;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class OfferingPedestalTileEntityForge extends OfferingPedestalTileEntity {
    public OfferingPedestalTileEntityForge(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    public void onLoad() {
        this.unpackLootTable(null);
        super.onLoad();
    }
}
