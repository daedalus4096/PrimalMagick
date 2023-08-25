package com.verdantartifice.primalmagick.client.gui.grimoire;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.crafting.ShapedRecipe;

/**
 * Grimoire page showing a shaped vanilla recipe.
 * 
 * @author Daedalus4096
 */
public class ShapedRecipePage extends AbstractShapedRecipePage<ShapedRecipe> {
    public ShapedRecipePage(ShapedRecipe recipe, RegistryAccess registryAccess) {
        super(recipe, registryAccess);
    }

    @Override
    protected String getRecipeTypeTranslationKey() {
        return "grimoire.primalmagick.shaped_recipe_header";
    }
}
