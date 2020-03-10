package com.verdantartifice.primalmagic.client.gui.grimoire;

import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Grimoire page showing a shapeless vanilla recipe.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
public class ShapelessRecipePage extends AbstractShapelessRecipePage<ShapelessRecipe> {
    public ShapelessRecipePage(ShapelessRecipe recipe) {
        super(recipe);
    }

    @Override
    protected String getTitleTranslationKey() {
        return "primalmagic.grimoire.shapeless_recipe_header";
    }
}
