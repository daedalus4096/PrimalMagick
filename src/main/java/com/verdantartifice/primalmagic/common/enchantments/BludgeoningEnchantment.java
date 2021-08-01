package com.verdantartifice.primalmagic.common.enchantments;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.item.enchantment.Enchantment;

/**
 * Definition of a melee damage boosting enchantment for magical staves.
 * 
 * @author Daedalus4096
 */
public class BludgeoningEnchantment extends Enchantment {
    public BludgeoningEnchantment(Enchantment.Rarity rarity, EquipmentSlot... slots) {
        super(rarity, EnchantmentTypesPM.STAFF, slots);
    }
    
    @Override
    public int getMinCost(int enchantmentLevel) {
        return 1 + (enchantmentLevel - 1) * 11;
    }
    
    @Override
    public int getMaxCost(int enchantmentLevel) {
        return this.getMinCost(enchantmentLevel) + 20;
    }
    
    @Override
    public int getMaxLevel() {
        return 5;
    }
    
    @Override
    public float getDamageBonus(int level, MobType creatureType) {
        return 1.0F + (float)Math.max(0, level - 1) * 0.5F;
    }
}
