package com.verdantartifice.primalmagick.client.gui.grimoire;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraftforge.common.crafting.IShapedRecipe;

/**
 * Grimoire page showing a shaped vanilla recipe.
 * 
 * @author Daedalus4096
 */
public class ShapedRecipePage extends AbstractShapedRecipePage<IShapedRecipe<?>> {
    public ShapedRecipePage(RecipeHolder<? extends IShapedRecipe<?>> recipe, RegistryAccess registryAccess) {
        super(recipe, registryAccess);
    }

    @Override
    protected String getRecipeTypeTranslationKey() {
        return "grimoire.primalmagick.shaped_recipe_header";
    }
}
