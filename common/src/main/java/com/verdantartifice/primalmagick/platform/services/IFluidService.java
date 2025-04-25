package com.verdantartifice.primalmagick.platform.services;

import com.verdantartifice.primalmagick.common.fluids.IFluidStackPM;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.NotNull;

public interface IFluidService {
    @NotNull IFluidStackPM emptyStack();
    @NotNull IFluidStackPM makeFluidStack(@NotNull Fluid fluid, int amount);
}
