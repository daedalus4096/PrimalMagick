package com.verdantartifice.primalmagick.common.tiles.crafting;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class AbstractCalcinatorTileEntityNeoforge extends AbstractCalcinatorTileEntity {
    public AbstractCalcinatorTileEntityNeoforge(BlockEntityType<? extends AbstractCalcinatorTileEntity> tileEntityType, BlockPos pos, BlockState state) {
        super(tileEntityType, pos, state);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (!this.level.isClientSide) {
            this.relevantResearch = assembleRelevantResearch();
        }
        this.cookTimeTotal = this.getCookTimeTotal();
    }

    @Override
    protected boolean hasFuelRemainingItem(ItemStack fuelStack) {
        return fuelStack.hasCraftingRemainingItem();
    }

    @Override
    protected ItemStack getFuelRemainingItem(ItemStack fuelStack) {
        return fuelStack.getCraftingRemainingItem();
    }
}
