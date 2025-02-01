package com.verdantartifice.primalmagick.platform.services;

import com.verdantartifice.primalmagick.common.capabilities.IItemHandlerPM;
import com.verdantartifice.primalmagick.common.tiles.base.AbstractTilePM;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public interface IItemHandlerService {
    IItemHandlerPM create(@Nullable AbstractTilePM tile);
    IItemHandlerPM create(int size, @Nullable AbstractTilePM tile);
    IItemHandlerPM create(NonNullList<ItemStack> stacks, @Nullable AbstractTilePM tile);
    IItemHandlerPM.Builder builder(NonNullList<ItemStack> stacks, @Nullable AbstractTilePM tile);

    IItemHandlerPM wrap(Container container, @Nullable Direction side);
}
