package com.verdantartifice.primalmagick.common.enchantments;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;

/**
 * Definition of a wand enchantment that mimics and stacks with the effect of the Amplify spell mod.
 * 
 * @author Daedalus4096
 * @see {@link com.verdantartifice.primalmagick.common.spells.mods.AmplifySpellMod}
 */
public class SpellPowerEnchantment extends Enchantment {
    public SpellPowerEnchantment(Enchantment.Rarity rarity, EquipmentSlot... slots) {
        super(rarity, EnchantmentTypesPM.WAND, slots);
    }
    
    @Override
    public int getMinCost(int enchantmentLevel) {
        return 10 + ((enchantmentLevel - 1) * 10);
    }
    
    @Override
    public int getMaxCost(int enchantmentLevel) {
        return this.getMinCost(enchantmentLevel) + 25;
    }
    
    @Override
    public int getMaxLevel() {
        return 5;
    }
}
