package com.verdantartifice.primalmagick.common.tiles.mana;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class AutoChargerTileEntityNeoforge extends AutoChargerTileEntity{
    public AutoChargerTileEntityNeoforge(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        this.doInventorySync();
        if (this.getLevel() != null) {
            this.loadManaNetwork(this.getLevel());
        }
    }
}
