package com.verdantartifice.primalmagick.common.enchantments;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

/**
 * Base class definition for a rune enchantment.  Rune enchantments can only be applied to items by
 * socketing the appropriate rune combination into them, not at the enchanting table.  They can
 * be applied to books, but are not available through villager trading.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractRuneEnchantment extends Enchantment {
    protected AbstractRuneEnchantment(Enchantment.Rarity rarityIn, EnchantmentCategory typeIn, EquipmentSlot[] slots) {
        super(rarityIn, typeIn, slots);
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
    public boolean isTradeable() {
        return false;
    }
}
