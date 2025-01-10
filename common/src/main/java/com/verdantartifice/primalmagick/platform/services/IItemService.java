package com.verdantartifice.primalmagick.platform.services;

import net.minecraft.world.item.ItemStack;

public interface IItemService {
    boolean hasCraftingRemainingItem(ItemStack stack);
    ItemStack getCraftingRemainingItem(ItemStack stack);
}
