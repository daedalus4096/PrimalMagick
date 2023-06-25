package com.verdantartifice.primalmagick.common.enchantments;

import com.verdantartifice.primalmagick.common.tags.DamageTypeTagsPM;

import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;

/**
 * Definition of an enchantment that reduces magick damage taken.
 * 
 * @author Daedalus4096
 */
public class MagickProtectionEnchantment extends ProtectionEnchantment {
    public MagickProtectionEnchantment(Enchantment.Rarity rarity, EquipmentSlot... slotTypes) {
        super(rarity, null, slotTypes);
    }

    @Override
    public int getMinCost(int level) {
        return 5 + (level - 1) * 8;
    }

    @Override
    public int getMaxCost(int level) {
        return this.getMinCost(level) + 8;
    }

    @Override
    public int getDamageProtection(int level, DamageSource source) {
        if (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return 0;
        } else if (source.is(DamageTypeTagsPM.IS_MAGIC)) {
            return level * 2;
        } else {
            return 0;
        }
    }

    @Override
    public boolean checkCompatibility(Enchantment other) {
        if (other instanceof ProtectionEnchantment protEnch) {
            // Allow stacking with Feather Fall, but not other protection-type enchantments
            if (other instanceof MagickProtectionEnchantment) {
                return false;
            } else {
                return protEnch.type == ProtectionEnchantment.Type.FALL;
            }
        } else {
            return super.checkCompatibility(other);
        }
    }
}
