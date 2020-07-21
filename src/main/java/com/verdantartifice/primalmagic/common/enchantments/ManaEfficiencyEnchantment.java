package com.verdantartifice.primalmagic.common.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EquipmentSlotType;

/**
 * Definition of a mana efficiency enchantment that can be applied to wands or staves.  Decreases
 * mana consumed when crafting or casting spells.
 * 
 * @author Daedalus4096
 */
public class ManaEfficiencyEnchantment extends Enchantment {
    public ManaEfficiencyEnchantment(Enchantment.Rarity rarity, EquipmentSlotType... slots) {
        super(rarity, EnchantmentTypesPM.WAND, slots);
    }
    
    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 1 + 10 * (enchantmentLevel - 1);
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return super.getMinEnchantability(enchantmentLevel) + 50;
    }
    
    @Override
    public int getMaxLevel() {
        return 5;
    }
}
