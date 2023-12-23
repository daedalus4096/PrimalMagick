package com.verdantartifice.primalmagick.common.menus.base;

import java.util.List;

import com.verdantartifice.primalmagick.client.recipe_book.ArcaneRecipeBookCategories;
import com.verdantartifice.primalmagick.common.crafting.recipe_book.ArcaneRecipeBookType;
import com.verdantartifice.primalmagick.common.crafting.recipe_book.ServerPlaceArcaneRecipe;

import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;

/**
 * Interface for a menu that's compatible with the arcane recipe book.
 * 
 * @author Daedalus4096
 */
public interface IArcaneRecipeBookMenu<C extends Container> {
    @SuppressWarnings("unchecked")
    default void handlePlacement(boolean shiftDown, RecipeHolder<?> recipe, ServerPlayer player) {
        new ServerPlaceArcaneRecipe<C>(this).recipeClicked(player, (RecipeHolder<? extends Recipe<C>>)recipe, shiftDown);
    }
    
    void fillCraftSlotsStackedContents(StackedContents contents);

    void clearCraftingContent();

    boolean recipeMatches(RecipeHolder<? extends Recipe<? super C>> recipe);

    int getResultSlotIndex();

    int getGridWidth();

    int getGridHeight();
    
    NonNullList<Slot> getSlots();
    
    Slot getSlot(int slotId);

    int getSize();
    
    default List<ArcaneRecipeBookCategories> getRecipeBookCategories() {
        return ArcaneRecipeBookCategories.getCategories(this.getRecipeBookType());
    }
    
    ArcaneRecipeBookType getRecipeBookType();

    boolean shouldMoveToInventory(int index);
    
    default boolean isSingleIngredientMenu() {
        return false;
    }
}
