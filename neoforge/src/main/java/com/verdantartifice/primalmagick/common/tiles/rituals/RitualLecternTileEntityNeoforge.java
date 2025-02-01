package com.verdantartifice.primalmagick.common.tiles.rituals;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class RitualLecternTileEntityNeoforge extends RitualLecternTileEntity {
    public RitualLecternTileEntityNeoforge(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        this.doInventorySync();
    }
}
