package com.verdantartifice.primalmagick.common.enchantments;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.enchantment.DamageEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;

/**
 * Definition of a rune enchantment that combines the effects of Sharpness and Smite.
 * 
 * @author Daedalus4096
 */
public class JudgmentEnchantment extends DamageEnchantment {
    public JudgmentEnchantment(Enchantment.Rarity rarity, EquipmentSlot... slotTypes) {
        super(rarity, 0, slotTypes);
    }
    
    @Override
    public int getMinCost(int enchantmentLevel) {
        return 10 + ((enchantmentLevel - 1) * 10);
    }
    
    @Override
    public int getMaxCost(int enchantmentLevel) {
        return this.getMinCost(enchantmentLevel) + 25;
    }
    
    @Override
    public int getMaxLevel() {
        return 5;
    }
    
    @Override
    public float getDamageBonus(int level, MobType creatureType) {
        float damage = 1.0F + (float)Math.max(0, level - 1) * 0.5F;
        if (creatureType == MobType.UNDEAD) {
            damage += ((float)level * 2.5F);
        }
        return damage;
    }
    
    @Override
    public boolean canEnchant(ItemStack stack) {
        Item item = stack.getItem();
        return item instanceof TridentItem || item instanceof AxeItem ? true : stack.canApplyAtEnchantingTable(this);
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
