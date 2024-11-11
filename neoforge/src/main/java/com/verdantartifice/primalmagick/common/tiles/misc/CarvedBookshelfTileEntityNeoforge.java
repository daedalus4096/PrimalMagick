package com.verdantartifice.primalmagick.common.tiles.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class CarvedBookshelfTileEntityNeoforge extends CarvedBookshelfTileEntity {
    public CarvedBookshelfTileEntityNeoforge(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    // TODO Implement item handler capability cache

    @Override
    public void onLoad() {
        super.onLoad();
        this.doInventorySync();
    }
}
