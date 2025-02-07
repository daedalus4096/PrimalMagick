package com.verdantartifice.primalmagick.common.tiles.rituals;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class OfferingPedestalTileEntityNeoforge extends OfferingPedestalTileEntity {
    public OfferingPedestalTileEntityNeoforge(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    public void onLoad() {
        this.unpackLootTable(null);
        super.onLoad();
        this.doInventorySync();
    }
}
