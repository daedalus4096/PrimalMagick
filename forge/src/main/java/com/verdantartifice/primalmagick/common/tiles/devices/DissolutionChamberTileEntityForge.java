package com.verdantartifice.primalmagick.common.tiles.devices;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class DissolutionChamberTileEntityForge extends DissolutionChamberTileEntity {
    public DissolutionChamberTileEntityForge(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        this.processTimeTotal = this.getProcessTimeTotal();
    }
}
