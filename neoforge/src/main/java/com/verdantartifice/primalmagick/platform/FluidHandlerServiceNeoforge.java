package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.common.capabilities.FluidHandlerPMNeoforge;
import com.verdantartifice.primalmagick.common.capabilities.IFluidHandlerPM;
import com.verdantartifice.primalmagick.common.fluids.IFluidStackPM;
import com.verdantartifice.primalmagick.platform.services.IFluidHandlerService;

import java.util.function.Predicate;

public class FluidHandlerServiceNeoforge implements IFluidHandlerService {
    @Override
    public IFluidHandlerPM create(int tanks, int capacity) {
        return new FluidHandlerPMNeoforge(tanks, capacity);
    }

    @Override
    public IFluidHandlerPM create(int tanks, int capacity, Predicate<IFluidStackPM> validator) {
        return new FluidHandlerPMNeoforge(tanks, capacity, validator);
    }
}
