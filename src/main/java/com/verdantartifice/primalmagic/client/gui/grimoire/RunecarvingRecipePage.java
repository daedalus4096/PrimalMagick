package com.verdantartifice.primalmagic.client.gui.grimoire;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.verdantartifice.primalmagic.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagic.client.gui.widgets.grimoire.IngredientWidget;
import com.verdantartifice.primalmagic.client.gui.widgets.grimoire.ItemStackWidget;
import com.verdantartifice.primalmagic.common.crafting.RunecarvingRecipe;

import net.minecraft.client.Minecraft;

/**
 * Grimoire page showing a runecarving recipe.
 * 
 * @author Daedalus4096
 */
public class RunecarvingRecipePage extends AbstractRecipePage {
    protected RunecarvingRecipe recipe;
    
    public RunecarvingRecipePage(RunecarvingRecipe recipe) {
        this.recipe = recipe;
    }
    
    @Override
    protected String getTitleTranslationKey() {
        return "primalmagic.grimoire.runecarving_recipe_header";
    }

    @Override
    public void initWidgets(GrimoireScreen screen, int side, int x, int y) {
        int indent = 84;
        int overlayWidth = 13;

        // Render ingredient stacks
        screen.addWidgetToScreen(new IngredientWidget(this.recipe.getIngredients().get(0), x - 6 + (side * 140) + (indent / 2), y + 99));
        screen.addWidgetToScreen(new IngredientWidget(this.recipe.getIngredients().get(1), x + 58 + (side * 140) + (indent / 2) - (overlayWidth / 2), y + 99));

        // Render output stack
        screen.addWidgetToScreen(new ItemStackWidget(this.recipe.getRecipeOutput(), x + 29 + (side * 140) + (indent / 2) - (overlayWidth / 2), y + 30, false));
    }

    @Override
    public void render(MatrixStack matrixStack, int side, int x, int y, int mouseX, int mouseY) {
        super.render(matrixStack, side, x, y, mouseX, mouseY);
        y += 53;
        
        int indent = 84;
        int overlayWidth = 13;
        int overlayHeight = 13;
        
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        Minecraft.getInstance().getTextureManager().bindTexture(OVERLAY);
        
        // Render overlay background
        matrixStack.push();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        matrixStack.translate(x + 16 + (side * 140) + (indent / 2) - (overlayWidth / 2), y + 68, 0.0F);
        matrixStack.scale(2.0F, 2.0F, 1.0F);
        this.blit(matrixStack, 0, 0, 0, 51, overlayWidth, overlayHeight);
        matrixStack.pop();
    }
}
