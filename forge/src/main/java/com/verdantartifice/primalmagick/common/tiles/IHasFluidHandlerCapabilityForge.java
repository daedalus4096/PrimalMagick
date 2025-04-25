package com.verdantartifice.primalmagick.common.tiles;

import com.verdantartifice.primalmagick.common.capabilities.IFluidHandlerPM;
import net.minecraft.core.NonNullList;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.EmptyFluidHandler;
import org.jetbrains.annotations.NotNull;

public interface IHasFluidHandlerCapabilityForge {
    default @NotNull NonNullList<LazyOptional<IFluidHandler>> makeFluidHandlerOpts(final NonNullList<IFluidHandlerPM> fluidHandlers) {
        NonNullList<LazyOptional<IFluidHandler>> retVal = NonNullList.withSize(fluidHandlers.size(), LazyOptional.empty());
        for (int index = 0; index < fluidHandlers.size(); index++) {
            final int optIndex = index;
            retVal.set(index, LazyOptional.of(() -> fluidHandlers.get(optIndex) instanceof IFluidHandler castHandler ? castHandler : EmptyFluidHandler.INSTANCE));
        }
        return retVal;
    }
}
