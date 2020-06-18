package com.verdantartifice.primalmagic.common.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;

/**
 * Base class definition for a rune enchantment.  Rune enchantments can only be applied to items by
 * socketing the appropriate rune combination into them, not at the enchanting table.  They cannot
 * be applied to books.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractRuneEnchantment extends Enchantment {
    protected AbstractRuneEnchantment(Enchantment.Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType[] slots) {
        super(rarityIn, typeIn, slots);
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
