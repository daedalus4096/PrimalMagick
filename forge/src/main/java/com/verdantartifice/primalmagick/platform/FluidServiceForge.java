package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.common.fluids.FluidStackPMForge;
import com.verdantartifice.primalmagick.common.fluids.IFluidStackPM;
import com.verdantartifice.primalmagick.platform.services.IFluidService;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.NotNull;

public class FluidServiceForge implements IFluidService {
    @Override
    public @NotNull IFluidStackPM emptyStack() {
        return FluidStackPMForge.EMPTY;
    }

    @Override
    public @NotNull IFluidStackPM makeFluidStack(@NotNull Fluid fluid, int amount) {
        return new FluidStackPMForge(fluid, amount);
    }
}
