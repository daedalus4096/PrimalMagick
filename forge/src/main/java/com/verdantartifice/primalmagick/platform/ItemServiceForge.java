package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.platform.services.IItemService;
import net.minecraft.world.item.ItemStack;

public class ItemServiceForge implements IItemService {
    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return stack.hasCraftingRemainingItem();
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack stack) {
        return stack.getCraftingRemainingItem();
    }
}
