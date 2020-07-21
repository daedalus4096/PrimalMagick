package com.verdantartifice.primalmagic.common.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EquipmentSlotType;

/**
 * Definition of a wand enchantment that mimics and stacks with the effect of the Amplify spell mod.
 * 
 * @author Daedalus4096
 * @see {@link com.verdantartifice.primalmagic.common.spells.mods.AmplifySpellMod}
 */
public class SpellPowerEnchantment extends Enchantment {
    public SpellPowerEnchantment(Enchantment.Rarity rarity, EquipmentSlotType... slots) {
        super(rarity, EnchantmentTypesPM.WAND, slots);
    }
    
    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 10 + ((enchantmentLevel - 1) * 10);
    }
    
    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 25;
    }
    
    @Override
    public int getMaxLevel() {
        return 5;
    }
}
