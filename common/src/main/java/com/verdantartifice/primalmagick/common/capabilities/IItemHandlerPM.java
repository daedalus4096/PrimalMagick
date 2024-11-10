package com.verdantartifice.primalmagick.common.capabilities;

import net.minecraft.world.item.ItemStack;

/**
 * Common interface mirroring the Forge and Neoforge item handler capability interfaces.
 * Provided here so that common code has a usable reference to capability functions.
 */
public interface IItemHandlerPM {
    int getSlots();

    ItemStack getStackInSlot(int slot);

    ItemStack insertItem(int slot, ItemStack stack, boolean simulate);

    ItemStack extractItem(int slot, int amount, boolean simulate);

    int getSlotLimit(int slot);

    boolean isItemValid(int slot, ItemStack stack);
}
