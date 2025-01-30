package com.verdantartifice.primalmagick.common.tiles;

import com.verdantartifice.primalmagick.common.capabilities.IItemHandlerPM;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.EmptyHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface IHasItemHandlerCapabilityForge {
    NonNullList<LazyOptional<IItemHandler>> getItemHandlerOpts();

    Optional<Integer> getInventoryIndexForFace(@NotNull Direction face);

    default @NotNull NonNullList<LazyOptional<IItemHandler>> makeItemHandlerOpts(final NonNullList<IItemHandlerPM> itemHandlers) {
        NonNullList<LazyOptional<IItemHandler>> retVal = NonNullList.withSize(itemHandlers.size(), LazyOptional.empty());
        for (int index = 0; index < itemHandlers.size(); index++) {
            final int optIndex = index;
            retVal.set(index, LazyOptional.of(() -> itemHandlers.get(optIndex) instanceof IItemHandler castHandler ? castHandler : EmptyHandler.INSTANCE));
        }
        return retVal;
    }

    default @NotNull LazyOptional<IItemHandler> getItemHandlerCapability(@Nullable Direction direction) {
        if (direction != null && this.getInventoryIndexForFace(direction).isPresent()) {
            return this.getItemHandlerOpts().get(this.getInventoryIndexForFace(direction).get());
        } else {
            return LazyOptional.empty();
        }
    }
}
