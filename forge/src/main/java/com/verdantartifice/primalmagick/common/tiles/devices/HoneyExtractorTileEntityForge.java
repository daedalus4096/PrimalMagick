package com.verdantartifice.primalmagick.common.tiles.devices;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class HoneyExtractorTileEntityForge extends HoneyExtractorTileEntity {
    public HoneyExtractorTileEntityForge(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        this.spinTimeTotal = this.getSpinTimeTotal();
    }
}
