package com.verdantartifice.primalmagic.common.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;

/**
 * Definition of an enchantment that slowly mends equipment over time.
 * 
 * @author Daedalus4096
 */
public class RegrowthEnchantment extends Enchantment {
    public RegrowthEnchantment(Enchantment.Rarity rarityIn, EquipmentSlotType... slots) {
        super(rarityIn, EnchantmentType.BREAKABLE, slots);
    }
    
    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return enchantmentLevel * 30;
    }
    
    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 60;
    }
    
    @Override
    public int getMaxLevel() {
        return 1;
    }
}
