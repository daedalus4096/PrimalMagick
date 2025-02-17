package com.verdantartifice.primalmagick.common.items.misc;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

/**
 * Item definition for an earthshatter hammer.  Can be crafted with ore to break it into grit for
 * ore doubling.
 * 
 * @author Daedalus4096
 */
public abstract class EarthshatterHammerItem extends Item {
    protected EarthshatterHammerItem(Item.Properties properties) {
        super(properties);
    }
    
    // Technically an override, but the base method exists only in loader-specific code
    public ItemStack getCraftingRemainingItem(ItemStack stack) {
        if (this.hasCraftingRemainingItem(stack)) {
            ItemStack newStack = stack.copy();
            newStack.setDamageValue(newStack.getDamageValue() + 1);
            return newStack;
        } else {
            return ItemStack.EMPTY;
        }
    }

    // Technically an override, but the base method exists only in loader-specific code
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return stack.getItem() instanceof EarthshatterHammerItem && stack.getDamageValue() < stack.getMaxDamage();
    }
}
