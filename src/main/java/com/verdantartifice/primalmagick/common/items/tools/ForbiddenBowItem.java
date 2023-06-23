package com.verdantartifice.primalmagick.common.items.tools;

import com.google.common.collect.ImmutableMap;
import com.verdantartifice.primalmagick.common.enchantments.EnchantmentsPM;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

/**
 * Definition for a bow made of the magickal metal hexium which comes pre-enchanted with Soulpiercing.
 * 
 * @author Daedalus4096
 */
public class ForbiddenBowItem extends TieredBowItem {
    public ForbiddenBowItem(Item.Properties properties) {
        super(ItemTierPM.HEXIUM, properties);
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack stack = new ItemStack(this);
        EnchantmentHelper.setEnchantments(ImmutableMap.of(EnchantmentsPM.SOULPIERCING.get(), 2), stack);
        return stack;
    }
}
