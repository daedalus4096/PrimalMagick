package com.verdantartifice.primalmagic.client.gui.grimoire.pages;

import java.util.List;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.verdantartifice.primalmagic.client.gui.grimoire.GrimoireScreen;
import com.verdantartifice.primalmagic.client.gui.grimoire.widgets.IngredientWidget;
import com.verdantartifice.primalmagic.client.gui.grimoire.widgets.ItemStackWidget;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.crafting.IShapedRecipe;

/**
 * Base class for grimoire shaped recipe pages.
 * 
 * @author Daedalus4096
 * @param <T> type of recipe, e.g. ShapedArcaneRecipe
 */
@OnlyIn(Dist.CLIENT)
public abstract class AbstractShapedRecipePage<T extends IShapedRecipe<?>> extends AbstractRecipePage {
    protected T recipe;
    
    public AbstractShapedRecipePage(T recipe) {
        this.recipe = recipe;
    }

    @Override
    public void initWidgets(GrimoireScreen screen, int side, int x, int y) {
        int indent = 124;
        int overlayWidth = 52;

        // Render ingredient stacks
        int recipeWidth = this.recipe.getRecipeWidth();
        int recipeHeight = this.recipe.getRecipeHeight();
        List<Ingredient> ingredients = this.recipe.getIngredients();
        for (int i = 0; i < Math.min(recipeWidth, 3); i++) {
            for (int j = 0; j < Math.min(recipeHeight, 3); j++) {
                Ingredient ingredient = ingredients.get(i + j * recipeWidth);
                if (ingredient != null) {
                    screen.addWidgetToScreen(new IngredientWidget(ingredient, x - 5 + (side * 140) + (indent / 2) - (overlayWidth / 2) + (i * 32), y + 67 + (j * 32)));
                }
            }
        }
        
        // Render output stack
        ItemStack output = this.recipe.getRecipeOutput();
        screen.addWidgetToScreen(new ItemStackWidget(output, x + 27 + (side * 140) + (indent / 2) - (overlayWidth / 2), y + 30, false));
    }
    
    @Override
    public void render(int side, int x, int y, int mouseX, int mouseY) {
        super.render(side, x, y, mouseX, mouseY);
        y += 53;
        
        int indent = 124;
        int overlayWidth = 52;
        int overlayHeight = 52;
        
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        Minecraft.getInstance().getTextureManager().bindTexture(OVERLAY);
        
        // Render overlay background
        RenderSystem.pushMatrix();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.translatef(x - 5 + (side * 140) + (indent / 2), y + 50 + (overlayHeight / 2), 0.0F);
        RenderSystem.scalef(2.0F, 2.0F, 1.0F);
        this.blit(-(overlayWidth / 2), -(overlayHeight / 2), 0, 0, overlayWidth, overlayHeight);
        RenderSystem.popMatrix();
    }
}
