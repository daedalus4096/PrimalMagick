package com.verdantartifice.primalmagic.common.items.tools;

import com.google.common.collect.ImmutableMap;
import com.verdantartifice.primalmagic.common.enchantments.EnchantmentsPM;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.AxeItem;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

/**
 * Definition of an axe that comes pre-enchanted with Disintegration.
 * 
 * @author Daedalus4096
 */
public class PrimalAxeItem extends AxeItem {
    public PrimalAxeItem(IItemTier tier, float attackDamageIn, float attackSpeedIn, Item.Properties builder) {
        super(tier, attackDamageIn, attackSpeedIn, builder);
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack stack = new ItemStack(this);
        EnchantmentHelper.setEnchantments(ImmutableMap.of(EnchantmentsPM.DISINTEGRATION.get(), 2), stack);
        return stack;
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        // Populate the creative pane with a pre-enchanted axe
        if (this.isInGroup(group)) {
            items.add(this.getDefaultInstance());
        }
    }
}
