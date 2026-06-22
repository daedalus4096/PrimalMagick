package com.verdantartifice.primalmagick.client.gui.grimoire;

import com.verdantartifice.primalmagick.common.crafting.display.ConcoctingRecipeDisplay;
import com.verdantartifice.primalmagick.common.crafting.display.DissolutionRecipeDisplay;
import com.verdantartifice.primalmagick.common.crafting.display.RitualRecipeDisplay;
import com.verdantartifice.primalmagick.common.crafting.display.RunecarvingRecipeDisplay;
import com.verdantartifice.primalmagick.common.crafting.display.ShapedArcaneCraftingRecipeDisplay;
import com.verdantartifice.primalmagick.common.crafting.display.ShapelessArcaneCraftingRecipeDisplay;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.display.FurnaceRecipeDisplay;
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
    @Nonnull
    public static AbstractRecipePage<?> createPage(@Nonnull RecipeHolder<?> recipeHolder) {
        ResourceKey<Recipe<?>> recipeKey = recipeHolder.id();
        RecipeDisplay display = recipeHolder.value().display().getFirst();
        if (display instanceof FurnaceRecipeDisplay furnaceDisplay) {
            return new SmeltingRecipePage(furnaceDisplay);
        } else if (display instanceof RitualRecipeDisplay ritualDisplay) {
            return new RitualRecipePage(ritualDisplay, recipeKey);
        } else if (display instanceof RunecarvingRecipeDisplay runecarvingDisplay) {
            return new RunecarvingRecipePage(runecarvingDisplay);
        } else if (display instanceof ConcoctingRecipeDisplay concoctingDisplay) {
            return new ConcoctingRecipePage(concoctingDisplay);
        } else if (display instanceof DissolutionRecipeDisplay dissolutionDisplay) {
            return new DissolutionRecipePage(dissolutionDisplay);
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
