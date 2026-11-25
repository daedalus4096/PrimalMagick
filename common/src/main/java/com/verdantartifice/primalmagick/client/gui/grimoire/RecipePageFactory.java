package com.verdantartifice.primalmagick.client.gui.grimoire;

import com.verdantartifice.primalmagick.common.crafting.display.ConcoctingRecipeDisplay;
import com.verdantartifice.primalmagick.common.crafting.display.DissolutionRecipeDisplay;
import com.verdantartifice.primalmagick.common.crafting.display.RitualRecipeDisplay;
import com.verdantartifice.primalmagick.common.crafting.display.RunecarvingRecipeDisplay;
import com.verdantartifice.primalmagick.common.crafting.display.ShapedArcaneCraftingRecipeDisplay;
import com.verdantartifice.primalmagick.common.crafting.display.ShapelessArcaneCraftingRecipeDisplay;
import com.verdantartifice.primalmagick.common.util.RecipeUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.display.FurnaceRecipeDisplay;
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
        return RecipeUtils.getFirstDisplay(recipeHolder).map(display -> switch (display) {
            case FurnaceRecipeDisplay furnaceDisplay -> new SmeltingRecipePage(furnaceDisplay);
            case RitualRecipeDisplay ritualDisplay -> new RitualRecipePage(ritualDisplay, recipeKey);
            case RunecarvingRecipeDisplay runecarvingDisplay -> new RunecarvingRecipePage(runecarvingDisplay);
            case ConcoctingRecipeDisplay concoctingDisplay -> new ConcoctingRecipePage(concoctingDisplay);
            case DissolutionRecipeDisplay dissolutionDisplay -> new DissolutionRecipePage(dissolutionDisplay);
            case ShapedArcaneCraftingRecipeDisplay shapedArcaneDisplay -> new ShapedArcaneRecipePage(shapedArcaneDisplay, recipeKey);
            case ShapelessArcaneCraftingRecipeDisplay shapelessArcaneDisplay -> new ShapelessArcaneRecipePage(shapelessArcaneDisplay, recipeKey);
            case ShapedCraftingRecipeDisplay shapedDisplay -> new ShapedRecipePage(shapedDisplay);
            case ShapelessCraftingRecipeDisplay shapelessDisplay -> new ShapelessRecipePage(shapelessDisplay);
            default -> throw new IllegalArgumentException("Unexpected recipe type in RecipePageFactory");
        }).orElseThrow(() -> new IllegalArgumentException("Failed to get recipe display in RecipePageFactory"));
    }
}
