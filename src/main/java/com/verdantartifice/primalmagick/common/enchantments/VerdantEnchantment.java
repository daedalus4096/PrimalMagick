package com.verdantartifice.primalmagick.common.enchantments;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;

/**
 * Definition of an enchantment that simulates bone meal when using on crops, at the cost of
 * durability.  Higher enchantment levels reduce durability cost.
 * 
 * @author Daedalus4096
 */
public class VerdantEnchantment extends AbstractRuneEnchantment {
    public static final int BASE_DAMAGE_PER_USE = 8;
    
    public VerdantEnchantment(Enchantment.Rarity rarity, EquipmentSlot... slots) {
        super(rarity, EnchantmentTypesPM.HOE, slots);
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
        return 4;
    }
}
