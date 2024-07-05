package com.verdantartifice.primalmagick.common.enchantments;

import java.util.Optional;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

/**
 * Helper methods for dealing with mod enchantments.
 * 
 * @author Daedalus4096
 */
public class EnchantmentHelperPM {
    public static int getEquippedEnchantmentLevel(LivingEntity entity, ResourceKey<Enchantment> enchantment) {
        Optional<Holder.Reference<Enchantment>> holderOpt = entity.level().holderLookup(Registries.ENCHANTMENT).get(enchantment);
        if (holderOpt.isPresent()) {
            return EnchantmentHelper.getEnchantmentLevel(holderOpt.get(), entity);
        } else {
            return 0;
        }
    }
    
    public static int getEnchantmentLevel(ItemStack stack, ResourceKey<Enchantment> enchantment, HolderLookup<Enchantment> holderLookup) {
        Optional<Holder.Reference<Enchantment>> holderOpt = holderLookup.get(enchantment);
        if (holderOpt.isPresent()) {
            return stack.getEnchantments().getLevel(holderOpt.get());
        } else {
            return 0;
        }
    }
    
    public static boolean hasEnderport(LivingEntity entity) {
        return getEquippedEnchantmentLevel(entity, EnchantmentsPM.ENDERPORT) > 0;
    }
    
    public static boolean hasRegrowth(ItemStack stack, HolderLookup<Enchantment> holderLookup) {
        return getEnchantmentLevel(stack, EnchantmentsPM.REGROWTH, holderLookup) > 0;
    }
}
