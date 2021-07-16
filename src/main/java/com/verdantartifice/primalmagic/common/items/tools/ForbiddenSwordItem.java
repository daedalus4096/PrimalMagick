package com.verdantartifice.primalmagic.common.items.tools;

import com.google.common.collect.ImmutableMap;
import com.verdantartifice.primalmagic.common.enchantments.EnchantmentsPM;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.NonNullList;

/**
 * Definition of a sword that comes pre-enchanted with Essence Thief.
 * 
 * @author Daedalus4096
 */
public class ForbiddenSwordItem extends SwordItem {
    public ForbiddenSwordItem(IItemTier tier, int attackDamageIn, float attackSpeedIn, Item.Properties builderIn) {
        super(tier, attackDamageIn, attackSpeedIn, builderIn);
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack stack = new ItemStack(this);
        EnchantmentHelper.setEnchantments(ImmutableMap.of(EnchantmentsPM.ESSENCE_THIEF.get(), 2), stack);
        return stack;
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        // Populate the creative pane with a pre-enchanted sword
        if (this.isInGroup(group)) {
            items.add(this.getDefaultInstance());
        }
    }
}
