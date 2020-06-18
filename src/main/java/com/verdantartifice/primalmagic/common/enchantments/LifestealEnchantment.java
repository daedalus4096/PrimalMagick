package com.verdantartifice.primalmagic.common.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;

/**
 * Definition of an enchantment that steals life from entities wounded by this weapon.  Gives a
 * twenty percent chance per level to heal one point.
 * 
 * @author Daedalus4096
 */
public class LifestealEnchantment extends AbstractRuneEnchantment {
    public LifestealEnchantment(Enchantment.Rarity rarity, EquipmentSlotType... slots) {
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
        return 5;
    }
    
    @Override
    public boolean canApply(ItemStack stack) {
        Item item = stack.getItem();
        return item instanceof TridentItem || item instanceof AxeItem ? true : super.canApply(stack);
    }
    
    @Override
    public void onEntityDamaged(LivingEntity user, Entity target, int level) {
        super.onEntityDamaged(user, target, level);
        if (target instanceof LivingEntity && user.getRNG().nextInt(5) < level) {
            user.heal(1.0F);
        }
    }
}
