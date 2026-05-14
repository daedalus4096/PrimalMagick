package com.verdantartifice.primalmagick.common.util;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.CraftingInput;

/**
 * Collection of utility methods pertaining to crafting.
 *
 * @author Daedalus4096
 */
public class CraftingUtils {
    public static NonNullList<ItemStack> defaultCraftingReminder(CraftingInput input) {
        NonNullList<ItemStack> result = NonNullList.withSize(input.size(), ItemStack.EMPTY);

        for (int slot = 0; slot < result.size(); slot++) {
            Item item = input.getItem(slot).getItem();
            ItemStackTemplate remainder = item.getCraftingRemainder();
            result.set(slot, remainder != null ? remainder.create() : ItemStack.EMPTY);
        }

        return result;
    }
}
