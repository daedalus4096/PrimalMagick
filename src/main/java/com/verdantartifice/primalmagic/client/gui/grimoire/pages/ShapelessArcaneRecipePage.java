package com.verdantartifice.primalmagic.client.gui.grimoire.pages;

import com.verdantartifice.primalmagic.client.gui.grimoire.GrimoireScreen;
import com.verdantartifice.primalmagic.common.crafting.ShapelessArcaneRecipe;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ShapelessArcaneRecipePage extends AbstractShapelessRecipePage<ShapelessArcaneRecipe> {
    public ShapelessArcaneRecipePage(ShapelessArcaneRecipe recipe) {
        super(recipe);
    }
    
    @Override
    protected String getTitleTranslationKey() {
        return "primalmagic.grimoire.shapeless_arcane_recipe_header";
    }
    
    @Override
    public void initWidgets(GrimoireScreen screen, int side, int x, int y) {
        super.initWidgets(screen, side, x, y);
        // TODO Render mana cost widgets
    }
}
