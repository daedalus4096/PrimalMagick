package com.verdantartifice.primalmagick.common.tiles.devices;

import com.verdantartifice.primalmagick.common.capabilities.CapabilitiesForge;
import com.verdantartifice.primalmagick.common.capabilities.IManaStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public class InfernalFurnaceTileEntityForge extends InfernalFurnaceTileEntity {
    protected LazyOptional<IManaStorage<?>> manaStorageOpt = LazyOptional.of(() -> this.manaStorage);

    public InfernalFurnaceTileEntityForge(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    // FIXME Refactor to use item service in base class
    @Override
    protected boolean hasFuelCraftRemaining(ItemStack stack) {
        return stack.hasCraftingRemainingItem();
    }

    // FIXME Refactor to use item service in base class
    @Override
    protected ItemStack getFuelCraftRemaining(ItemStack stack) {
        return stack.getCraftingRemainingItem();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        this.processTimeTotal = getTotalCookTime(this.level, this, DEFAULT_COOK_TIME);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (!this.remove && cap == CapabilitiesForge.MANA_STORAGE) {
            return this.manaStorageOpt.cast();
        } else {
            return super.getCapability(cap, side);
        }
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.manaStorageOpt.invalidate();
    }
}
