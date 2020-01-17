package com.verdantartifice.primalmagic.client.gui.grimoire.pages;

import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Grimoire page showing a shaped vanilla recipe.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
public class ShapedRecipePage extends AbstractShapedRecipePage<ShapedRecipe> {
    public ShapedRecipePage(ShapedRecipe recipe) {
        super(recipe);
    }

    @Override
    protected String getTitleTranslationKey() {
        return "primalmagic.grimoire.shaped_recipe_header";
    }
}
