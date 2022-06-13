package com.verdantartifice.primalmagick.client.gui.grimoire;

import java.awt.Color;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.BlockIngredientWidget;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.IngredientWidget;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.ItemStackWidget;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.ManaCostSummaryWidget;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.RecipeTypeWidget;
import com.verdantartifice.primalmagick.common.crafting.BlockIngredient;
import com.verdantartifice.primalmagick.common.crafting.RitualRecipe;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

/**
 * Grimoire page showing a ritual recipe.
 * 
 * @author Daedalus4096
 */
public class RitualRecipePage extends AbstractRecipePage {
    protected static final int ITEMS_PER_ROW = 7;
    
    protected RitualRecipe recipe;
    
    public RitualRecipePage(RitualRecipe recipe) {
        this.recipe = recipe;
    }

    @Override
    protected String getTitleTranslationKey() {
        return this.recipe.getResultItem().getDescriptionId();
    }

    @Override
    protected String getRecipeTypeTranslationKey() {
        return "primalmagick.grimoire.ritual_recipe_header";
    }

    @Override
    public void initWidgets(GrimoireScreen screen, int side, int x, int y) {
        int indent = 124;
        int overlayWidth = 52;
        int deltaX = 0;
        Minecraft mc = Minecraft.getInstance();

        y += 25;    // Make room for page title
        
        // Render output stack
        ItemStack output = this.recipe.getResultItem();
        screen.addWidgetToScreen(new ItemStackWidget(output, x + 27 + (side * 140) + (indent / 2) - (overlayWidth / 2), y, false));
        
        // Add mana cost summary widget
        if (!this.recipe.getManaCosts().isEmpty()) {
            screen.addWidgetToScreen(new ManaCostSummaryWidget(this.recipe.getManaCosts(), x + 75 + (side * 140) + (indent / 2) - (overlayWidth / 2), y));
        }
        
        // Render recipe type widget
        screen.addWidgetToScreen(new RecipeTypeWidget(this.recipe, x - 22 + (side * 140) + (indent / 2) - (overlayWidth / 2), y, Component.translatable(this.getRecipeTypeTranslationKey())));
        
        y += 28;
        
        // Init ingredient widgets
        if (!this.recipe.getIngredients().isEmpty()) {
            y += mc.font.lineHeight;   // Make room for section header
            for (Ingredient ingredient : this.recipe.getIngredients()) {
                if (deltaX >= (ITEMS_PER_ROW * 18)) {
                    deltaX = 0;
                    y += 18;
                }
                screen.addWidgetToScreen(new IngredientWidget(ingredient, x + 8 + deltaX + (side * 144), y, screen));
                deltaX += 18;
            }
            deltaX = 0;
            y += 18;
            y += (int)(mc.font.lineHeight * 0.66F);
        }
        
        // Init prop widgets
        if (!this.recipe.getProps().isEmpty()) {
            y += mc.font.lineHeight;   // Make room for section header
            for (BlockIngredient prop : this.recipe.getProps()) {
                if (deltaX >= (ITEMS_PER_ROW * 18)) {
                    deltaX = 0;
                    y += 18;
                }
                screen.addWidgetToScreen(new BlockIngredientWidget(prop, x + 8 + deltaX + (side * 144), y));
                deltaX += 18;
            }
            deltaX = 0;
            y += 18;
            y += (int)(mc.font.lineHeight * 0.66F);
        }
    }

    @Override
    public void render(PoseStack matrixStack, int side, int x, int y, int mouseX, int mouseY) {
        super.render(matrixStack, side, x, y, mouseX, mouseY);
        y += 79;
        
        matrixStack.pushPose();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        matrixStack.translate(0.0F, 0.0F, 1.0F);  // Bump up slightly in the Z-order to prevent the underline from being swallowed
        Minecraft mc = Minecraft.getInstance();

        // Render ingredients section header
        if (!this.recipe.getIngredients().isEmpty()) {
            Component leadComponent = Component.translatable("primalmagick.grimoire.ritual_offerings_header").withStyle(ChatFormatting.UNDERLINE);
            mc.font.draw(matrixStack, leadComponent, x - 3 + (side * 140), y - 6, Color.BLACK.getRGB());
            y += mc.font.lineHeight;
            y += 18 * Mth.ceil((double)this.recipe.getIngredients().size() / (double)ITEMS_PER_ROW); // Make room for ingredient widgets
            y += (int)(mc.font.lineHeight * 0.66F);
        }
        
        // Render props section header
        if (!this.recipe.getProps().isEmpty()) {
            Component leadComponent = Component.translatable("primalmagick.grimoire.ritual_props_header").withStyle(ChatFormatting.UNDERLINE);
            mc.font.draw(matrixStack, leadComponent, x - 3 + (side * 140), y - 6, Color.BLACK.getRGB());
            y += mc.font.lineHeight;
            y += 18 * Mth.ceil((double)this.recipe.getProps().size() / (double)ITEMS_PER_ROW);       // Make room for prop widgets
            y += (int)(mc.font.lineHeight * 0.66F);
        }
        
        // Render instability rating line
        Component headerComponent = Component.translatable("primalmagick.ritual.instability.header").withStyle(ChatFormatting.UNDERLINE);
        int rating = Mth.clamp(this.recipe.getInstability() / 2, 0, 5);
        Component valueComponent = Component.translatable("primalmagick.ritual.instability.rating." + rating);
        Component lineComponent = Component.translatable("primalmagick.ritual.instability", headerComponent, valueComponent);
        mc.font.draw(matrixStack, lineComponent, x - 3 + (side * 140), y - 6, Color.BLACK.getRGB());
        y += mc.font.lineHeight;

        matrixStack.popPose();
    }
}
