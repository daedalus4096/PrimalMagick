package com.verdantartifice.primalmagic.common.enchantments;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;

/**
 * Helper methods for dealing with mod enchantments.
 * 
 * @author Daedalus4096
 */
public class EnchantmentHelperPM {
    public static boolean hasEnderport(LivingEntity entity) {
        return EnchantmentHelper.getMaxEnchantmentLevel(EnchantmentsPM.ENDERPORT.get(), entity) > 0;
    }
}
