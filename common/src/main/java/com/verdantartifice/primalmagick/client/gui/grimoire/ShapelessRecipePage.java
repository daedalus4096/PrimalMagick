package com.verdantartifice.primalmagick.client.gui.grimoire;

import net.minecraft.world.item.crafting.display.ShapelessCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;

import java.util.List;

/**
 * Grimoire page showing a shapeless vanilla recipe.
 * 
 * @author Daedalus4096
 */
public class ShapelessRecipePage extends AbstractShapelessRecipePage<ShapelessCraftingRecipeDisplay> {
    public ShapelessRecipePage(ShapelessCraftingRecipeDisplay display) {
        super(display);
    }

    @Override
    protected String getRecipeTypeTranslationKey() {
        return "grimoire.primalmagick.shapeless_recipe_header";
    }

    @Override
    protected List<SlotDisplay> getRecipeIngredients() {
        return this.display.ingredients();
    }
}
