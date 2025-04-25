package com.verdantartifice.primalmagick.common.fluids;

import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.Objects;

public class FluidStackPMForge implements IFluidStackPM {
    public static final FluidStackPMForge EMPTY = new FluidStackPMForge(FluidStack.EMPTY);

    private final FluidStack innerStack;

    public FluidStackPMForge(final Fluid fluid, final int amount) {
        this.innerStack = new FluidStack(fluid, amount);
    }

    public FluidStackPMForge(final FluidStack innerStack) {
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
        return this.getFluid() == fluid;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof FluidStackPMForge that)) return false;
        return Objects.equals(innerStack, that.innerStack);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(innerStack);
    }
}
