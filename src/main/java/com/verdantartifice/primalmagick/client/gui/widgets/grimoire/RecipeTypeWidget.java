package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import com.verdantartifice.primalmagick.client.util.GuiUtils;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.Recipe;

/**
 * Display widget for showing a recipe type.  Used on the recipe pages.
 * 
 * @author Daedalus4096
 */
public class RecipeTypeWidget extends AbstractWidget {
    protected Recipe<?> recipe;
    
    public RecipeTypeWidget(Recipe<?> recipe, int x, int y, Component tooltip) {
        super(x, y, 16, 16, Component.empty());
        this.recipe = recipe;
        this.setTooltip(Tooltip.create(tooltip));
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        // Draw recipe station icon
        GuiUtils.renderItemStack(guiGraphics, this.recipe.getToastSymbol(), this.getX(), this.getY(), this.getMessage().getString(), false);

        // Don't allow the widget to become focused, to prevent keyboard navigation from moving the active tooltip
        this.setFocused(false);
    }

    @Override
    public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
        // Disable click behavior
        return false;
    }

    @Override
    public void updateWidgetNarration(NarrationElementOutput output) {
    }
}
