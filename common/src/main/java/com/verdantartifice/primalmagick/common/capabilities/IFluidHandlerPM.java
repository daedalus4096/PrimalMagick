package com.verdantartifice.primalmagick.common.capabilities;

import com.verdantartifice.primalmagick.common.fluids.IFluidStackPM;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;

/**
 * Common interface mirroring the Forge and Neoforge fluid handler capability interfaces.
 * Provided here so that common code has a usable reference to capability functions.
 */
public interface IFluidHandlerPM {
    int getTanks();
    IFluidStackPM getFluidInTank(int tank);
    int getTankCapacity(int tank);
    boolean isFluidValid(int tank, IFluidStackPM stack);
    int fill(IFluidStackPM stack, boolean simulate);
    IFluidStackPM drain(IFluidStackPM stack, boolean simulate);
    IFluidStackPM drain(int maxDrain, boolean simulate);
    IFluidHandlerPM readFromNBT(HolderLookup.Provider lookupProvider, CompoundTag nbt);
    CompoundTag writeToNBT(HolderLookup.Provider lookupProvider, CompoundTag nbt);
}
