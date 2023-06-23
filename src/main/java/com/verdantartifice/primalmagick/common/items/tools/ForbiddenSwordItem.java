package com.verdantartifice.primalmagick.common.items.tools;

import com.google.common.collect.ImmutableMap;
import com.verdantartifice.primalmagick.common.enchantments.EnchantmentsPM;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

/**
 * Definition of a sword that comes pre-enchanted with Essence Thief.
 * 
 * @author Daedalus4096
 */
public class ForbiddenSwordItem extends SwordItem {
    public ForbiddenSwordItem(Tier tier, int attackDamageIn, float attackSpeedIn, Item.Properties builderIn) {
        super(tier, attackDamageIn, attackSpeedIn, builderIn);
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack stack = new ItemStack(this);
        EnchantmentHelper.setEnchantments(ImmutableMap.of(EnchantmentsPM.ESSENCE_THIEF.get(), 2), stack);
        return stack;
    }
}
