package com.verdantartifice.primalmagick.common.fluids;

import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.Objects;

public class FluidStackPMNeoforge implements IFluidStackPM {
    public static final FluidStackPMNeoforge EMPTY = new FluidStackPMNeoforge(FluidStack.EMPTY);

    private final FluidStack innerStack;

    public FluidStackPMNeoforge(final Fluid fluid, final int amount) {
        this.innerStack = new FluidStack(fluid, amount);
    }

    public FluidStackPMNeoforge(final FluidStack innerStack) {
        this.innerStack = innerStack;
    }

    public FluidStack getInner() {
        return this.innerStack;
    }

    @Override
    public Fluid getFluid() {
        return this.innerStack.getFluid();
    }

    @Override
    public int getAmount() {
        return this.innerStack.getAmount();
    }

    @Override
    public boolean isEmpty() {
        return this.innerStack.isEmpty();
    }

    @Override
    public boolean is(Fluid fluid) {
        return this.innerStack.is(fluid);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof FluidStackPMNeoforge that)) return false;
        return Objects.equals(innerStack, that.innerStack);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(innerStack);
    }
}
