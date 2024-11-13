package com.verdantartifice.primalmagick.common.tiles.devices;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class InfernalFurnaceTileEntityNeoforge extends InfernalFurnaceTileEntity {
    public InfernalFurnaceTileEntityNeoforge(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    protected boolean hasFuelCraftRemaining(ItemStack stack) {
        return stack.hasCraftingRemainingItem();
    }

    @Override
    protected ItemStack getFuelCraftRemaining(ItemStack stack) {
        return stack.getCraftingRemainingItem();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        this.processTimeTotal = getTotalCookTime(this.level, this, DEFAULT_COOK_TIME);
    }
}
