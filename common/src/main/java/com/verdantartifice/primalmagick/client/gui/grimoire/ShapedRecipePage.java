package com.verdantartifice.primalmagick.client.gui.grimoire;

import net.minecraft.world.item.crafting.display.ShapedCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;

import java.util.List;

/**
 * Grimoire page showing a shaped vanilla recipe.
 * 
 * @author Daedalus4096
 */
public class ShapedRecipePage extends AbstractShapedRecipePage<ShapedCraftingRecipeDisplay> {
    public ShapedRecipePage(ShapedCraftingRecipeDisplay display) {
        super(display);
    }

    @Override
    protected String getRecipeTypeTranslationKey() {
        return "grimoire.primalmagick.shaped_recipe_header";
    }

    @Override
    protected List<SlotDisplay> getRecipeIngredients() {
        return this.display.ingredients();
    }

    @Override
    protected int getRecipeWidth() {
        return this.display.width();
    }

    @Override
    protected int getRecipeHeight() {
        return this.display.height();
    }
}
