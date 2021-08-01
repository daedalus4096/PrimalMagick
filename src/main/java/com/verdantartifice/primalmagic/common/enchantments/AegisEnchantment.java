package com.verdantartifice.primalmagic.common.enchantments;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;

/**
 * Definition of a rune enchantment that combines the damage reduction of the base five protection
 * enchantments.  Does not reduce burn time or explosion knockback, however.
 * 
 * @author Daedalus4096
 */
public class AegisEnchantment extends ProtectionEnchantment {
    public AegisEnchantment(Enchantment.Rarity rarity, EquipmentSlot... slotTypes) {
        super(rarity, ProtectionEnchantment.Type.ALL, slotTypes);
    }
    
    @Override
    public int getDamageProtection(int level, DamageSource source) {
        if (source.isBypassInvul()) {
            return 0;
        } else if (source == DamageSource.FALL) {
            return level * 3;
        } else if (source.isFire() || source.isExplosion() || source.isProjectile()) {
            return level * 2;
        } else {
            return level;
        }
    }
    
    @Override
    public boolean checkCompatibility(Enchantment ench) {
        return !(ench instanceof ProtectionEnchantment);
    }
    
    @Override
    public boolean canEnchant(ItemStack stack) {
        return stack.canApplyAtEnchantingTable(this);
    }
    
    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return false;
    }
    
    @Override
    public boolean isAllowedOnBooks() {
        return false;
    }
}
