package com.verdantartifice.primalmagick.common.tiles.devices;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class ScribeTableTileEntityNeoforge extends ScribeTableTileEntity {
    public ScribeTableTileEntityNeoforge(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        this.doInventorySync();
    }

    @Override
    public boolean shouldTriggerClientSideContainerClosingOnOpen() {
        // Prevent the mouse cursor from re-centering when switching table modes
        return false;
    }
}
