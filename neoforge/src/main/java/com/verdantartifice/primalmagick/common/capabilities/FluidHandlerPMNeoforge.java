package com.verdantartifice.primalmagick.common.capabilities;

import com.verdantartifice.primalmagick.common.fluids.FluidStackPMNeoforge;
import com.verdantartifice.primalmagick.common.fluids.IFluidStackPM;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Predicate;

public class FluidHandlerPMNeoforge extends FluidStacksResourceHandler implements IFluidHandlerPM {
    public static final IFluidHandlerPM EMPTY = new FluidHandlerPMNeoforge(0, 0, _ -> false);

    protected final Optional<Predicate<IFluidStackPM>> validatorOpt;

    public FluidHandlerPMNeoforge(int tanks, int capacity) {
        this(tanks, capacity, Optional.empty());
    }

    public FluidHandlerPMNeoforge(int tanks, int capacity, @NotNull Predicate<IFluidStackPM> validator) {
        this(tanks, capacity, Optional.of(validator));
    }

    private FluidHandlerPMNeoforge(int tanks, int capacity, @NotNull Optional<Predicate<IFluidStackPM>> validatorOpt) {
        super(tanks, capacity);
        this.validatorOpt = validatorOpt;
    }

    @Override
    public int getTanks() {
        return this.size();
    }

    @Override
    public IFluidStackPM getFluidInTank(int tank) {
        return new FluidStackPMNeoforge(FluidUtil.getStack(this, tank));
    }

    @Override
    public int getTankCapacity(int tank) {
        return this.getCapacity(tank, this.getResource(tank));
    }

    @Override
    public boolean isFluidValid(int tank, IFluidStackPM stack) {
        return this.isValid(tank, FluidResource.of(stack.getFluid()));
    }

    @Override
    public boolean isValid(int index, @NotNull FluidResource resource) {
        return this.validatorOpt.map(f -> f.test(new FluidStackPMNeoforge(resource, 1))).orElseGet(() -> this.isValid(index, resource));
    }

    @Override
    public int fill(IFluidStackPM stack, boolean simulate) {
        try (Transaction tx = Transaction.openRoot()) {
            int retVal = this.insert(FluidResource.of(stack.getFluid()), stack.getAmount(), tx);
            if (!simulate) {
                tx.commit();
            }
            return retVal;
        }
    }

    @Override
    public IFluidStackPM drain(IFluidStackPM stack, boolean simulate) {
        try (Transaction tx = Transaction.openRoot()) {
            FluidResource resource = FluidResource.of(stack.getFluid());
            int retVal = this.extract(resource, stack.getAmount(), tx);
            if (!simulate) {
                tx.commit();
            }
            return new FluidStackPMNeoforge(resource, retVal);
        }
    }

    @Override
    public IFluidStackPM drain(int tank, int maxDrain, boolean simulate) {
        try (Transaction tx = Transaction.openRoot()) {
            FluidResource resource = this.getResource(tank);
            int retVal = this.extract(tank, resource, maxDrain, tx);
            if (!simulate) {
                tx.commit();
            }
            return new FluidStackPMNeoforge(resource, retVal);
        }
    }
}
