package com.verdantartifice.primalmagick.common.crafting;

import com.verdantartifice.primalmagick.common.blocks.BlocksPM;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

public interface IDissolutionRecipe extends Recipe<Container>, IHasManaCost, IArcaneRecipeBookItem {
    @Override
    default RecipeType<?> getType() {
        return RecipeTypesPM.DISSOLUTION.get();
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
