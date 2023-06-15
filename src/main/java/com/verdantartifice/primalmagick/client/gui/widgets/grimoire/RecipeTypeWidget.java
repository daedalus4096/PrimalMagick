package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import java.util.Collections;

import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagick.client.util.GuiUtils;

import net.minecraft.client.gui.components.AbstractWidget;
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
    protected Component tooltip;
    
    public RecipeTypeWidget(Recipe<?> recipe, int x, int y, Component tooltip) {
        super(x, y, 16, 16, Component.empty());
        this.recipe = recipe;
        this.tooltip = tooltip;
    }

    @Override
    public void renderButton(PoseStack matrixStack, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        // Draw recipe station icon
        GuiUtils.renderItemStack(matrixStack, this.recipe.getToastSymbol(), this.getX(), this.getY(), this.getMessage().getString(), false);
    }

    @Override
    public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
        // Disable click behavior
        return false;
    }

    @Override
    public void updateWidgetNarration(NarrationElementOutput output) {
    }

    @Override
    public void renderToolTip(PoseStack matrixStack, int mouseX, int mouseY) {
        // Draw tooltip if hovered
        matrixStack.pushPose();
        matrixStack.translate(0, 0, 200);
        
        GuiUtils.renderCustomTooltip(matrixStack, Collections.singletonList(this.tooltip), mouseX, mouseY);
        
        matrixStack.popPose();
    }
}
