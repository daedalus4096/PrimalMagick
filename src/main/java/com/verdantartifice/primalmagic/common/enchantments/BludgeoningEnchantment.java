package com.verdantartifice.primalmagic.common.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.inventory.EquipmentSlotType;

/**
 * Definition of a melee damage boosting enchantment for magical staves.
 * 
 * @author Daedalus4096
 */
public class BludgeoningEnchantment extends Enchantment {
    public BludgeoningEnchantment(Enchantment.Rarity rarity, EquipmentSlotType... slots) {
        super(rarity, EnchantmentTypesPM.STAFF, slots);
    }
    
    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 1 + (enchantmentLevel - 1) * 11;
    }
    
    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 20;
    }
    
    @Override
    public int getMaxLevel() {
        return 5;
    }
    
    @Override
    public float calcDamageByCreature(int level, CreatureAttribute creatureType) {
        return 1.0F + (float)Math.max(0, level - 1) * 0.5F;
    }
}
