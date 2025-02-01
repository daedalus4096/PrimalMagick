package com.verdantartifice.primalmagick.common.tiles.devices;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class SanguineCrucibleTileEntityNeoforge extends SanguineCrucibleTileEntity {
    public SanguineCrucibleTileEntityNeoforge(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        this.doInventorySync();
    }
}
