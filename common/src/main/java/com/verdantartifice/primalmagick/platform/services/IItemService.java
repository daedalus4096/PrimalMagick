package com.verdantartifice.primalmagick.platform.services;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;

public interface IItemService {
    InteractionResult onItemUseFirst(Item item, ItemStack stack, UseOnContext context);
}
