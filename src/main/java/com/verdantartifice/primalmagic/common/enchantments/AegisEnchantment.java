package com.verdantartifice.primalmagic.common.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

/**
 * Definition of a rune enchantment that combines the damage reduction of the base five protection
 * enchantments.  Does not reduce burn time or explosion knockback, however.
 * 
 * @author Daedalus4096
 */
public class AegisEnchantment extends ProtectionEnchantment {
    public AegisEnchantment(Enchantment.Rarity rarity, EquipmentSlotType... slotTypes) {
        super(rarity, ProtectionEnchantment.Type.ALL, slotTypes);
    }
    
    @Override
    public int calcModifierDamage(int level, DamageSource source) {
        if (source.canHarmInCreative()) {
            return 0;
        } else if (source == DamageSource.FALL) {
            return level * 3;
        } else if (source.isFireDamage() || source.isExplosion() || source.isProjectile()) {
            return level * 2;
        } else {
            return level;
        }
    }
    
    @Override
    public boolean canApplyTogether(Enchantment ench) {
        return !(ench instanceof ProtectionEnchantment);
    }
    
    @Override
    public boolean canApply(ItemStack stack) {
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
