package com.verdantartifice.primalmagick.common.enchantments;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

/**
 * Definition of an enchantment that grants bonus rolls when fishing or harvesting crops.  Note
 * that unlike Luck of the Sea, this increases the quantity of items gained, not quality.
 * 
 * @author Daedalus4096
 */
public class BountyEnchantment extends AbstractRuneEnchantment {
    public BountyEnchantment(Enchantment.Rarity rarity, EquipmentSlot... slots) {
        super(rarity, EnchantmentCategory.FISHING_ROD, slots);
    }

    @Override
    public int getMinCost(int enchantmentLevel) {
        return 5 + ((enchantmentLevel - 1) * 10);
    }
    
    @Override
    public int getMaxCost(int enchantmentLevel) {
        return this.getMinCost(enchantmentLevel) + 15;
    }
    
    @Override
    public int getMaxLevel() {
        return 4;
    }

    @Override
    public boolean canEnchant(ItemStack stack) {
        return stack.getItem() instanceof HoeItem || super.canEnchant(stack);
    }
}
