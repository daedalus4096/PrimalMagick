package com.verdantartifice.primalmagick.common.crafting;

import com.verdantartifice.primalmagick.common.crafting.inputs.RunecarvingRecipeInput;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.NotNull;

/**
 * Crafting recipe interface for a runecarving recipe.  A runecarving recipe is like a stonecutting
 * recipe, but has two ingredients and a research requirement.
 *  
 * @author Daedalus4096
 */
public interface IRunecarvingRecipe extends Recipe<RunecarvingRecipeInput>, IHasRequirement, IHasExpertise {
    @Override
    @NotNull
    default RecipeType<IRunecarvingRecipe> getType() {
        return RecipeTypesPM.RUNECARVING.get();
    }

    @NotNull
    RecipeSerializer<? extends IRunecarvingRecipe> getSerializer();

    @Override
    default boolean isSpecial() {
        // Return true to keep runecarving recipes from showing up in the vanilla recipe book
        return true;
    }

    @Override
    default RecipeBookCategory recipeBookCategory() {
        // FIXME Tie into datapacked recipe book category system
    }
}
