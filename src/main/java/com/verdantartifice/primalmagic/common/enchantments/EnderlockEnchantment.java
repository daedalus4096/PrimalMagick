package com.verdantartifice.primalmagic.common.enchantments;

import com.verdantartifice.primalmagic.common.effects.EffectsPM;

import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.effect.MobEffectInstance;

/**
 * Definition of an enchantment that prevents entities from teleporting for a brief time.
 * 
 * @author Daedalus4096
 */
public class EnderlockEnchantment extends AbstractRuneEnchantment {
    public EnderlockEnchantment(Enchantment.Rarity rarity, EquipmentSlot... slots) {
        super(rarity, EnchantmentCategory.WEAPON, slots);
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
        return 5;
    }
    
    @Override
    public boolean canEnchant(ItemStack stack) {
        Item item = stack.getItem();
        return item instanceof TridentItem || item instanceof AxeItem ? true : super.canEnchant(stack);
    }
    
    @Override
    public void doPostAttack(LivingEntity user, Entity target, int level) {
        super.doPostAttack(user, target, level);
        if (target instanceof LivingEntity) {
            int ticks = (40 * level);   // Last two seconds per enchantment level
            ((LivingEntity)target).addEffect(new MobEffectInstance(EffectsPM.ENDERLOCK.get(), ticks, 0));
        }
    }
}
