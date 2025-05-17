package com.verdantartifice.primalmagick.common.tiles.devices;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class DesalinatorTileEntityNeoforge extends DesalinatorTileEntity {
    public DesalinatorTileEntityNeoforge(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        this.doInventorySync();
        this.boilTimeTotal = this.getBoilTimeTotal();
    }
}
