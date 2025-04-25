package com.verdantartifice.primalmagick.common.capabilities;

import com.verdantartifice.primalmagick.common.fluids.FluidStackPMNeoforge;
import com.verdantartifice.primalmagick.common.fluids.IFluidStackPM;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import java.util.function.Predicate;

public class FluidHandlerPMNeoforge implements IFluidHandlerPM {
    public static final IFluidHandlerPM EMPTY = new FluidHandlerPMNeoforge(0, fs -> false);

    protected final FluidTank tank;

    public FluidHandlerPMNeoforge(int capacity) {
        this(capacity, fs -> true);
    }

    public FluidHandlerPMNeoforge(int capacity, Predicate<IFluidStackPM> validator) {
        this.tank = new FluidTank(capacity, nfStack -> validator.test(new FluidStackPMNeoforge(nfStack)));
    }

    @Override
    public int getTanks() {
        return this.tank.getTanks();
    }

    @Override
    public IFluidStackPM getFluidInTank(int tank) {
        return new FluidStackPMNeoforge(this.tank.getFluidInTank(tank));
    }

    @Override
    public int getTankCapacity(int tank) {
        return this.tank.getTankCapacity(tank);
    }

    @Override
    public boolean isFluidValid(int tank, IFluidStackPM stack) {
        return stack instanceof FluidStackPMNeoforge nfStack && this.tank.isFluidValid(tank, nfStack.getInner());
    }

    @Override
    public int fill(IFluidStackPM stack, boolean simulate) {
        if (stack instanceof FluidStackPMNeoforge nfStack) {
            return this.tank.fill(nfStack.getInner(), simulate ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE);
        } else {
            return 0;
        }
    }

    @Override
    public IFluidStackPM drain(IFluidStackPM stack, boolean simulate) {
        if (stack instanceof FluidStackPMNeoforge nfStack) {
            return new FluidStackPMNeoforge(this.tank.drain(nfStack.getInner(), simulate ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE));
        } else {
            return FluidStackPMNeoforge.EMPTY;
        }
    }

    @Override
    public IFluidStackPM drain(int maxDrain, boolean simulate) {
        return new FluidStackPMNeoforge(this.tank.drain(maxDrain, simulate ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE));
    }
}
