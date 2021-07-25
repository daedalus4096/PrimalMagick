package com.verdantartifice.primalmagic.common.items.tools;

import java.util.function.Supplier;

import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.util.LazyLoadedValue;

import com.verdantartifice.primalmagic.common.misc.HarvestLevel;
import com.verdantartifice.primalmagic.common.tags.ItemTagsPM;

/**
 * Definition of tiered item materials for the mod.
 * 
 * @author Daedalus4096
 */
public enum ItemTierPM implements Tier {
    PRIMALITE(HarvestLevel.IRON.getLevel(), 800, 7.5F, 2.5F, 18, () -> {
        return Ingredient.of(ItemTagsPM.INGOTS_PRIMALITE);
    }),
    HEXIUM(HarvestLevel.DIAMOND.getLevel(), 1600, 9.5F, 4.0F, 23, () -> {
        return Ingredient.of(ItemTagsPM.INGOTS_HEXIUM);
    }),
    HALLOWSTEEL(HarvestLevel.HALLOWSTEEL.getLevel(), 2400, 11.5F, 5.5F, 28, () -> {
        return Ingredient.of(ItemTagsPM.INGOTS_HALLOWSTEEL);
    });

    private final int harvestLevel;
    private final int maxUses;
    private final float efficiency;
    private final float attackDamage;
    private final int enchantability;
    private final LazyLoadedValue<Ingredient> repairMaterial;

    private ItemTierPM(int harvestLevel, int maxUses, float efficiency, float attackDamage, int enchantability, Supplier<Ingredient> repairMaterial) {
        this.harvestLevel = harvestLevel;
        this.maxUses = maxUses;
        this.efficiency = efficiency;
        this.attackDamage = attackDamage;
        this.enchantability = enchantability;
        this.repairMaterial = new LazyLoadedValue<>(repairMaterial);
    }

    @Override
    public int getUses() {
        return this.maxUses;
    }

    @Override
    public float getSpeed() {
        return this.efficiency;
    }

    @Override
    public float getAttackDamageBonus() {
        return this.attackDamage;
    }

    @Override
    public int getLevel() {
        return this.harvestLevel;
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantability;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairMaterial.get();
    }
}
