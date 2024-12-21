package com.verdantartifice.primalmagick.platform.services;

import com.verdantartifice.primalmagick.common.capabilities.IItemHandlerPM;
import com.verdantartifice.primalmagick.common.tiles.base.AbstractTilePM;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IItemHandlerService {
    IItemHandlerPM create(@Nullable AbstractTilePM tile);
    IItemHandlerPM create(int size, @Nullable AbstractTilePM tile);
    IItemHandlerPM create(NonNullList<ItemStack> stacks, @Nullable AbstractTilePM tile);
    IItemHandlerPM.Builder builder(NonNullList<ItemStack> stacks, @Nullable AbstractTilePM tile);

    IItemHandlerPM wrap(Container container, @Nullable Direction side);

    /**
     * Attempts to get an item handler capability for the given side of the given position in the given world.
     * First searches for tiles that directly implement the capability, then attempts to wrap instances of the
     * vanilla inventory interface.
     *
     * @param level the level containing the desired tile entity
     * @param pos the position of the desired tile entity
     * @param side the side of the tile entity to be queried
     * @return the item handler of the tile entity, or null if no such capability could be found
     */
    IItemHandlerPM get(@Nonnull Level level, @Nonnull BlockPos pos, @Nullable Direction side);

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
