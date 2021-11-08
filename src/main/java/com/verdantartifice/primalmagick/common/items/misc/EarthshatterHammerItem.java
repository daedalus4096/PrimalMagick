package com.verdantartifice.primalmagick.common.items.misc;

import java.util.Random;

import com.verdantartifice.primalmagick.PrimalMagic;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.common.Tags;

/**
 * Item definition for an earthshatter hammer.  Can be crafting with ore to break it into grit for
 * ore doubling.
 * 
 * @author Daedalus4096
 */
public class EarthshatterHammerItem extends Item {
    protected static final Random RNG = new Random();
    
    public EarthshatterHammerItem() {
        super(new Item.Properties().tab(PrimalMagic.ITEM_GROUP).durability(255).rarity(Rarity.UNCOMMON));
    }
    
    @Override
    public ItemStack getContainerItem(ItemStack stack) {
        if (this.hasContainerItem(stack)) {
            ItemStack newStack = stack.copy();
            newStack.hurt(1, RNG, null);
            return newStack;
        } else {
            return ItemStack.EMPTY;
        }
    }
    
    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return stack.getItem() instanceof EarthshatterHammerItem && stack.getDamageValue() < stack.getMaxDamage();
    }

    @Override
    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        return repair.is(Tags.Items.INGOTS_IRON) || super.isValidRepairItem(toRepair, repair);
    }
}
