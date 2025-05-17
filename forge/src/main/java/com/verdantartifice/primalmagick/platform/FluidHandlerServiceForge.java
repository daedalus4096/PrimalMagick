package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.common.capabilities.FluidHandlerPMForge;
import com.verdantartifice.primalmagick.common.capabilities.IFluidHandlerPM;
import com.verdantartifice.primalmagick.common.fluids.IFluidStackPM;
import com.verdantartifice.primalmagick.platform.services.IFluidHandlerService;

import java.util.function.Predicate;

public class FluidHandlerServiceForge implements IFluidHandlerService {
    @Override
    public IFluidHandlerPM create(int capacity) {
        return new FluidHandlerPMForge(capacity);
    }

    @Override
    public IFluidHandlerPM create(int capacity, Predicate<IFluidStackPM> validator) {
        return new FluidHandlerPMForge(capacity, validator);
    }
}
