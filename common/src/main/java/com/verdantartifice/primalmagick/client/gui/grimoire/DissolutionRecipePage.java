package com.verdantartifice.primalmagick.client.gui.grimoire;

import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.ItemStackWidget;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.ManaCostSummaryWidget;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.RecipeTypeWidget;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.SlotDisplayWidget;
import com.verdantartifice.primalmagick.common.crafting.display.DissolutionRecipeDisplay;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.display.SlotDisplay;

import java.util.List;

/**
 * Grimoire page showing a dissolution recipe.
 * 
 * @author Daedalus4096
 */
public class DissolutionRecipePage extends AbstractRecipePage<DissolutionRecipeDisplay> {
    protected DissolutionRecipeDisplay display;
    
    public DissolutionRecipePage(DissolutionRecipeDisplay display) {
        super(display);
    }

    @Override
    protected String getRecipeTypeTranslationKey() {
        return "grimoire.primalmagick.dissolution_recipe_header";
    }

    @Override
    protected List<SlotDisplay> getRecipeIngredients() {
        return List.of(this.display.ingredient());
    }

    @Override
    public void initWidgets(GrimoireScreen screen, int side, int x, int y) {
        int indent = 124;
        int overlayWidth = 51;

        // Render mana cost widget if appropriate
        if (!this.display.manaCosts().isEmpty()) {
            screen.addWidgetToScreen(new ManaCostSummaryWidget(this.display.manaCosts(), x + 75 + (side * 140) + (indent / 2) - (overlayWidth / 2), y + 30));
        }

        // Render output stack
        ItemStack output = this.getRecipeResult();
        screen.addWidgetToScreen(new ItemStackWidget(output, x + 27 + (side * 140) + (indent / 2) - (overlayWidth / 2), y + 30, false));
        
        // Render recipe type widget
        screen.addWidgetToScreen(new RecipeTypeWidget(this.display.craftingStation(), x - 22 + (side * 140) + (indent / 2) - (overlayWidth / 2), y + 30, Component.translatable(this.getRecipeTypeTranslationKey())));

        // Render ingredient stacks
        screen.addWidgetToScreen(new SlotDisplayWidget(this.display.ingredient(), x - 5 + (side * 140) + (indent / 2) - (overlayWidth / 2) + 32, y + 67 + 27, screen));
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor guiGraphics, int side, int x, int y, int mouseX, int mouseY) {
        super.extractRenderState(guiGraphics, side, x, y, mouseX, mouseY);
        y += 53;
        
        int indent = 124;
        int overlayWidth = 46;
        int overlayHeight = 46;
        
        // Render overlay background
        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().translate(x - 6 + (side * 140) + (indent / 2), y + 49 + (overlayHeight / 2));
        guiGraphics.pose().scale(2.0F, 2.0F);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, OVERLAY, -(overlayWidth / 2), -(overlayHeight / 2), 97, 0, overlayWidth, overlayHeight, 256, 256);
        guiGraphics.pose().popMatrix();
    }
}
