package com.verdantartifice.primalmagick.client.gui.grimoire;

import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.ManaCostSummaryWidget;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.RecipeExpertiseWidget;
import com.verdantartifice.primalmagick.common.crafting.IShapedArcaneRecipePM;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.crafting.RecipeHolder;

/**
 * Grimoire page showing a shaped arcane recipe.
 * 
 * @author Daedalus4096
 */
public class ShapedArcaneRecipePage extends AbstractShapedRecipePage<IShapedArcaneRecipePM> {
    public ShapedArcaneRecipePage(RecipeHolder<? extends IShapedArcaneRecipePM> recipe, RegistryAccess registryAccess) {
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
        return "grimoire.primalmagick.shaped_arcane_recipe_header";
    }
}
