package com.verdantartifice.primalmagick.common.items.tools;

import com.google.common.collect.ImmutableMap;
import com.verdantartifice.primalmagick.common.enchantments.EnchantmentsPM;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

/**
 * Definition of a pickaxe that comes pre-enchanted with Lucky Strike.
 * 
 * @author Daedalus4096
 */
public class PrimalPickaxeItem extends PickaxeItem {
    public PrimalPickaxeItem(Tier tier, int attackDamageIn, float attackSpeedIn, Item.Properties builder) {
        super(tier, attackDamageIn, attackSpeedIn, builder);
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack stack = new ItemStack(this);
        EnchantmentHelper.setEnchantments(ImmutableMap.of(EnchantmentsPM.LUCKY_STRIKE.get(), 2), stack);
        return stack;
    }
}
