package com.verdantartifice.primalmagick.common.enchantments;

import com.verdantartifice.primalmagick.common.effects.EffectsPM;

import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

/**
 * Definition of an enchantment that applies a stacking bleed DoT to the target.
 * 
 * @author Daedalus4096
 */
public class RendingEnchantment extends AbstractRuneEnchantment {
    public RendingEnchantment(Enchantment.Rarity rarity, EquipmentSlot... slots) {
        super(rarity, EnchantmentCategory.TRIDENT, slots);
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
        Item item = stack.getItem();
        return item instanceof SwordItem || item instanceof AxeItem ? true : super.canEnchant(stack);
    }

    @Override
    public void doPostAttack(LivingEntity user, Entity target, int level) {
        super.doPostAttack(user, target, level);
        if (target instanceof LivingEntity) {
            LivingEntity livingTarget = (LivingEntity)target;
            MobEffectInstance effectInstance = livingTarget.getEffect(EffectsPM.BLEEDING.get());
            int amplifier = (effectInstance == null) ? 0 : Mth.clamp(1 + effectInstance.getAmplifier(), 0, level - 1);
            livingTarget.addEffect(new MobEffectInstance(EffectsPM.BLEEDING.get(), 120, amplifier));
        }
    }
}
