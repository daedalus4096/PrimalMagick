package com.verdantartifice.primalmagic.common.items.tools;

import com.google.common.collect.ImmutableMap;
import com.verdantartifice.primalmagic.common.enchantments.EnchantmentsPM;

import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;

/**
 * Definition of a fishing rod that comes pre-enchanted with Bounty.
 * 
 * @author Daedalus4096
 */
public class PrimalFishingRodItem extends FishingRodItem {
    public PrimalFishingRodItem(Item.Properties builder) {
        super(builder);
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack stack = new ItemStack(this);
        EnchantmentHelper.setEnchantments(ImmutableMap.of(EnchantmentsPM.BOUNTY.get(), 2), stack);
        return stack;
    }

    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
        // Populate the creative pane with a pre-enchanted axe
        if (this.allowdedIn(group)) {
            items.add(this.getDefaultInstance());
        }
    }
}
