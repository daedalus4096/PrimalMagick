package com.verdantartifice.primalmagick.platform.services;

import com.verdantartifice.primalmagick.common.capabilities.IFluidHandlerPM;
import com.verdantartifice.primalmagick.common.fluids.IFluidStackPM;

import java.util.function.Predicate;

public interface IFluidHandlerService {
    IFluidHandlerPM create(int tanks, int capacity);
    IFluidHandlerPM create(int tanks, int capacity, Predicate<IFluidStackPM> validator);
}
