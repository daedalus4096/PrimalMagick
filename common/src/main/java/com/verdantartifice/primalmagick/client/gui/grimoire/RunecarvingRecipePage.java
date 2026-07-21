package com.verdantartifice.primalmagick.client.gui.grimoire;

import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.ItemStackWidget;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.RecipeTypeWidget;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.SlotDisplayWidget;
import com.verdantartifice.primalmagick.common.crafting.display.RunecarvingRecipeDisplay;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.display.SlotDisplay;

import java.util.List;

/**
 * Grimoire page showing a runecarving recipe.
 * 
 * @author Daedalus4096
 */
public class RunecarvingRecipePage extends AbstractRecipePage<RunecarvingRecipeDisplay> {
    public RunecarvingRecipePage(RunecarvingRecipeDisplay display) {
        super(display);
    }

    @Override
    protected String getRecipeTypeTranslationKey() {
        return "grimoire.primalmagick.runecarving_recipe_header";
    }

    @Override
    protected List<SlotDisplay> getRecipeIngredients() {
        return List.of(this.display.baseIngredient(), this.display.etchingIngredient());
    }

    @Override
    public void initWidgets(GrimoireScreen screen, int side, int x, int y) {
        int indent = 84;
        int overlayWidth = 13;

        // Render ingredient stacks
        screen.addWidgetToScreen(new SlotDisplayWidget(this.display.baseIngredient(), x - 6 + (side * 140) + (indent / 2), y + 99, screen));
        screen.addWidgetToScreen(new SlotDisplayWidget(this.display.etchingIngredient(), x + 58 + (side * 140) + (indent / 2) - (overlayWidth / 2), y + 99, screen));

        // Render output stack
        screen.addWidgetToScreen(new ItemStackWidget(this.getRecipeResult(), x + 29 + (side * 140) + (indent / 2) - (overlayWidth / 2), y + 30, false));
        
        // Render recipe type widget
        screen.addWidgetToScreen(new RecipeTypeWidget(this.display.craftingStation(), x - 22 + (side * 140) + (indent / 2) - (overlayWidth / 2), y + 30, Component.translatable(this.getRecipeTypeTranslationKey())));
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor guiGraphics, int side, int x, int y, int mouseX, int mouseY) {
        super.extractRenderState(guiGraphics, side, x, y, mouseX, mouseY);
        y += 53;
        
        int indent = 84;
        int overlayWidth = 13;
        int overlayHeight = 13;
        
        // Render overlay background
        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().translate(x + 16 + (side * 140) + (indent / 2) - (overlayWidth / 2), y + 68);
        guiGraphics.pose().scale(2.0F, 2.0F);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, OVERLAY, 0, 0, 0, 51, overlayWidth, overlayHeight, 256, 256);
        guiGraphics.pose().popMatrix();
    }
}
