package com.verdantartifice.primalmagick.common.items.tools;

import com.verdantartifice.primalmagick.common.tags.ItemTagsPM;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

/**
 * Definition of tiered item materials for the mod.
 * 
 * @author Daedalus4096
 */
public enum ItemTierPM implements Tier {
    PRIMALITE(BlockTags.INCORRECT_FOR_IRON_TOOL, 800, 7.5F, 2.5F, 18, () -> {
        return Ingredient.of(ItemTagsPM.INGOTS_PRIMALITE);
    }),
    HEXIUM(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 1600, 9.5F, 4.0F, 23, () -> {
        return Ingredient.of(ItemTagsPM.INGOTS_HEXIUM);
    }),
    HALLOWSTEEL(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 2400, 11.5F, 5.5F, 28, () -> {
        return Ingredient.of(ItemTagsPM.INGOTS_HALLOWSTEEL);
    });

    private final TagKey<Block> incorrectBlocks;
    private final int maxUses;
    private final float efficiency;
    private final float attackDamage;
    private final int enchantability;
    private final Supplier<Ingredient> repairMaterial;

    private ItemTierPM(TagKey<Block> incorrectBlocks, int maxUses, float efficiency, float attackDamage, int enchantability, Supplier<Ingredient> repairMaterial) {
        this.incorrectBlocks = incorrectBlocks;
        this.maxUses = maxUses;
        this.efficiency = efficiency;
        this.attackDamage = attackDamage;
        this.enchantability = enchantability;
        this.repairMaterial = repairMaterial;
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
    public int getEnchantmentValue() {
        return this.enchantability;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairMaterial.get();
    }

    @Override
    public TagKey<Block> getIncorrectBlocksForDrops() {
        return this.incorrectBlocks;
    }
}
