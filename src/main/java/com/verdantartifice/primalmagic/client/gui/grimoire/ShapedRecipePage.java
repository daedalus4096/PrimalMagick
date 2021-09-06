package com.verdantartifice.primalmagic.client.gui.grimoire;

import net.minecraft.world.item.crafting.ShapedRecipe;

/**
 * Grimoire page showing a shaped vanilla recipe.
 * 
 * @author Daedalus4096
 */
public class ShapedRecipePage extends AbstractShapedRecipePage<ShapedRecipe> {
    public ShapedRecipePage(ShapedRecipe recipe) {
        super(recipe);
    }

    @Override
    protected String getRecipeTypeTranslationKey() {
        return "primalmagic.grimoire.shaped_recipe_header";
    }
}
