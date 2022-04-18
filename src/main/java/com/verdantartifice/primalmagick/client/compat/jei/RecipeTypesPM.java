package com.verdantartifice.primalmagick.client.compat.jei;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.crafting.IArcaneRecipe;

import mezz.jei.api.recipe.RecipeType;

/**
 * List of all the recipe types added by the mod.
 * 
 * @author Daedalus4096
 */
public class RecipeTypesPM {
    public static final RecipeType<IArcaneRecipe> ARCANE_CRAFTING = RecipeType.create(PrimalMagick.MODID, "arcane_workbench", IArcaneRecipe.class);
}
