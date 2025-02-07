package com.verdantartifice.primalmagick.common.items;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;

import java.util.Map;

/**
 * Interface for items that are intended to come with one or more enchantments applied
 * on their default instances.
 * 
 * @author Daedalus4096
 */
public interface IEnchantedByDefault {
    ItemStack getDefaultInstance();
    Map<ResourceKey<Enchantment>, Integer> getDefaultEnchantments();
    
    default ItemStack getDefaultEnchantedInstance(HolderLookup.Provider registries) {
        ItemStack stack = this.getDefaultInstance();
        ItemEnchantments.Mutable enchants = new ItemEnchantments.Mutable(stack.getEnchantments());
        this.getDefaultEnchantments().forEach((enchKey, level) -> {
            registries.lookupOrThrow(Registries.ENCHANTMENT).get(enchKey).ifPresent(ench -> {
                enchants.upgrade(ench, level);
            });
        });
        EnchantmentHelper.setEnchantments(stack, enchants.toImmutable());
        return stack;
    }
}
