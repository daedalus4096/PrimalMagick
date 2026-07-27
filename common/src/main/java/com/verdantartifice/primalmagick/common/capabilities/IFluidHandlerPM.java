package com.verdantartifice.primalmagick.common.capabilities;

import com.verdantartifice.primalmagick.common.fluids.IFluidStackPM;
import com.verdantartifice.primalmagick.common.util.IValueIOSerializablePM;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;

/**
 * Common interface mirroring the Neoforge fluid handler capability interfaces.
 * Provided here so that common code has a usable reference to capability functions.
 */
public interface IFluidHandlerPM extends IValueIOSerializablePM {
    int getTanks();
    IFluidStackPM getFluidInTank(int tank);
    int getTankCapacity(int tank);
    boolean isFluidValid(int tank, IFluidStackPM stack);

    /**
     * @param stack
     * @param simulate
     * @return the amount accepted by the fluid handler, in millibuckets
     */
    int fill(IFluidStackPM stack, boolean simulate);

    /**
     * @param stack
     * @param simulate
     * @return the amount extracted from the fluid handler, in millibuckets
     */
    IFluidStackPM drain(IFluidStackPM stack, boolean simulate);

    /**
     * @param tank
     * @param maxDrain
     * @param simulate
     * @return the amount extracted from the fluid handler, in millibuckets
     */
    IFluidStackPM drain(int tank, int maxDrain, boolean simulate);
}
