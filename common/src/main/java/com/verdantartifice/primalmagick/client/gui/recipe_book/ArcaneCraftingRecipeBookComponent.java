package com.verdantartifice.primalmagick.client.gui.recipe_book;

import com.verdantartifice.primalmagick.common.crafting.display.ShapedArcaneCraftingRecipeDisplay;
import com.verdantartifice.primalmagick.common.crafting.display.ShapelessArcaneCraftingRecipeDisplay;
import com.verdantartifice.primalmagick.common.crafting.recipe_book.RecipeBookCategoriesPM;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.menus.ArcaneWorkbenchMenu;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.recipebook.GhostSlots;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import net.minecraft.network.chat.Component;
import net.minecraft.recipebook.PlaceRecipeHelper;
import net.minecraft.resources.Identifier;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapedCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapelessCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Recipe book component for the arcane workbench GUI.
 *
 * @author Daedalus4096
 */
public class ArcaneCraftingRecipeBookComponent extends RecipeBookComponent<ArcaneWorkbenchMenu> {
    private static final WidgetSprites FILTER_BUTTON_SPRITES = new WidgetSprites(
            Identifier.withDefaultNamespace("recipe_book/filter_enabled"),
            Identifier.withDefaultNamespace("recipe_book/filter_disabled"),
            Identifier.withDefaultNamespace("recipe_book/filter_enabled_highlighted"),
            Identifier.withDefaultNamespace("recipe_book/filter_disabled_highlighted")
    );
    private static final Component ONLY_CRAFTABLES_TOOLTIP = Component.translatable("gui.recipebook.toggleRecipes.craftable");
    private static final List<TabInfo> TABS = List.of(
            new RecipeBookComponent.TabInfo(Items.COMPASS, RecipeBookCategoriesPM.SEARCH_ARCANE_CRAFTING.get()),
            new RecipeBookComponent.TabInfo(ItemsPM.GRIMOIRE.get(), RecipeBookCategoriesPM.CRAFTING_ARCANE.get()),
            new RecipeBookComponent.TabInfo(Items.IRON_AXE, Items.GOLDEN_SWORD, RecipeBookCategories.CRAFTING_EQUIPMENT),
            new RecipeBookComponent.TabInfo(Items.BRICKS, RecipeBookCategories.CRAFTING_BUILDING_BLOCKS),
            new RecipeBookComponent.TabInfo(Items.LAVA_BUCKET, Items.APPLE, RecipeBookCategories.CRAFTING_MISC),
            new RecipeBookComponent.TabInfo(Items.REDSTONE, RecipeBookCategories.CRAFTING_REDSTONE)
    );

    public ArcaneCraftingRecipeBookComponent(ArcaneWorkbenchMenu menu) {
        super(menu, TABS);
    }

    private boolean canDisplay(RecipeDisplay display) {
        int gridWidth = this.menu.getGridWidth();
        int gridHeight = this.menu.getGridHeight();

        return switch (display) {
            case ShapedArcaneCraftingRecipeDisplay shapedArcane -> gridWidth >= shapedArcane.width() && gridHeight >= shapedArcane.height();
            case ShapelessArcaneCraftingRecipeDisplay shapelessArcane -> gridWidth * gridHeight >= shapelessArcane.ingredients().size();
            case ShapedCraftingRecipeDisplay shaped -> gridWidth >= shaped.width() && gridHeight >= shaped.height();
            case ShapelessCraftingRecipeDisplay shapeless -> gridWidth * gridHeight >= shapeless.ingredients().size();
            default -> false;
        };
    }

    @Override
    @NotNull
    protected WidgetSprites getFilterButtonTextures() {
        return FILTER_BUTTON_SPRITES;
    }

    @Override
    protected boolean isCraftingSlot(@NotNull Slot slot) {
        return this.menu.getResultSlot() == slot || this.menu.getInputGridSlots().contains(slot);
    }

    @Override
    protected void selectMatchingRecipes(@NotNull RecipeCollection recipeCollection, @NotNull StackedItemContents stackedItemContents) {
        recipeCollection.selectRecipes(stackedItemContents, this::canDisplay);
    }

    @Override
    @NotNull
    protected Component getRecipeFilterName() {
        return ONLY_CRAFTABLES_TOOLTIP;
    }

    private void placeShapedGhostRecipe(@NotNull GhostSlots ghostSlots, @NotNull ContextMap contextMap, int width, int height, @NotNull List<SlotDisplay> ingredients) {
        List<Slot> inputSlots = this.menu.getInputGridSlots();
        PlaceRecipeHelper.placeRecipe(
                this.menu.getGridWidth(),
                this.menu.getGridHeight(),
                width,
                height,
                ingredients,
                (ingredient, gridIndex, gridXPos, gridYPos) -> {
                    Slot slot = inputSlots.get(gridIndex);
                    ghostSlots.setInput(slot, contextMap, ingredient);
                }
        );
    }

    private void placeShapelessGhostRecipe(@NotNull GhostSlots ghostSlots, @NotNull ContextMap contextMap, @NotNull List<SlotDisplay> ingredients) {
        List<Slot> inputSlots = this.menu.getInputGridSlots();
        int slotCount = Math.min(ingredients.size(), inputSlots.size());
        for (int i = 0; i < slotCount; i++) {
            ghostSlots.setInput(inputSlots.get(i), contextMap, ingredients.get(i));
        }
    }

    @Override
    protected void fillGhostRecipe(@NotNull GhostSlots ghostSlots, @NotNull RecipeDisplay recipeDisplay, @NotNull ContextMap contextMap) {
        ghostSlots.setResult(this.menu.getResultSlot(), contextMap, recipeDisplay.result());
        switch (recipeDisplay) {
            case ShapedArcaneCraftingRecipeDisplay shapedArcane: {
                this.placeShapedGhostRecipe(ghostSlots, contextMap, shapedArcane.width(), shapedArcane.height(), shapedArcane.ingredients());
                break;
            }
            case ShapelessArcaneCraftingRecipeDisplay shapelessArcane: {
                this.placeShapelessGhostRecipe(ghostSlots, contextMap, shapelessArcane.ingredients());
                break;
            }
            case ShapedCraftingRecipeDisplay shaped: {
                this.placeShapedGhostRecipe(ghostSlots, contextMap, shaped.width(), shaped.height(), shaped.ingredients());
                break;
            }
            case ShapelessCraftingRecipeDisplay shapeless: {
                this.placeShapelessGhostRecipe(ghostSlots, contextMap, shapeless.ingredients());
                break;
            }
            default:
        }
    }
}
