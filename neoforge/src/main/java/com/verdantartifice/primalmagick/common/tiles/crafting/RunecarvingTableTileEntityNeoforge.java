package com.verdantartifice.primalmagick.common.tiles.crafting;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class RunecarvingTableTileEntityNeoforge extends RunecarvingTableTileEntity {
    public RunecarvingTableTileEntityNeoforge(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        this.doInventorySync();
    }
}
