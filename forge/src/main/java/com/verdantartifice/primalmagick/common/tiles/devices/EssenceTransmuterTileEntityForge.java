package com.verdantartifice.primalmagick.common.tiles.devices;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class EssenceTransmuterTileEntityForge extends EssenceTransmuterTileEntity {
    public EssenceTransmuterTileEntityForge(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (!this.level.isClientSide) {
            this.relevantResearch = assembleRelevantResearch();
        }
        this.processTimeTotal = this.getProcessTimeTotal();
    }
}
