package com.verdantartifice.primalmagick.common.crafting;

import com.verdantartifice.primalmagick.common.blocks.BlockRegistration;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

public interface IConcoctingRecipe extends Recipe<CraftingInput>, IHasManaCost, IHasRequirement, IArcaneRecipeBookItem {
    public static final int MAX_WIDTH = 3;
    public static final int MAX_HEIGHT = 3;
    
    @Override
    default RecipeType<?> getType() {
        return RecipeTypesPM.CONCOCTING.get();
    }
    
    @Override
    default boolean isSpecial() {
        // Return true to keep arcane recipes from showing up in the vanilla recipe book
        return true;
    }
    
    @Override
    default ItemStack getToastSymbol() {
        return new ItemStack(BlockRegistration.CONCOCTER.get());
    }
}
