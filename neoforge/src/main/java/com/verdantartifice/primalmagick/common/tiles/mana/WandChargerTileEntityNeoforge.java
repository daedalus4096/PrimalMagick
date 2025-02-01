package com.verdantartifice.primalmagick.common.tiles.mana;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class WandChargerTileEntityNeoforge extends WandChargerTileEntity {
    public WandChargerTileEntityNeoforge(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        this.doInventorySync();
    }
}
