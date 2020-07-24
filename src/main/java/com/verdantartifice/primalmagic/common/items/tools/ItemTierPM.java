package com.verdantartifice.primalmagic.common.items.tools;

import java.util.function.Supplier;

import net.minecraft.item.IItemTier;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;

import com.verdantartifice.primalmagic.common.misc.HarvestLevel;
import com.verdantartifice.primalmagic.common.tags.ItemTagsPM;

/**
 * Definition of tiered item materials for the mod.
 * 
 * @author Daedalus4096
 */
public enum ItemTierPM implements IItemTier {
    PRIMALITE(HarvestLevel.IRON.getLevel(), 800, 7.5F, 2.5F, 18, () -> {
        return Ingredient.fromTag(ItemTagsPM.INGOTS_PRIMALITE);
    }),
    HEXIUM(HarvestLevel.DIAMOND.getLevel(), 1600, 9.5F, 4.0F, 23, () -> {
        return Ingredient.fromTag(ItemTagsPM.INGOTS_HEXIUM);
    }),
    HALLOWSTEEL(HarvestLevel.HALLOWSTEEL.getLevel(), 2400, 11.5F, 5.5F, 28, () -> {
        return Ingredient.fromTag(ItemTagsPM.INGOTS_HALLOWSTEEL);
    });

    private final int harvestLevel;
    private final int maxUses;
    private final float efficiency;
    private final float attackDamage;
    private final int enchantability;
    private final LazyValue<Ingredient> repairMaterial;

    private ItemTierPM(int harvestLevel, int maxUses, float efficiency, float attackDamage, int enchantability, Supplier<Ingredient> repairMaterial) {
        this.harvestLevel = harvestLevel;
        this.maxUses = maxUses;
        this.efficiency = efficiency;
        this.attackDamage = attackDamage;
        this.enchantability = enchantability;
        this.repairMaterial = new LazyValue<>(repairMaterial);
    }

    @Override
    public int getMaxUses() {
        return this.maxUses;
    }

    @Override
    public float getEfficiency() {
        return this.efficiency;
    }

    @Override
    public float getAttackDamage() {
        return this.attackDamage;
    }

    @Override
    public int getHarvestLevel() {
        return this.harvestLevel;
    }

    @Override
    public int getEnchantability() {
        return this.enchantability;
    }

    @Override
    public Ingredient getRepairMaterial() {
        return this.repairMaterial.getValue();
    }
}
