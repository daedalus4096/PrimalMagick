package com.verdantartifice.primalmagick.common.tiles.crafting;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class ConcocterTileEntityForge extends ConcocterTileEntity {
    public ConcocterTileEntityForge(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (!this.level.isClientSide) {
            this.relevantResearch = assembleRelevantResearch(this.level.getRecipeManager());
        }
        this.cookTimeTotal = this.getCookTimeTotal();
    }
}
