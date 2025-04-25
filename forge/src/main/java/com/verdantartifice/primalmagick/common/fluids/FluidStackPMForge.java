package com.verdantartifice.primalmagick.common.fluids;

import net.minecraft.core.Holder;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class FluidStackPMForge implements IFluidStackPM {
    private final FluidStack innerStack;

    public FluidStackPMForge(final Holder<Fluid> fluidHolder, final int amount) {
        this.innerStack = new FluidStack(fluidHolder.value(), amount);
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
