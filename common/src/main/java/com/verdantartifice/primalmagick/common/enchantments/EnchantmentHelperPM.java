package com.verdantartifice.primalmagick.common.enchantments;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.Optional;

/**
 * Helper methods for dealing with mod enchantments.
 * 
 * @author Daedalus4096
 */
public class EnchantmentHelperPM {
    public static int getEquippedEnchantmentLevel(LivingEntity entity, ResourceKey<Enchantment> enchantment) {
        Optional<Holder.Reference<Enchantment>> holderOpt = entity.level().holderLookup(Registries.ENCHANTMENT).get(enchantment);
        return holderOpt.map(enchantmentReference -> EnchantmentHelper.getEnchantmentLevel(enchantmentReference, entity)).orElse(0);
    }
    
    public static int getEnchantmentLevel(ItemStack stack, ResourceKey<Enchantment> enchantment, HolderLookup.Provider registries) {
        Optional<Holder.Reference<Enchantment>> holderOpt = registries.lookupOrThrow(Registries.ENCHANTMENT).get(enchantment);
        return holderOpt.map(enchantmentReference -> stack.getEnchantments().getLevel(enchantmentReference)).orElse(0);
    }
    
    public static boolean hasEnderport(LivingEntity entity) {
        return getEquippedEnchantmentLevel(entity, EnchantmentsPM.ENDERPORT) > 0;
    }
    
    public static boolean hasRegrowth(ItemStack stack, HolderLookup.Provider registries) {
        return getEnchantmentLevel(stack, EnchantmentsPM.REGROWTH, registries) > 0;
    }
}
