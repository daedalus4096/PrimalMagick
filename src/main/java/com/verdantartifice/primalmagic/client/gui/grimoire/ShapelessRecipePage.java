package com.verdantartifice.primalmagic.client.gui.grimoire;

import net.minecraft.world.item.crafting.ShapelessRecipe;

/**
 * Grimoire page showing a shapeless vanilla recipe.
 * 
 * @author Daedalus4096
 */
public class ShapelessRecipePage extends AbstractShapelessRecipePage<ShapelessRecipe> {
    public ShapelessRecipePage(ShapelessRecipe recipe) {
        super(recipe);
    }

    @Override
    protected String getRecipeTypeTranslationKey() {
        return "primalmagic.grimoire.shapeless_recipe_header";
    }
}
