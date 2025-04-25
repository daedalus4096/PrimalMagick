package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.common.capabilities.FluidHandlerPMNeoforge;
import com.verdantartifice.primalmagick.common.capabilities.IFluidHandlerPM;
import com.verdantartifice.primalmagick.common.fluids.IFluidStackPM;
import com.verdantartifice.primalmagick.platform.services.IFluidHandlerService;

import java.util.function.Predicate;

public class FluidHandlerServiceNeoforge implements IFluidHandlerService {
    @Override
    public IFluidHandlerPM create(int capacity) {
        return new FluidHandlerPMNeoforge(capacity);
    }

    @Override
    public IFluidHandlerPM create(int capacity, Predicate<IFluidStackPM> validator) {
        return new FluidHandlerPMNeoforge(capacity, validator);
    }
}
