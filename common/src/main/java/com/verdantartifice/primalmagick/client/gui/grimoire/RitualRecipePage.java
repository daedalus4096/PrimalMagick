package com.verdantartifice.primalmagick.client.gui.grimoire;

import com.verdantartifice.primalmagick.client.gui.screens.GrimoireScreen;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.ItemStackWidget;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.ManaCostSummaryWidget;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.RecipeExpertiseWidget;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.RecipeTypeWidget;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.SlotDisplayWidget;
import com.verdantartifice.primalmagick.common.crafting.display.RitualRecipeDisplay;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.display.SlotDisplay;

import java.awt.Color;
import java.util.List;

/**
 * Grimoire page showing a ritual recipe.
 * 
 * @author Daedalus4096
 */
public class RitualRecipePage extends AbstractRecipePage<RitualRecipeDisplay> {
    protected static final int ITEMS_PER_ROW = 7;
    
    protected ResourceKey<Recipe<?>> recipeKey;
    
    public RitualRecipePage(RitualRecipeDisplay display, ResourceKey<Recipe<?>> recipeKey) {
        super(display);
        this.recipeKey = recipeKey;
    }

    @Override
    protected String getRecipeTypeTranslationKey() {
        return "grimoire.primalmagick.ritual_recipe_header";
    }

    @Override
    protected List<SlotDisplay> getRecipeIngredients() {
        return List.of();
    }

    @Override
    public void initWidgets(GrimoireScreen screen, int side, int x, int y) {
        int indent = 124;
        int overlayWidth = 52;
        int deltaX = 0;
        Minecraft mc = Minecraft.getInstance();

        y += 25;    // Make room for page title
        
        // Render output stack
        ItemStack output = this.getRecipeResult();
        screen.addWidgetToScreen(new ItemStackWidget(output, x + 27 + (side * 140) + (indent / 2) - (overlayWidth / 2), y, false));
        
        // Add mana cost summary widget
        if (!this.display.manaCosts().isEmpty()) {
            screen.addWidgetToScreen(new ManaCostSummaryWidget(this.display.manaCosts(), x + 75 + (side * 140) + (indent / 2) - (overlayWidth / 2), y));
        }
        
        // Render recipe type widget
        screen.addWidgetToScreen(new RecipeTypeWidget(this.display.craftingStation(), x - 22 + (side * 140) + (indent / 2) - (overlayWidth / 2), y, Component.translatable(this.getRecipeTypeTranslationKey())));
        
        // Render recipe expertise widget if applicable
        if (this.display.expertise().hasReward()) {
            screen.addWidgetToScreen(new RecipeExpertiseWidget(this.display.expertise(), this.recipeKey, x - 6 + (side * 140) + (indent / 2) - (overlayWidth / 2), y));
        }
        
        y += 28;
        
        // Init ingredient widgets
        if (!this.display.ingredients().isEmpty()) {
            y += mc.font.lineHeight;   // Make room for section header
            for (SlotDisplay ingredient : this.display.ingredients()) {
                if (deltaX >= (ITEMS_PER_ROW * 18)) {
                    deltaX = 0;
                    y += 18;
                }
                screen.addWidgetToScreen(new SlotDisplayWidget(ingredient, x + 8 + deltaX + (side * 144), y, screen));
                deltaX += 18;
            }
            deltaX = 0;
            y += 18;
            y += (int)(mc.font.lineHeight * 0.66F);
        }
        
        // Init prop widgets
        if (!this.display.props().isEmpty()) {
            y += mc.font.lineHeight;   // Make room for section header
            for (SlotDisplay prop : this.display.props()) {
                if (deltaX >= (ITEMS_PER_ROW * 18)) {
                    deltaX = 0;
                    y += 18;
                }
                screen.addWidgetToScreen(new SlotDisplayWidget(prop, x + 8 + deltaX + (side * 144), y, screen));
                deltaX += 18;
            }
            deltaX = 0;
            y += 18;
            y += (int)(mc.font.lineHeight * 0.66F);
        }
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor guiGraphics, int side, int x, int y, int mouseX, int mouseY) {
        super.extractRenderState(guiGraphics, side, x, y, mouseX, mouseY);
        y += 79;
        
        guiGraphics.pose().pushMatrix();
        Minecraft mc = Minecraft.getInstance();

        // Render ingredients section header
        if (!this.display.ingredients().isEmpty()) {
            Component leadComponent = Component.translatable("grimoire.primalmagick.ritual_offerings_header").withStyle(ChatFormatting.UNDERLINE);
            guiGraphics.text(mc.font, leadComponent, x - 3 + (side * 140), y - 6, Color.BLACK.getRGB(), false);
            y += mc.font.lineHeight;
            y += 18 * Mth.ceil((double)this.display.ingredients().size() / (double)ITEMS_PER_ROW); // Make room for ingredient widgets
            y += (int)(mc.font.lineHeight * 0.66F);
        }
        
        // Render props section header
        if (!this.display.props().isEmpty()) {
            Component leadComponent = Component.translatable("grimoire.primalmagick.ritual_props_header").withStyle(ChatFormatting.UNDERLINE);
            guiGraphics.text(mc.font, leadComponent, x - 3 + (side * 140), y - 6, Color.BLACK.getRGB(), false);
            y += mc.font.lineHeight;
            y += 18 * Mth.ceil((double)this.display.props().size() / (double)ITEMS_PER_ROW);       // Make room for prop widgets
            y += (int)(mc.font.lineHeight * 0.66F);
        }
        
        // Render instability rating line
        Component headerComponent = Component.translatable("ritual.primalmagick.instability.header").withStyle(ChatFormatting.UNDERLINE);
        int rating = Mth.clamp(this.display.instability() / 2, 0, 5);
        Component valueComponent = Component.translatable("ritual.primalmagick.instability.rating." + rating);
        Component lineComponent = Component.translatable("ritual.primalmagick.instability", headerComponent, valueComponent);
        guiGraphics.text(mc.font, lineComponent, x - 3 + (side * 140), y - 6, Color.BLACK.getRGB(), false);
        y += mc.font.lineHeight;

        guiGraphics.pose().popMatrix();
    }
}
