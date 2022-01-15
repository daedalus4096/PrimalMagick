package com.verdantartifice.primalmagick.common.compat.jei;

import java.util.List;

import com.verdantartifice.primalmagick.common.crafting.ShapedArcaneRecipe;

import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.item.crafting.RecipeManager;

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
    
    public List<ShapedArcaneRecipe> getShapedArcaneRecipes(IRecipeCategory<ShapedArcaneRecipe> category) {
        // TODO Stub
//        CategoryRecipeValidator<ShapedArcaneRecipe> validator = new CategoryRecipeValidator<>(category, 9);
        return null;
    }
}
