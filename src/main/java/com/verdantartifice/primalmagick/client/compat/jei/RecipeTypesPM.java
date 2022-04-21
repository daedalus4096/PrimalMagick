package com.verdantartifice.primalmagick.client.compat.jei;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.crafting.IArcaneRecipe;
import com.verdantartifice.primalmagick.common.crafting.IConcoctingRecipe;
import com.verdantartifice.primalmagick.common.crafting.IDissolutionRecipe;
import com.verdantartifice.primalmagick.common.crafting.IRitualRecipe;
import com.verdantartifice.primalmagick.common.crafting.IRunecarvingRecipe;

import mezz.jei.api.recipe.RecipeType;

/**
 * List of all the recipe types added by the mod.
 * 
 * @author Daedalus4096
 */
public class RecipeTypesPM {
    public static final RecipeType<IArcaneRecipe> ARCANE_CRAFTING = RecipeType.create(PrimalMagick.MODID, "arcane_workbench", IArcaneRecipe.class);
    public static final RecipeType<IConcoctingRecipe> CONCOCTING = RecipeType.create(PrimalMagick.MODID, "concocter", IConcoctingRecipe.class);
    public static final RecipeType<IRunecarvingRecipe> RUNECARVING = RecipeType.create(PrimalMagick.MODID, "runecarving_table", IRunecarvingRecipe.class);
    public static final RecipeType<IDissolutionRecipe> DISSOLUTION = RecipeType.create(PrimalMagick.MODID, "dissolution_chamber", IDissolutionRecipe.class);
    public static final RecipeType<IRitualRecipe> RITUAL = RecipeType.create(PrimalMagick.MODID, "ritual_altar", IRitualRecipe.class);
}
