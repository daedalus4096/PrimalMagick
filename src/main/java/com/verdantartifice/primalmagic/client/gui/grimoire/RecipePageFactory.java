package com.verdantartifice.primalmagic.client.gui.grimoire;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.common.crafting.RitualRecipe;
import com.verdantartifice.primalmagic.common.crafting.ShapedArcaneRecipe;
import com.verdantartifice.primalmagic.common.crafting.ShapelessArcaneRecipe;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.item.crafting.ShapelessRecipe;

/**
 * Factory class to create an appropriate grimoire recipe page for a given recipe type.
 * 
 * @author Daedalus4096
 */
public class RecipePageFactory {
    @Nullable
    public static AbstractRecipePage createPage(@Nonnull IRecipe<?> recipe) {
        if (recipe instanceof ShapelessArcaneRecipe) {
            return new ShapelessArcaneRecipePage((ShapelessArcaneRecipe)recipe);
        } else if (recipe instanceof ShapedArcaneRecipe) {
            return new ShapedArcaneRecipePage((ShapedArcaneRecipe)recipe);
        } else if (recipe instanceof ShapelessRecipe) {
            return new ShapelessRecipePage((ShapelessRecipe)recipe);
        } else if (recipe instanceof ShapedRecipe) {
            return new ShapedRecipePage((ShapedRecipe)recipe);
        } else if (recipe instanceof RitualRecipe) {
            return new RitualRecipePage((RitualRecipe)recipe);
        } else {
            return null;
        }
    }
}
