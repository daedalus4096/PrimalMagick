package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import java.util.Collections;

import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagick.client.util.GuiUtils;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
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
        super(x, y, 16, 16, TextComponent.EMPTY);
        this.recipe = recipe;
        this.tooltip = tooltip;
    }

    @Override
    public void renderButton(PoseStack matrixStack, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        // Draw recipe station icon
        GuiUtils.renderItemStack(matrixStack, this.recipe.getToastSymbol(), this.x, this.y, this.getMessage().getString(), false);
        
        // Draw tooltip if hovered
        if (this.isHoveredOrFocused()) {
            GuiUtils.renderCustomTooltip(matrixStack, Collections.singletonList(this.tooltip), this.x, this.y);
        }
    }

    @Override
    public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
        // Disable click behavior
        return false;
    }

    @Override
    public void updateNarration(NarrationElementOutput p_169152_) {
    }
}
