package com.verdantartifice.primalmagic.client.gui.widgets.grimoire;

import javax.annotation.Nullable;

import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagic.client.util.GuiUtils;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

/**
 * Display widget for showing all the possible itemstacks for a given crafting ingredient.  Used
 * on recipe pages.
 * 
 * @author Daedalus4096
 */
public class IngredientWidget extends AbstractWidget {
    protected Ingredient ingredient;

    public IngredientWidget(@Nullable Ingredient ingredient, int xIn, int yIn) {
        super(xIn, yIn, 16, 16, TextComponent.EMPTY);
        this.ingredient = ingredient;
    }

    @Override
    public void renderButton(PoseStack matrixStack, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        if (this.ingredient != null) {
            ItemStack[] matching = this.ingredient.getItems();
            if (matching != null && matching.length > 0) {
                // Cycle through each matching stack of the ingredient and display them one at a time
                int index = (int)((System.currentTimeMillis() / 1000L) % matching.length);
                ItemStack toDisplay = matching[index];
                GuiUtils.renderItemStack(matrixStack, toDisplay, this.x, this.y, this.getMessage().getString(), false);
                if (this.isHovered()) {
                    // If hovered, show a tooltip with the display name of the current matching itemstack
                    GuiUtils.renderItemTooltip(matrixStack, toDisplay, this.x, this.y);
                }
            }
        }
    }
    
    @Override
    public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
        // Disable click behavior
        return false;
    }

    @Override
    public void updateNarration(NarrationElementOutput output) {
    }
}
