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
import net.minecraft.item.SwordItem;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.MathHelper;

/**
 * Definition of an enchantment that applies a stacking bleed DoT to the target.
 * 
 * @author Daedalus4096
 */
public class RendingEnchantment extends AbstractRuneEnchantment {
    public RendingEnchantment(Enchantment.Rarity rarity, EquipmentSlotType... slots) {
        super(rarity, EnchantmentType.TRIDENT, slots);
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
        return item instanceof SwordItem || item instanceof AxeItem ? true : super.canApply(stack);
    }

    @Override
    public void onEntityDamaged(LivingEntity user, Entity target, int level) {
        super.onEntityDamaged(user, target, level);
        if (target instanceof LivingEntity) {
            LivingEntity livingTarget = (LivingEntity)target;
            EffectInstance effectInstance = livingTarget.getActivePotionEffect(EffectsPM.BLEEDING.get());
            int amplifier = (effectInstance == null) ? 0 : MathHelper.clamp(1 + effectInstance.getAmplifier(), 0, level - 1);
            livingTarget.addPotionEffect(new EffectInstance(EffectsPM.BLEEDING.get(), 120, amplifier));
        }
    }
}
