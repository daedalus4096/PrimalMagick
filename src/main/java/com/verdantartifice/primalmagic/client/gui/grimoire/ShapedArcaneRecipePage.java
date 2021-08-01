package com.verdantartifice.primalmagic.client.gui.grimoire;

import com.verdantartifice.primalmagic.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagic.client.gui.widgets.grimoire.ManaCostSummaryWidget;
import com.verdantartifice.primalmagic.common.crafting.ShapedArcaneRecipe;

/**
 * Grimoire page showing a shaped arcane recipe.
 * 
 * @author Daedalus4096
 */
public class ShapedArcaneRecipePage extends AbstractShapedRecipePage<ShapedArcaneRecipe> {
    public ShapedArcaneRecipePage(ShapedArcaneRecipe recipe) {
        super(recipe);
    }
    
    @Override
    protected String getTitleTranslationKey() {
        return "primalmagic.grimoire.shaped_arcane_recipe_header";
    }
    
    @Override
    public void initWidgets(GrimoireScreen screen, int side, int x, int y) {
        // Add base page widgets
        super.initWidgets(screen, side, x, y);
        
        // Add mana cost summary widget
        int indent = 124;
        int overlayWidth = 52;
        screen.addWidgetToScreen(new ManaCostSummaryWidget(this.recipe.getManaCosts(), x + 75 + (side * 140) + (indent / 2) - (overlayWidth / 2), y + 30));
    }
}
