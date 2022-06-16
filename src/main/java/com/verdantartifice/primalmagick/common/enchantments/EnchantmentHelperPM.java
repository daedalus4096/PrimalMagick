package com.verdantartifice.primalmagick.common.enchantments;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

/**
 * Helper methods for dealing with mod enchantments.
 * 
 * @author Daedalus4096
 */
public class EnchantmentHelperPM {
    public static boolean hasEnderport(LivingEntity entity) {
        return EnchantmentHelper.getEnchantmentLevel(EnchantmentsPM.ENDERPORT.get(), entity) > 0;
    }
    
    public static boolean hasRegrowth(ItemStack stack) {
        return stack.getEnchantmentLevel(EnchantmentsPM.REGROWTH.get()) > 0;
    }
}
