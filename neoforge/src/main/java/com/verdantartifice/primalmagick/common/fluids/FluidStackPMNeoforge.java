package com.verdantartifice.primalmagick.common.fluids;

import net.minecraft.core.Holder;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;

public class FluidStackPMNeoforge implements IFluidStackPM {
    private final FluidStack innerStack;

    public FluidStackPMNeoforge(final Holder<Fluid> fluidHolder, final int amount) {
        this.innerStack = new FluidStack(fluidHolder, amount);
    }

    @Override
    public Fluid getFluid() {
        return this.innerStack.getFluid();
    }

    @Override
    public int getAmount() {
        return this.innerStack.getAmount();
    }
}
