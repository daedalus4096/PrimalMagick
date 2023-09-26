package com.verdantartifice.primalmagick.client.gui.grimoire;

import java.util.List;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.IngredientWidget;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.ItemStackWidget;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.RecipeTypeWidget;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;

/**
 * Base class for grimoire shapeless recipe pages.
 * 
 * @author Daedalus4096
 * @param <T> type of recipe, e.g. ShapelessArcaneRecipe
 */
public abstract class AbstractShapelessRecipePage<T extends Recipe<?>> extends AbstractRecipePage {
    protected T recipe;
    
    public AbstractShapelessRecipePage(T recipe, RegistryAccess registryAccess) {
        super(registryAccess);
        this.recipe = recipe;
    }

    @Override
    protected Component getTitleText() {
        ItemStack stack = this.recipe.getResultItem(this.registryAccess);
        return stack.getItem().getName(stack);
    }

    @Override
    public void initWidgets(GrimoireScreen screen, int side, int x, int y) {
        int indent = 124;
        int overlayWidth = 51;

        // Render ingredient stacks
        List<Ingredient> ingredients = this.recipe.getIngredients();
        for (int index = 0; index < Math.min(ingredients.size(), 9); index++) {
            Ingredient ingredient = ingredients.get(index);
            if (ingredient != null) {
                screen.addWidgetToScreen(new IngredientWidget(ingredient, x - 5 + (side * 140) + (indent / 2) - (overlayWidth / 2) + ((index % 3) * 32), y + 67 + ((index / 3) * 32), screen));
            }
        }
        
        // Render output stack
        ItemStack output = this.recipe.getResultItem(this.registryAccess);
        screen.addWidgetToScreen(new ItemStackWidget(output, x + 27 + (side * 140) + (indent / 2) - (overlayWidth / 2), y + 30, false));
        
        // Render recipe type widget
        screen.addWidgetToScreen(new RecipeTypeWidget(this.recipe, x - 22 + (side * 140) + (indent / 2) - (overlayWidth / 2), y + 30, Component.translatable(this.getRecipeTypeTranslationKey())));
    }
    
    @Override
    public void render(GuiGraphics guiGraphics, int side, int x, int y, int mouseX, int mouseY) {
        super.render(guiGraphics, side, x, y, mouseX, mouseY);
        y += 53;
        
        int indent = 124;
        int overlayWidth = 51;
        int overlayHeight = 51;
        
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);        
        
        // Render overlay background
        guiGraphics.pose().pushPose();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        guiGraphics.pose().translate(x - 6 + (side * 140) + (indent / 2), y + 49 + (overlayHeight / 2), 0.0F);
        guiGraphics.pose().scale(2.0F, 2.0F, 1.0F);
        guiGraphics.blit(OVERLAY, -(overlayWidth / 2), -(overlayHeight / 2), 0, 0, overlayWidth, overlayHeight);
        guiGraphics.pose().popPose();
    }
}
