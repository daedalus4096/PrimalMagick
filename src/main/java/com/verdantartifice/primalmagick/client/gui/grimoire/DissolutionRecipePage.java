package com.verdantartifice.primalmagick.client.gui.grimoire;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.IngredientWidget;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.ItemStackWidget;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.ManaCostSummaryWidget;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.RecipeTypeWidget;
import com.verdantartifice.primalmagick.common.crafting.DissolutionRecipe;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

/**
 * Grimoire page showing a dissolution recipe.
 * 
 * @author Daedalus4096
 */
public class DissolutionRecipePage extends AbstractRecipePage {
    protected DissolutionRecipe recipe;
    
    public DissolutionRecipePage(DissolutionRecipe recipe, RegistryAccess registryAccess) {
        super(registryAccess);
        this.recipe = recipe;
    }

    @Override
    protected Component getTitleText() {
        ItemStack stack = this.recipe.getResultItem(this.registryAccess);
        return stack.getItem().getName(stack);
    }

    @Override
    protected String getRecipeTypeTranslationKey() {
        return "grimoire.primalmagick.dissolution_recipe_header";
    }

    @Override
    public void initWidgets(GrimoireScreen screen, int side, int x, int y) {
        int indent = 124;
        int overlayWidth = 51;

        // Render mana cost widget if appropriate
        if (!this.recipe.getManaCosts().isEmpty()) {
            screen.addWidgetToScreen(new ManaCostSummaryWidget(this.recipe.getManaCosts(), x + 75 + (side * 140) + (indent / 2) - (overlayWidth / 2), y + 30));
        }

        // Render output stack
        ItemStack output = this.recipe.getResultItem(this.registryAccess);
        screen.addWidgetToScreen(new ItemStackWidget(output, x + 27 + (side * 140) + (indent / 2) - (overlayWidth / 2), y + 30, false));
        
        // Render recipe type widget
        screen.addWidgetToScreen(new RecipeTypeWidget(this.recipe, x - 22 + (side * 140) + (indent / 2) - (overlayWidth / 2), y + 30, Component.translatable(this.getRecipeTypeTranslationKey())));

        // Render ingredient stacks
        screen.addWidgetToScreen(new IngredientWidget(this.recipe.getIngredients().get(0), x - 5 + (side * 140) + (indent / 2) - (overlayWidth / 2) + 32, y + 67 + 27, screen));
    }

    @Override
    public void render(GuiGraphics guiGraphics, int side, int x, int y, int mouseX, int mouseY) {
        super.render(guiGraphics, side, x, y, mouseX, mouseY);
        y += 53;
        
        int indent = 124;
        int overlayWidth = 46;
        int overlayHeight = 46;
        
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        
        // Render overlay background
        guiGraphics.pose().pushPose();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        guiGraphics.pose().translate(x - 6 + (side * 140) + (indent / 2), y + 49 + (overlayHeight / 2), 0.0F);
        guiGraphics.pose().scale(2.0F, 2.0F, 1.0F);
        guiGraphics.blit(OVERLAY, -(overlayWidth / 2), -(overlayHeight / 2), 97, 0, overlayWidth, overlayHeight);
        guiGraphics.pose().popPose();
    }
}
