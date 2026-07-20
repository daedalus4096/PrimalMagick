package com.verdantartifice.primalmagick.client.gui.recipe_book;

import com.verdantartifice.primalmagick.common.concoctions.ConcoctionType;
import com.verdantartifice.primalmagick.common.concoctions.ConcoctionUtils;
import com.verdantartifice.primalmagick.common.crafting.display.ConcoctingRecipeDisplay;
import com.verdantartifice.primalmagick.common.crafting.recipe_book.RecipeBookCategoriesPM;
import com.verdantartifice.primalmagick.common.menus.ConcocterMenu;
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
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

/**
 * Recipe book component for the concocter GUI.
 *
 * @author Daedalus4096
 */
public class ConcocterRecipeBookComponent extends RecipeBookComponent<ConcocterMenu> {
    private static final WidgetSprites FILTER_BUTTON_SPRITES = new WidgetSprites(
            Identifier.withDefaultNamespace("recipe_book/filter_enabled"),
            Identifier.withDefaultNamespace("recipe_book/filter_disabled"),
            Identifier.withDefaultNamespace("recipe_book/filter_enabled_highlighted"),
            Identifier.withDefaultNamespace("recipe_book/filter_disabled_highlighted")
    );
    private static final Component ONLY_CRAFTABLES_TOOLTIP = Component.translatable("gui.recipebook.toggleRecipes.craftable");
    private static final List<TabInfo> TABS = List.of(
            new RecipeBookComponent.TabInfo(Items.COMPASS, RecipeBookCategoriesPM.SEARCH_CONCOCTER.get()),
            new RecipeBookComponent.TabInfo(ConcoctionUtils.newConcoction(Potions.REGENERATION, ConcoctionType.TINCTURE), Optional.empty(), RecipeBookCategoriesPM.CONCOCTER_DRINKABLE.get()),
            new RecipeBookComponent.TabInfo(ConcoctionUtils.newBomb(Potions.POISON), Optional.empty(), RecipeBookCategoriesPM.CONCOCTER_BOMB.get())
    );

    public ConcocterRecipeBookComponent(ConcocterMenu menu) {
        super(menu, TABS);
    }

    private boolean canDisplay(RecipeDisplay display) {
        return display instanceof ConcoctingRecipeDisplay concocting && (this.menu.getGridWidth() * this.menu.getGridHeight() >= concocting.ingredients().size());
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

    @Override
    protected void fillGhostRecipe(@NotNull GhostSlots ghostSlots, @NotNull RecipeDisplay recipeDisplay, @NotNull ContextMap contextMap) {
        ghostSlots.setResult(this.menu.getResultSlot(), contextMap, recipeDisplay.result());
        if (recipeDisplay instanceof ConcoctingRecipeDisplay concoctingRecipeDisplay) {
            List<Slot> inputSlots = this.menu.getInputGridSlots();
            int slotCount = Math.min(concoctingRecipeDisplay.ingredients().size(), inputSlots.size());
            for (int i = 0; i < slotCount; i++) {
                ghostSlots.setInput(inputSlots.get(i), contextMap, concoctingRecipeDisplay.ingredients().get(i));
            }
        }
    }
}
