package com.verdantartifice.primalmagick.client.gui.grimoire;

import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.ItemStackWidget;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.RecipeTypeWidget;
import com.verdantartifice.primalmagick.client.gui.widgets.grimoire.SlotDisplayWidget;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;

import java.util.List;

/**
 * Base class for grimoire shapeless recipe pages.
 * 
 * @author Daedalus4096
 * @param <T> type of recipe, e.g. ShapelessArcaneRecipe
 */
public abstract class AbstractShapelessRecipePage<T extends RecipeDisplay> extends AbstractRecipePage<T> {
    public AbstractShapelessRecipePage(T display) {
        super(display);
    }

    @Override
    public void initWidgets(GrimoireScreen screen, int side, int x, int y) {
        int indent = 124;
        int overlayWidth = 51;

        // Render ingredient stacks
        List<SlotDisplay> ingredients = this.getRecipeIngredients();
        for (int index = 0; index < Math.min(ingredients.size(), 9); index++) {
            SlotDisplay ingDisplay = ingredients.get(index);
            if (ingDisplay != null) {
                screen.addWidgetToScreen(new SlotDisplayWidget(ingDisplay, x - 5 + (side * 140) + (indent / 2) - (overlayWidth / 2) + ((index % 3) * 32), y + 67 + ((index / 3) * 32), screen));
            }
        }
        
        // Render output stack
        ItemStack output = this.getRecipeResult();
        screen.addWidgetToScreen(new ItemStackWidget(output, x + 27 + (side * 140) + (indent / 2) - (overlayWidth / 2), y + 30, false));
        
        // Render recipe type widget
        screen.addWidgetToScreen(new RecipeTypeWidget(this.display.craftingStation(), x - 22 + (side * 140) + (indent / 2) - (overlayWidth / 2), y + 30, Component.translatable(this.getRecipeTypeTranslationKey())));
    }
    
    @Override
    public void extractRenderState(GuiGraphicsExtractor guiGraphics, int side, int x, int y, int mouseX, int mouseY) {
        super.extractRenderState(guiGraphics, side, x, y, mouseX, mouseY);
        y += 53;
        
        int indent = 124;
        int overlayWidth = 51;
        int overlayHeight = 51;
        
        // Render overlay background
        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().translate(x - 6 + (side * 140) + (indent / 2), y + 49 + (overlayHeight / 2));
        guiGraphics.pose().scale(2.0F, 2.0F);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, OVERLAY, -(overlayWidth / 2), -(overlayHeight / 2), 0, 0, overlayWidth, overlayHeight, 256, 256);
        guiGraphics.pose().popMatrix();
    }
}
