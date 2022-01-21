package com.verdantartifice.primalmagick.client.compat.jei;

import java.util.List;

import com.verdantartifice.primalmagick.common.crafting.IArcaneRecipe;
import com.verdantartifice.primalmagick.common.crafting.RecipeTypesPM;

import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;

/**
 * Helper class to fetch which recipes belong to each recipe category.
 * 
 * @author Daedalus4096
 */
public class CategoryRecipes {
    private final RecipeManager recipeManager;
    
    public CategoryRecipes() {
        Minecraft mc = Minecraft.getInstance();
        if (mc == null) {
            throw new IllegalStateException("Minecraft instance not initialized");
        }
        ClientLevel level = mc.level;
        if (level == null) {
            throw new IllegalStateException("Client level instance not initialized");
        }
        this.recipeManager = level.getRecipeManager();
        if (this.recipeManager == null) {
            throw new IllegalStateException("Recipe manager instance not initialized");
        }
    }
    
    public List<IArcaneRecipe> getArcaneRecipes(IRecipeCategory<IArcaneRecipe> category) {
        CategoryRecipeValidatorPM<IArcaneRecipe> validator = new CategoryRecipeValidatorPM<>(category, 9, true);
        return getValidHandledRecipes(this.recipeManager, RecipeTypesPM.ARCANE_CRAFTING, validator);
    }
    
    private static <C extends Container, T extends Recipe<C>> List<T> getValidHandledRecipes(RecipeManager recipeManager, RecipeType<T> recipeType, CategoryRecipeValidatorPM<T> validator) {
        return recipeManager.getAllRecipesFor(recipeType).stream().filter(r -> validator.isRecipeValid(r) && validator.isRecipeHandled(r)).toList();
    }
}
