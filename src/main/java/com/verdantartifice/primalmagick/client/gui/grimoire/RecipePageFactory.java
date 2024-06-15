package com.verdantartifice.primalmagick.client.gui.grimoire;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagick.common.crafting.IConcoctingRecipe;
import com.verdantartifice.primalmagick.common.crafting.IDissolutionRecipe;
import com.verdantartifice.primalmagick.common.crafting.IRitualRecipe;
import com.verdantartifice.primalmagick.common.crafting.IRunecarvingRecipe;
import com.verdantartifice.primalmagick.common.crafting.IShapedArcaneRecipePM;
import com.verdantartifice.primalmagick.common.crafting.IShapelessArcaneRecipePM;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraftforge.common.crafting.IShapedRecipe;

/**
 * Factory class to create an appropriate grimoire recipe page for a given recipe type.
 * 
 * @author Daedalus4096
 */
public class RecipePageFactory {
    @SuppressWarnings("unchecked")
    @Nonnull
    public static AbstractRecipePage createPage(@Nonnull RecipeHolder<?> recipeHolder, RegistryAccess registryAccess) {
        Recipe<?> recipe = recipeHolder.value();
        if (recipe instanceof SmeltingRecipe) {
            return new SmeltingRecipePage((RecipeHolder<SmeltingRecipe>)recipeHolder, registryAccess);
        } else if (recipe instanceof IRitualRecipe) {
            return new RitualRecipePage((RecipeHolder<IRitualRecipe>)recipeHolder, registryAccess);
        } else if (recipe instanceof IRunecarvingRecipe) {
            return new RunecarvingRecipePage((RecipeHolder<IRunecarvingRecipe>)recipeHolder, registryAccess);
        } else if (recipe instanceof IConcoctingRecipe) {
            return new ConcoctingRecipePage((RecipeHolder<IConcoctingRecipe>)recipeHolder, registryAccess);
        } else if (recipe instanceof IDissolutionRecipe) {
            return new DissolutionRecipePage((RecipeHolder<IDissolutionRecipe>)recipeHolder, registryAccess);
        } else if (recipe instanceof IShapedArcaneRecipePM) {
            return new ShapedArcaneRecipePage((RecipeHolder<IShapedArcaneRecipePM>)recipeHolder, registryAccess);
        } else if (recipe instanceof IShapelessArcaneRecipePM) {
            return new ShapelessArcaneRecipePage((RecipeHolder<IShapelessArcaneRecipePM>)recipeHolder, registryAccess);
        } else if (recipe instanceof IShapedRecipe) {
            return new ShapedRecipePage((RecipeHolder<IShapedRecipe<?>>)recipeHolder, registryAccess);
        } else {
            return new ShapelessRecipePage(recipeHolder, registryAccess);
        }
    }
}
