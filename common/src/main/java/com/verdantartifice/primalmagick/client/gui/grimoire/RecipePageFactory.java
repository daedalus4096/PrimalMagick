package com.verdantartifice.primalmagick.client.gui.grimoire;

import com.verdantartifice.primalmagick.common.crafting.IDissolutionRecipe;
import com.verdantartifice.primalmagick.common.crafting.IRitualRecipe;
import com.verdantartifice.primalmagick.common.crafting.IRunecarvingRecipe;
import com.verdantartifice.primalmagick.common.crafting.display.ConcoctingRecipeDisplay;
import com.verdantartifice.primalmagick.common.crafting.display.ShapedArcaneCraftingRecipeDisplay;
import com.verdantartifice.primalmagick.common.crafting.display.ShapelessArcaneCraftingRecipeDisplay;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapedCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapelessCraftingRecipeDisplay;

import javax.annotation.Nonnull;

/**
 * Factory class to create an appropriate grimoire recipe page for a given recipe type.
 * 
 * @author Daedalus4096
 */
public class RecipePageFactory {
    @SuppressWarnings("unchecked")
    @Nonnull
    public static AbstractRecipePage<?> createPage(@Nonnull RecipeHolder<?> recipeHolder, RegistryAccess registryAccess) {
        ResourceKey<Recipe<?>> recipeKey = recipeHolder.id();
        Recipe<?> recipe = recipeHolder.value();
        RecipeDisplay display = recipe.display().getFirst();
        if (recipe instanceof SmeltingRecipe) {
            return new SmeltingRecipePage((RecipeHolder<SmeltingRecipe>)recipeHolder, registryAccess);
        } else if (recipe instanceof IRitualRecipe) {
            return new RitualRecipePage((RecipeHolder<IRitualRecipe>)recipeHolder, registryAccess);
        } else if (recipe instanceof IRunecarvingRecipe) {
            return new RunecarvingRecipePage((RecipeHolder<IRunecarvingRecipe>)recipeHolder, registryAccess);
        } else if (display instanceof ConcoctingRecipeDisplay concoctingDisplay) {
            return new ConcoctingRecipePage(concoctingDisplay);
        } else if (recipe instanceof IDissolutionRecipe) {
            return new DissolutionRecipePage((RecipeHolder<IDissolutionRecipe>)recipeHolder, registryAccess);
        } else if (display instanceof ShapedArcaneCraftingRecipeDisplay shapedArcaneDisplay) {
            return new ShapedArcaneRecipePage(shapedArcaneDisplay, recipeKey);
        } else if (display instanceof ShapelessArcaneCraftingRecipeDisplay shapelessArcaneDisplay) {
            return new ShapelessArcaneRecipePage(shapelessArcaneDisplay, recipeKey);
        } else if (display instanceof ShapedCraftingRecipeDisplay shapedDisplay) {
            return new ShapedRecipePage(shapedDisplay);
        } else if (display instanceof ShapelessCraftingRecipeDisplay shapelessDisplay) {
            return new ShapelessRecipePage(shapelessDisplay);
        } else {
            throw new IllegalArgumentException("Unexpected recipe type in RecipePageFactory");
        }
    }
}
