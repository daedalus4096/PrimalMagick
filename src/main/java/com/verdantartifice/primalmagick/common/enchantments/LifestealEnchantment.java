package com.verdantartifice.primalmagick.common.enchantments;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

/**
 * Definition of an enchantment that steals life from entities wounded by this weapon.  Gives a
 * twenty percent chance per level to heal one point.
 * 
 * @author Daedalus4096
 */
public class LifestealEnchantment extends AbstractRuneEnchantment {
    protected static final Map<Entity, Long> LAST_LEECHED = new HashMap<>();
    
    public LifestealEnchantment(Enchantment.Rarity rarity, EquipmentSlot... slots) {
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
        
        // Disallow leeching from the same entity multiple times in one tick to prevent bigger than intended heals
        long ticks = user.level().getGameTime();
        if (target instanceof LivingEntity && user.getRandom().nextInt(5) < level && LAST_LEECHED.getOrDefault(target, 0L) != ticks) {
            user.heal(1.0F);
            LAST_LEECHED.put(target, ticks);
        }
    }
}
