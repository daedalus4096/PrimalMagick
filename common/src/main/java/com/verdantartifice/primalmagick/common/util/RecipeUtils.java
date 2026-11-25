package com.verdantartifice.primalmagick.common.util;

import net.minecraft.util.context.ContextMap;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplayContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

/**
 * Collection of utility methods pertaining to recipes.
 *
 * @author Daedalus4096
 */
public class RecipeUtils {
    @NotNull
    public static Optional<RecipeDisplay> getFirstDisplay(RecipeHolder<?> recipeHolder) {
        List<RecipeDisplay> displayList = recipeHolder.value().display();
        return displayList.isEmpty() ? Optional.empty() : Optional.of(displayList.getFirst());
    }

    @NotNull
    public static ItemStack getOutputStack(RecipeHolder<?> recipeHolder, Level level) {
        return level == null ? ItemStack.EMPTY : getOutputStack(recipeHolder, SlotDisplayContext.fromLevel(level));
    }

    @NotNull
    public static ItemStack getOutputStack(RecipeHolder<?> recipeHolder, ContextMap contextMap) {
        if (contextMap == null) {
            return ItemStack.EMPTY;
        } else {
            return getFirstDisplay(recipeHolder).map(display -> display.result().resolveForFirstStack(contextMap)).orElse(ItemStack.EMPTY);
        }
    }
}
