package com.verdantartifice.primalmagic.common.enchantments;

import net.minecraft.enchantment.DamageEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;

/**
 * Definition of a rune enchantment that combines the effects of Sharpness and Smite.
 * 
 * @author Daedalus4096
 */
public class JudgmentEnchantment extends DamageEnchantment {
    public JudgmentEnchantment(Enchantment.Rarity rarity, EquipmentSlotType... slotTypes) {
        super(rarity, 0, slotTypes);
    }
    
    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 10 + ((enchantmentLevel - 1) * 10);
    }
    
    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 25;
    }
    
    @Override
    public int getMaxLevel() {
        return 5;
    }
    
    @Override
    public float calcDamageByCreature(int level, CreatureAttribute creatureType) {
        float damage = 1.0F + (float)Math.max(0, level - 1) * 0.5F;
        if (creatureType == CreatureAttribute.UNDEAD) {
            damage += ((float)level * 2.5F);
        }
        return damage;
    }
    
    @Override
    public boolean canApply(ItemStack stack) {
        Item item = stack.getItem();
        return item instanceof TridentItem || item instanceof AxeItem ? true : stack.canApplyAtEnchantingTable(this);
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
