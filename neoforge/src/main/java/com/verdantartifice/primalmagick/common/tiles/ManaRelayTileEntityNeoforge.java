package com.verdantartifice.primalmagick.common.tiles;

import com.verdantartifice.primalmagick.common.tiles.mana.ManaRelayTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class ManaRelayTileEntityNeoforge extends ManaRelayTileEntity {
    public ManaRelayTileEntityNeoforge(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (this.getLevel() != null) {
            this.loadManaNetwork(this.getLevel());
        }
    }
}
