package com.verdantartifice.primalmagic.client.gui.grimoire;

import com.verdantartifice.primalmagic.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagic.client.gui.widgets.grimoire.IngredientWidget;
import com.verdantartifice.primalmagic.client.gui.widgets.grimoire.ItemStackWidget;
import com.verdantartifice.primalmagic.client.gui.widgets.grimoire.RecipeTypeWidget;

import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SmeltingRecipe;

/**
 * Grimoire page showing a smelting recipe.
 * 
 * @author Daedalus4096
 */
public class SmeltingRecipePage extends AbstractRecipePage {
    protected SmeltingRecipe recipe;
    
    public SmeltingRecipePage(SmeltingRecipe recipe) {
        this.recipe = recipe;
    }

    @Override
    protected String getTitleTranslationKey() {
        return this.recipe.getResultItem().getDescriptionId();
    }

    @Override
    protected String getRecipeTypeTranslationKey() {
        return "primalmagic.grimoire.smelting_recipe_header";
    }

    @Override
    public void initWidgets(GrimoireScreen screen, int side, int x, int y) {
        int indent = 124;
        int overlayWidth = 51;

        // Render ingredient stacks
        screen.addWidgetToScreen(new IngredientWidget(this.recipe.getIngredients().get(0), x - 5 + (side * 140) + (indent / 2) - (overlayWidth / 2) + 32, y + 67 + 32));

        // Render output stack
        ItemStack output = this.recipe.getResultItem();
        screen.addWidgetToScreen(new ItemStackWidget(output, x + 27 + (side * 140) + (indent / 2) - (overlayWidth / 2), y + 30, false));
        
        // Render recipe type widget
        screen.addWidgetToScreen(new RecipeTypeWidget(this.recipe, x - 22 + (side * 140) + (indent / 2) - (overlayWidth / 2), y + 30, new TranslatableComponent(this.getRecipeTypeTranslationKey())));
    }

}
