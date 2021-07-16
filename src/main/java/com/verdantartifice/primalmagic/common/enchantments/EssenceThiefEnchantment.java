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

    @Override
    public void onEntityDamaged(LivingEntity user, Entity target, int level) {
        super.onEntityDamaged(user, target, level);
        if (target instanceof LivingEntity) {
            ((LivingEntity)target).addPotionEffect(new EffectInstance(EffectsPM.STOLEN_ESSENCE.get(), 200, Math.max(0, level - 1)));
        }
    }
}
