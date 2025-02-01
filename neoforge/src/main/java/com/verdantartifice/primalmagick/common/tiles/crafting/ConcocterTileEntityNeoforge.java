package com.verdantartifice.primalmagick.common.tiles.crafting;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class ConcocterTileEntityNeoforge extends ConcocterTileEntity {
    public ConcocterTileEntityNeoforge(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        this.doInventorySync();
        if (!this.level.isClientSide) {
            this.relevantResearch = assembleRelevantResearch(this.level.getRecipeManager());
        }
        this.cookTimeTotal = this.getCookTimeTotal();
    }
}
