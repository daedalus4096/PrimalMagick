package com.verdantartifice.primalmagick.client.gui.grimoire;

import com.verdantartifice.primalmagick.client.gui.screens.GrimoireScreen;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.ManaCostSummaryWidget;
import com.verdantartifice.primalmagick.common.crafting.display.ConcoctingRecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;

import java.util.List;

/**
 * Grimoire page showing a concocting recipe.
 * 
 * @author Daedalus4096
 */
public class ConcoctingRecipePage extends AbstractShapelessRecipePage<ConcoctingRecipeDisplay> {
    public ConcoctingRecipePage(ConcoctingRecipeDisplay display) {
        super(display);
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
    }

    @Override
    protected String getRecipeTypeTranslationKey() {
        return "grimoire.primalmagick.concocting_recipe_header";
    }

    @Override
    protected List<SlotDisplay> getRecipeIngredients() {
        return this.display.ingredients();
    }
}
