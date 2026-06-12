package com.verdantartifice.primalmagick.common.crafting;

import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.List;

/**
 * Crafting recipe interface for a ritual recipe.  Ritual recipes are performed across multiple
 * blocks, with a central altar surrounded by multiple offering pedestals and props.  They also
 * have research requirements and optional mana costs.
 * 
 * @author Daedalus4096
 */
public interface IRitualRecipe extends Recipe<CraftingInput>, IHasManaCost, IHasRequirement, IHasExpertise {
    /**
     * Get the list of ingredients for the recipe.
     *
     * @return the ingredient list for the recipe
     */
    List<Ingredient> getIngredients();

    /**
     * Get the list of props for the recipe.
     * 
     * @return the prop list for the recipe
     */
    List<BlockIngredient> getProps();
    
    /**
     * Get the instability rating of the recipe.
     * 
     * @return the instability rating of the recipe
     */
    int getInstability();
    
    @Override
    default RecipeType<? extends RitualRecipe> getType() {
        return RecipeTypesPM.RITUAL.get();
    }
    
    @Override
    default boolean isSpecial() {
        // Return true to keep ritual recipes from showing up in the vanilla recipe book
        return true;
    }
}
