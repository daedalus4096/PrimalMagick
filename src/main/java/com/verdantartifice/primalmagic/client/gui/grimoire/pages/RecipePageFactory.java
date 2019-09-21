package com.verdantartifice.primalmagic.client.gui.grimoire.pages;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.common.crafting.ShapedArcaneRecipe;
import com.verdantartifice.primalmagic.common.crafting.ShapelessArcaneRecipe;

import net.minecraft.item.crafting.IRecipe;

public class RecipePageFactory {
    @Nullable
    public static AbstractRecipePage createPage(@Nonnull IRecipe<?> recipe) {
        if (recipe instanceof ShapelessArcaneRecipe) {
            return new ShapelessArcaneRecipePage((ShapelessArcaneRecipe)recipe);
        } else if (recipe instanceof ShapedArcaneRecipe) {
            return new ShapedArcaneRecipePage((ShapedArcaneRecipe)recipe);
        } else {
            return null;
        }
    }
}
