package com.verdantartifice.primalmagic.common.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;

/**
 * Definition of an enchantment that grants a chance of bonus nuggets when mining quartz or metal
 * ores.
 * 
 * @author Daedalus4096
 */
public class LuckyStrikeEnchantment extends AbstractRuneEnchantment {
    public LuckyStrikeEnchantment(Enchantment.Rarity rarity, EquipmentSlotType... slots) {
        super(rarity, EnchantmentType.FISHING_ROD, slots);
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
        return 4;
    }
}
