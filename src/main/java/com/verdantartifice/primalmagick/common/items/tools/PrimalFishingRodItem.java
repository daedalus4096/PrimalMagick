package com.verdantartifice.primalmagick.common.items.tools;

import com.google.common.collect.ImmutableMap;
import com.verdantartifice.primalmagick.common.enchantments.EnchantmentsPM;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

/**
 * Definition of a fishing rod that comes pre-enchanted with Bounty.
 * 
 * @author Daedalus4096
 */
public class PrimalFishingRodItem extends TieredFishingRodItem {
    public PrimalFishingRodItem(Tier tier, Item.Properties builder) {
        super(tier, builder);
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
