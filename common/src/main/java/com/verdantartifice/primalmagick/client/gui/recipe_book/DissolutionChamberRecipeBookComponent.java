package com.verdantartifice.primalmagick.client.gui.recipe_book;

import com.verdantartifice.primalmagick.common.crafting.display.DissolutionRecipeDisplay;
import com.verdantartifice.primalmagick.common.crafting.recipe_book.RecipeBookCategoriesPM;
import com.verdantartifice.primalmagick.common.menus.DissolutionChamberMenu;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.recipebook.GhostSlots;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Recipe book component for the dissolution chamber GUI.
 *
 * @author Daedalus4096
 */
public class DissolutionChamberRecipeBookComponent extends RecipeBookComponent<DissolutionChamberMenu> {
    private static final WidgetSprites FILTER_SPRITES = new WidgetSprites(
            Identifier.withDefaultNamespace("recipe_book/furnace_filter_enabled"),
            Identifier.withDefaultNamespace("recipe_book/furnace_filter_disabled"),
            Identifier.withDefaultNamespace("recipe_book/furnace_filter_enabled_highlighted"),
            Identifier.withDefaultNamespace("recipe_book/furnace_filter_disabled_highlighted"));
    private static final Component ONLY_CRAFTABLES_TOOLTIP = Component.translatable("gui.recipebook.toggleRecipes.craftable");
    private static final List<TabInfo> TABS = List.of(
            new RecipeBookComponent.TabInfo(Items.COMPASS, RecipeBookCategoriesPM.SEARCH_DISSOLUTION.get()),
            new RecipeBookComponent.TabInfo(Items.RAW_GOLD, RecipeBookCategoriesPM.DISSOLUTION_ORES.get()),
            new RecipeBookComponent.TabInfo(Items.GRAVEL, RecipeBookCategoriesPM.DISSOLUTION_MISC.get())
    );

    public DissolutionChamberRecipeBookComponent(DissolutionChamberMenu menu) {
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
            case 0, 1 -> true;
            default -> false;
        };
    }

    @Override
    protected void selectMatchingRecipes(@NotNull RecipeCollection recipeCollection, @NotNull StackedItemContents stackedItemContents) {
        recipeCollection.selectRecipes(stackedItemContents, display -> display instanceof DissolutionRecipeDisplay);
    }

    @Override
    @NotNull
    protected Component getRecipeFilterName() {
        return ONLY_CRAFTABLES_TOOLTIP;
    }

    @Override
    protected void fillGhostRecipe(@NotNull GhostSlots ghostSlots, @NotNull RecipeDisplay recipeDisplay, @NotNull ContextMap contextMap) {
        ghostSlots.setResult(this.menu.getResultSlot(), contextMap, recipeDisplay.result());
        if (recipeDisplay instanceof DissolutionRecipeDisplay dissolutionRecipeDisplay) {
            ghostSlots.setInput(this.menu.getInputGridSlots().getFirst(), contextMap, dissolutionRecipeDisplay.ingredient());
        }
    }
}
