package com.verdantartifice.primalmagic.common.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemStack;

/**
 * Definition of an enchantment that grants bonus rolls when fishing or harvesting crops.  Note
 * that unlike Luck of the Sea, this increases the quantity of items gained, not quality.
 * 
 * @author Daedalus4096
 */
public class BountyEnchantment extends AbstractRuneEnchantment {
    public BountyEnchantment(Enchantment.Rarity rarity, EquipmentSlotType... slots) {
        super(rarity, EnchantmentType.FISHING_ROD, slots);
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 5 + ((enchantmentLevel - 1) * 10);
    }
    
    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 15;
    }
    
    @Override
    public int getMaxLevel() {
        return 4;
    }

    @Override
    public boolean canApply(ItemStack stack) {
        return stack.getItem() instanceof HoeItem || super.canApply(stack);
    }
}
