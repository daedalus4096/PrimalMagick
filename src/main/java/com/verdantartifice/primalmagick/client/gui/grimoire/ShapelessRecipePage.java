package com.verdantartifice.primalmagick.client.gui.grimoire;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;

/**
 * Grimoire page showing a shapeless vanilla recipe.
 * 
 * @author Daedalus4096
 */
public class ShapelessRecipePage extends AbstractShapelessRecipePage<Recipe<?>> {
    public ShapelessRecipePage(RecipeHolder<?> recipe, RegistryAccess registryAccess) {
        super(recipe, registryAccess);
    }

    @Override
    protected String getRecipeTypeTranslationKey() {
        return "grimoire.primalmagick.shapeless_recipe_header";
    }
}
