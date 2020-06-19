package com.verdantartifice.primalmagic.common.enchantments;

import com.verdantartifice.primalmagic.common.effects.EffectsPM;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import net.minecraft.potion.EffectInstance;

/**
 * Definition of an enchantment that prevents entities from teleporting for a brief time.
 * 
 * @author Daedalus4096
 */
public class EnderlockEnchantment extends AbstractRuneEnchantment {
    public EnderlockEnchantment(Enchantment.Rarity rarity, EquipmentSlotType... slots) {
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
        if (target instanceof LivingEntity) {
            int ticks = (40 * level);   // Last two seconds per enchantment level
            ((LivingEntity)target).addPotionEffect(new EffectInstance(EffectsPM.ENDERLOCK.get(), ticks, 0));
        }
    }
}
