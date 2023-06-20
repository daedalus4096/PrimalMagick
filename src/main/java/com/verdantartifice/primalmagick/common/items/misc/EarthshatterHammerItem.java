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
public class EarthshatterHammerItem extends Item {
    protected static final RandomSource RNG = RandomSource.create();
    
    public EarthshatterHammerItem() {
        super(new Item.Properties().durability(255).rarity(Rarity.UNCOMMON).setNoRepair());
    }
    
    @Override
    public ItemStack getCraftingRemainingItem(ItemStack stack) {
        if (this.hasCraftingRemainingItem(stack)) {
            ItemStack newStack = stack.copy();
            newStack.hurt(1, RNG, null);
            return newStack;
        } else {
            return ItemStack.EMPTY;
        }
    }
    
    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return stack.getItem() instanceof EarthshatterHammerItem && stack.getDamageValue() < stack.getMaxDamage();
    }
}
