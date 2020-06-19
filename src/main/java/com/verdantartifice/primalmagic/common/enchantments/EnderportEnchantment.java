package com.verdantartifice.primalmagic.common.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Definition of an enchantment that teleports the shooter of an arrow to the impact location.
 * 
 * @author Daedalus4096
 */
public class EnderportEnchantment extends AbstractRuneEnchantment {
    public EnderportEnchantment(Enchantment.Rarity rarity, EquipmentSlotType... slots) {
        super(rarity, EnchantmentType.BOW, slots);
    }
    
    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 5 + ((enchantmentLevel - 1) * 10);
    }
    
    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 15;
    }
    
    @Override
    public int getMaxLevel() {
        return 1;
    }
    
    @Override
    public boolean canApply(ItemStack stack) {
        Item item = stack.getItem();
        return item instanceof CrossbowItem ? true : super.canApply(stack);
    }
}
