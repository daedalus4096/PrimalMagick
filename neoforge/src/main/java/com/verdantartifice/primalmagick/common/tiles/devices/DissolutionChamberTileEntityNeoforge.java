package com.verdantartifice.primalmagick.common.tiles.devices;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class DissolutionChamberTileEntityNeoforge extends DissolutionChamberTileEntity {
    public DissolutionChamberTileEntityNeoforge(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        this.processTimeTotal = this.getProcessTimeTotal();
    }
}
