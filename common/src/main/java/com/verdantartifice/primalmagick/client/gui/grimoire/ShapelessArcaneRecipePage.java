package com.verdantartifice.primalmagick.client.gui.grimoire;

import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.ManaCostSummaryWidget;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.RecipeExpertiseWidget;
import com.verdantartifice.primalmagick.common.crafting.display.ShapelessArcaneCraftingRecipeDisplay;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.display.SlotDisplay;

import java.util.List;

/**
 * Grimoire page showing a shapeless arcane recipe.
 * 
 * @author Daedalus4096
 */
public class ShapelessArcaneRecipePage extends AbstractShapelessRecipePage<ShapelessArcaneCraftingRecipeDisplay> {
    protected final ResourceKey<Recipe<?>> recipeKey;

    public ShapelessArcaneRecipePage(ShapelessArcaneCraftingRecipeDisplay display, ResourceKey<Recipe<?>> recipeKey) {
        super(display);
        this.recipeKey = recipeKey;
    }
    
    @Override
    public void initWidgets(GrimoireScreen screen, int side, int x, int y) {
        // Add base page widgets
        super.initWidgets(screen, side, x, y);
        
        // Add mana cost summary widget
        int indent = 124;
        int overlayWidth = 52;
        if (!this.display.manaCosts().isEmpty()) {
            screen.addWidgetToScreen(new ManaCostSummaryWidget(this.display.manaCosts(), x + 75 + (side * 140) + (indent / 2) - (overlayWidth / 2), y + 30));
        }
        
        // Add expertise widget if applicable
        if (this.display.expertise().hasReward()) {
            screen.addWidgetToScreen(new RecipeExpertiseWidget(this.display.expertise(), this.recipeKey, x - 6 + (side * 140) + (indent / 2) - (overlayWidth / 2), y + 30));
        }
    }

    @Override
    protected String getRecipeTypeTranslationKey() {
        return "grimoire.primalmagick.shapeless_arcane_recipe_header";
    }

    @Override
    protected List<SlotDisplay> getRecipeIngredients() {
        return this.display.ingredients();
    }
}
