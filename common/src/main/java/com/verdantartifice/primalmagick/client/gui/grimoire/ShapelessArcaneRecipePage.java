package com.verdantartifice.primalmagick.client.gui.grimoire;

import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.ManaCostSummaryWidget;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.RecipeExpertiseWidget;
import com.verdantartifice.primalmagick.common.crafting.IShapelessArcaneRecipePM;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.crafting.RecipeHolder;

/**
 * Grimoire page showing a shapeless arcane recipe.
 * 
 * @author Daedalus4096
 */
public class ShapelessArcaneRecipePage extends AbstractShapelessRecipePage<IShapelessArcaneRecipePM> {
    public ShapelessArcaneRecipePage(RecipeHolder<? extends IShapelessArcaneRecipePM> recipe, RegistryAccess registryAccess) {
        super(recipe, registryAccess);
    }
    
    @Override
    public void initWidgets(GrimoireScreen screen, int side, int x, int y) {
        // Add base page widgets
        super.initWidgets(screen, side, x, y);
        
        // Add mana cost summary widget
        int indent = 124;
        int overlayWidth = 52;
        if (!this.recipe.value().getManaCosts().isEmpty()) {
            screen.addWidgetToScreen(new ManaCostSummaryWidget(this.recipe.value().getManaCosts(), x + 75 + (side * 140) + (indent / 2) - (overlayWidth / 2), y + 30));
        }
        
        // Add expertise widget if applicable
        if (this.recipe.value().hasExpertiseReward(this.registryAccess)) {
            screen.addWidgetToScreen(new RecipeExpertiseWidget(this.recipe, x - 6 + (side * 140) + (indent / 2) - (overlayWidth / 2), y + 30));
        }
    }

    @Override
    protected String getRecipeTypeTranslationKey() {
        return "grimoire.primalmagick.shapeless_arcane_recipe_header";
    }
}
