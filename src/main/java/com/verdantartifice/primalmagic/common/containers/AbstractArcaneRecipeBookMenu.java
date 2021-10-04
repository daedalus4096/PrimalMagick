package com.verdantartifice.primalmagic.common.containers;

import java.util.List;

import com.verdantartifice.primalmagic.client.gui.recipe_book.ArcaneRecipeBookCategories;
import com.verdantartifice.primalmagic.common.crafting.recipe_book.ArcaneRecipeBookType;
import com.verdantartifice.primalmagic.common.crafting.recipe_book.ServerPlaceArcaneRecipe;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.crafting.Recipe;

/**
 * Base definition for a menu that's compatible with the arcane recipe book.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractArcaneRecipeBookMenu<C extends Container> extends AbstractContainerMenu {
    public AbstractArcaneRecipeBookMenu(MenuType<?> type, int windowId) {
        super(type, windowId);
    }

    public void handlePlacement(boolean shiftDown, Recipe<C> recipe, ServerPlayer player) {
        new ServerPlaceArcaneRecipe<C>(this).recipeClicked(player, recipe, shiftDown);
    }
    
    public abstract void fillCraftSlotsStackedContents(StackedContents contents);

    public abstract void clearCraftingContent();

    public abstract boolean recipeMatches(Recipe<? super C> recipe);

    public abstract int getResultSlotIndex();

    public abstract int getGridWidth();

    public abstract int getGridHeight();

    public abstract int getSize();
    
    public List<ArcaneRecipeBookCategories> getRecipeBookCategories() {
        return ArcaneRecipeBookCategories.getCategories(this.getRecipeBookType());
    }
    
    public abstract ArcaneRecipeBookType getRecipeBookType();

    public abstract boolean shouldMoveToInventory(int index);
}
