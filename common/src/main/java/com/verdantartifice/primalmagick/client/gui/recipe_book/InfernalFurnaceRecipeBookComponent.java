package com.verdantartifice.primalmagick.client.gui.recipe_book;

import com.verdantartifice.primalmagick.common.menus.InfernalFurnaceMenu;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.recipebook.GhostSlots;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import net.minecraft.client.gui.screens.recipebook.SearchRecipeBookCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.display.FurnaceRecipeDisplay;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Recipe book component for the infernal furnace GUI.
 *
 * @author Daedalus4096
 */
public class InfernalFurnaceRecipeBookComponent extends RecipeBookComponent<InfernalFurnaceMenu> {
    private static final WidgetSprites FILTER_SPRITES = new WidgetSprites(
            Identifier.withDefaultNamespace("recipe_book/furnace_filter_enabled"),
            Identifier.withDefaultNamespace("recipe_book/furnace_filter_disabled"),
            Identifier.withDefaultNamespace("recipe_book/furnace_filter_enabled_highlighted"),
            Identifier.withDefaultNamespace("recipe_book/furnace_filter_disabled_highlighted"));
    private static final Component FILTER_NAME = Component.translatable("gui.recipebook.toggleRecipes.smeltable");
    private static final List<RecipeBookComponent.TabInfo> TABS = List.of(
            new RecipeBookComponent.TabInfo(SearchRecipeBookCategory.FURNACE),
            new RecipeBookComponent.TabInfo(Items.PORKCHOP, RecipeBookCategories.FURNACE_FOOD),
            new RecipeBookComponent.TabInfo(Items.STONE, RecipeBookCategories.FURNACE_BLOCKS),
            new RecipeBookComponent.TabInfo(Items.LAVA_BUCKET, Items.EMERALD, RecipeBookCategories.FURNACE_MISC)
    );

    public InfernalFurnaceRecipeBookComponent(InfernalFurnaceMenu menu) {
        super(menu, TABS);
    }

    @Override
    @NotNull
    protected WidgetSprites getFilterButtonTextures() {
        return FILTER_SPRITES;
    }

    @Override
    protected boolean isCraftingSlot(Slot slot) {
        return switch (slot.index) {
            case 0, 1, 2 -> true;
            default -> false;
        };
    }

    @Override
    protected void selectMatchingRecipes(RecipeCollection recipeCollection, @NotNull StackedItemContents stackedItemContents) {
        recipeCollection.selectRecipes(stackedItemContents, display -> display instanceof FurnaceRecipeDisplay);
    }

    @Override
    @NotNull
    protected Component getRecipeFilterName() {
        return FILTER_NAME;
    }

    @Override
    protected void fillGhostRecipe(@NotNull GhostSlots ghostSlots, @NotNull RecipeDisplay recipe, @NotNull ContextMap contextMap) {
        ghostSlots.setResult(this.menu.getResultSlot(), contextMap, recipe.result());
        if (recipe instanceof FurnaceRecipeDisplay furnaceRecipe) {
            ghostSlots.setInput(this.menu.getInputGridSlots().getFirst(), contextMap, furnaceRecipe.ingredient());
        }
    }
}
