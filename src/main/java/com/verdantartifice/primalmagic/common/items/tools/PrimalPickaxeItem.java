package com.verdantartifice.primalmagic.common.items.tools;

import com.google.common.collect.ImmutableMap;
import com.verdantartifice.primalmagic.common.enchantments.EnchantmentsPM;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.util.NonNullList;

/**
 * Definition of a pickaxe that comes pre-enchanted with Lucky Strike.
 * 
 * @author Daedalus4096
 */
public class PrimalPickaxeItem extends PickaxeItem {
    public PrimalPickaxeItem(IItemTier tier, int attackDamageIn, float attackSpeedIn, Item.Properties builder) {
        super(tier, attackDamageIn, attackSpeedIn, builder);
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack stack = new ItemStack(this);
        EnchantmentHelper.setEnchantments(ImmutableMap.of(EnchantmentsPM.LUCKY_STRIKE.get(), 2), stack);
        return stack;
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        // Populate the creative pane with a pre-enchanted pickaxe
        if (this.isInGroup(group)) {
            items.add(this.getDefaultInstance());
        }
    }
}
