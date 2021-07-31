package com.verdantartifice.primalmagic.common.enchantments;

import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.entity.EquipmentSlot;

/**
 * Definition of an enchantment that slowly mends equipment over time.
 * 
 * @author Daedalus4096
 */
public class RegrowthEnchantment extends AbstractRuneEnchantment {
    public RegrowthEnchantment(Enchantment.Rarity rarityIn, EquipmentSlot... slots) {
        super(rarityIn, EnchantmentCategory.BREAKABLE, slots);
    }
    
    @Override
    public int getMinCost(int enchantmentLevel) {
        return enchantmentLevel * 30;
    }
    
    @Override
    public int getMaxCost(int enchantmentLevel) {
        return this.getMinCost(enchantmentLevel) + 60;
    }
    
    @Override
    public int getMaxLevel() {
        return 1;
    }
}
