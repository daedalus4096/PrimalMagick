package com.verdantartifice.primalmagick.common.capabilities;

import com.verdantartifice.primalmagick.common.fluids.FluidStackPMForge;
import com.verdantartifice.primalmagick.common.fluids.IFluidStackPM;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import java.util.function.Predicate;

public class FluidHandlerPMForge implements IFluidHandlerPM {
    public static final IFluidHandlerPM EMPTY = new FluidHandlerPMForge(0, fs -> false);

    protected final FluidTank tank;

    public FluidHandlerPMForge(int capacity) {
        this(capacity, fs -> true);
    }

    public FluidHandlerPMForge(int capacity, Predicate<IFluidStackPM> validator) {
        this.tank = new FluidTank(capacity, fStack -> validator.test(new FluidStackPMForge(fStack)));
    }

    @Override
    public int getTanks() {
        return this.tank.getTanks();
    }

    @Override
    public IFluidStackPM getFluidInTank(int tank) {
        return new FluidStackPMForge(this.tank.getFluidInTank(tank));
    }

    @Override
    public int getTankCapacity(int tank) {
        return this.tank.getTankCapacity(tank);
    }

    @Override
    public boolean isFluidValid(int tank, IFluidStackPM stack) {
        return stack instanceof FluidStackPMForge fStack && this.tank.isFluidValid(tank, fStack.getInner());
    }

    @Override
    public int fill(IFluidStackPM stack, boolean simulate) {
        if (stack instanceof FluidStackPMForge fStack) {
            return this.tank.fill(fStack.getInner(), simulate ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE);
        } else {
            return 0;
        }
    }

    @Override
    public IFluidStackPM drain(IFluidStackPM stack, boolean simulate) {
        if (stack instanceof FluidStackPMForge fStack) {
            return new FluidStackPMForge(this.tank.drain(fStack.getInner(), simulate ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE));
        } else {
            return FluidStackPMForge.EMPTY;
        }
    }

    @Override
    public IFluidStackPM drain(int maxDrain, boolean simulate) {
        return new FluidStackPMForge(this.tank.drain(maxDrain, simulate ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE));
    }

    @Override
    public IFluidHandlerPM readFromNBT(HolderLookup.Provider lookupProvider, CompoundTag nbt) {
        this.tank.readFromNBT(nbt.getCompound("Tank"));
        return this;
    }

    @Override
    public CompoundTag writeToNBT(HolderLookup.Provider lookupProvider, CompoundTag nbt) {
        nbt.put("Tank", this.tank.writeToNBT(new CompoundTag()));
        return nbt;
    }
}
