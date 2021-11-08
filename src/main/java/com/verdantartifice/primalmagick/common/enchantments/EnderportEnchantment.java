package com.verdantartifice.primalmagick.common.enchantments;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

/**
 * Definition of an enchantment that teleports the shooter of an arrow to the impact location.
 * 
 * @author Daedalus4096
 */
public class EnderportEnchantment extends AbstractRuneEnchantment {
    public EnderportEnchantment(Enchantment.Rarity rarity, EquipmentSlot... slots) {
        super(rarity, EnchantmentCategory.BOW, slots);
    }
    
    @Override
    public int getMinCost(int enchantmentLevel) {
        return 5 + ((enchantmentLevel - 1) * 10);
    }
    
    @Override
    public int getMaxCost(int enchantmentLevel) {
        return this.getMinCost(enchantmentLevel) + 15;
    }
    
    @Override
    public int getMaxLevel() {
        return 1;
    }
    
    @Override
    public boolean canEnchant(ItemStack stack) {
        Item item = stack.getItem();
        return item instanceof CrossbowItem ? true : super.canEnchant(stack);
    }
}
