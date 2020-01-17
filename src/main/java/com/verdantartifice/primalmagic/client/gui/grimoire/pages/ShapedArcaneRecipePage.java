package com.verdantartifice.primalmagic.client.gui.grimoire.pages;

import com.verdantartifice.primalmagic.client.gui.grimoire.GrimoireScreen;
import com.verdantartifice.primalmagic.common.crafting.ShapedArcaneRecipe;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Grimoire page showing a shaped arcane recipe.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
public class ShapedArcaneRecipePage extends AbstractShapedRecipePage<ShapedArcaneRecipe> {
    protected ShapedArcaneRecipe recipe;
    
    public ShapedArcaneRecipePage(ShapedArcaneRecipe recipe) {
        super(recipe);
    }
    
    @Override
    protected String getTitleTranslationKey() {
        return "primalmagic.grimoire.shaped_arcane_recipe_header";
    }
    
    @Override
    public void initWidgets(GrimoireScreen screen, int side, int x, int y) {
        super.initWidgets(screen, side, x, y);
        // TODO Render mana cost widgets
    }
}
