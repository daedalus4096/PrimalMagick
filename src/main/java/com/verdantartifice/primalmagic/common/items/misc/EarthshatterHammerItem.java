package com.verdantartifice.primalmagic.common.items.misc;

import com.verdantartifice.primalmagic.PrimalMagic;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;

/**
 * Item definition for an earthshatter hammer.  Can be crafting with ore to break it into grit for
 * ore doubling.
 * 
 * @author Daedalus4096
 */
public class EarthshatterHammerItem extends Item {
    public EarthshatterHammerItem() {
        super(new Item.Properties().group(PrimalMagic.ITEM_GROUP).maxDamage(255).rarity(Rarity.UNCOMMON));
    }
    
    @Override
    public ItemStack getContainerItem(ItemStack stack) {
        if (this.hasContainerItem(stack)) {
            ItemStack newStack = stack.copy();
            newStack.setDamage(stack.getDamage() + 1);
            return newStack;
        } else {
            return ItemStack.EMPTY;
        }
    }
    
    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return stack.getItem() instanceof EarthshatterHammerItem && stack.getDamage() < stack.getMaxDamage();
    }
}
