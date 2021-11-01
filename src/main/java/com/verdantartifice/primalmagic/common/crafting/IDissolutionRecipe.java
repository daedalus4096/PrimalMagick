package com.verdantartifice.primalmagic.common.crafting;

import com.verdantartifice.primalmagic.common.blocks.BlocksPM;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

public interface IDissolutionRecipe extends Recipe<Container>, IHasManaCost {
    @Override
    default RecipeType<?> getType() {
        return RecipeTypesPM.DISSOLUTION;
    }
    
    @Override
    default boolean isSpecial() {
        // Return true to keep arcane recipes from showing up in the vanilla recipe book
        return true;
    }
    
    @Override
    default ItemStack getToastSymbol() {
        return new ItemStack(BlocksPM.DISSOLUTION_CHAMBER.get());
    }
}
