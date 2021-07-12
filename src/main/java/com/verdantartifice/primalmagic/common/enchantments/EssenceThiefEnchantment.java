package com.verdantartifice.primalmagic.common.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;

/**
 * Definition of an enchantment that causes mobs to drop a sample of their essence when killed.
 * 
 * @author Daedalus4096
 */
public class EssenceThiefEnchantment extends AbstractRuneEnchantment {
    public EssenceThiefEnchantment(Enchantment.Rarity rarity, EquipmentSlotType... slots) {
        super(rarity, EnchantmentType.WEAPON, slots);
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
        Item item = stack.getItem();
        return item instanceof TridentItem || item instanceof AxeItem ? true : super.canApply(stack);
    }
}
