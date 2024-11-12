package com.verdantartifice.primalmagick.platform.services;

import com.verdantartifice.primalmagick.common.capabilities.IItemHandlerPM;
import com.verdantartifice.primalmagick.common.tiles.base.AbstractTilePM;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public interface IItemHandlerService {
    IItemHandlerPM create(@Nullable AbstractTilePM tile);
    IItemHandlerPM create(int size, @Nullable AbstractTilePM tile);
    IItemHandlerPM create(NonNullList<ItemStack> stacks, @Nullable AbstractTilePM tile);
    IItemHandlerPM.Builder builder(NonNullList<ItemStack> stacks, @Nullable AbstractTilePM tile);

    /**
     * Returns a shallow copy of the given item handler wrapped in a container.
     *
     * @param itemHandler the item handler to be wrapped
     * @return a wrapper around a copy of the given item handler, or null
     */
    default Container wrapAsContainer(IItemHandlerPM itemHandler) {
        SimpleContainer retVal = new SimpleContainer(itemHandler.getSlots());
        for (int index = 0; index < itemHandler.getSlots(); index++) {
            retVal.setItem(index, itemHandler.getStackInSlot(index));
        }
        return retVal;
    }
}
