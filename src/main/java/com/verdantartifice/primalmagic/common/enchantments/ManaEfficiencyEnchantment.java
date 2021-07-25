package com.verdantartifice.primalmagic.common.enchantments;

import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.entity.EquipmentSlot;

/**
 * Definition of a mana efficiency enchantment that can be applied to wands or staves.  Decreases
 * mana consumed when crafting or casting spells.
 * 
 * @author Daedalus4096
 */
public class ManaEfficiencyEnchantment extends Enchantment {
    public ManaEfficiencyEnchantment(Enchantment.Rarity rarity, EquipmentSlot... slots) {
        super(rarity, EnchantmentTypesPM.WAND, slots);
    }
    
    @Override
    public int getMinCost(int enchantmentLevel) {
        return 1 + 10 * (enchantmentLevel - 1);
    }

    @Override
    public int getMaxCost(int enchantmentLevel) {
        return super.getMinCost(enchantmentLevel) + 50;
    }
    
    @Override
    public int getMaxLevel() {
        return 5;
    }
}
