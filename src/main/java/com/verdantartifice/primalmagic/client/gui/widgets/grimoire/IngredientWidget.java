package com.verdantartifice.primalmagic.client.gui.widgets.grimoire;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.verdantartifice.primalmagic.client.util.GuiUtils;

import net.minecraft.client.gui.widget.Widget;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Display widget for showing all the possible itemstacks for a given crafting ingredient.  Used
 * on recipe pages.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
public class IngredientWidget extends Widget {
    protected Ingredient ingredient;

    public IngredientWidget(@Nullable Ingredient ingredient, int xIn, int yIn) {
        super(xIn, yIn, 16, 16, StringTextComponent.EMPTY);
        this.ingredient = ingredient;
    }

    @Override
    public void renderButton(MatrixStack matrixStack, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        if (this.ingredient != null) {
            ItemStack[] matching = this.ingredient.getMatchingStacks();
            if (matching != null && matching.length > 0) {
                // Cycle through each matching stack of the ingredient and display them one at a time
                int index = (int)((System.currentTimeMillis() / 1000L) % matching.length);
                ItemStack toDisplay = matching[index];
                GuiUtils.renderItemStack(toDisplay, this.x, this.y, this.getMessage().getString(), false);
                if (this.isHovered()) {
                    // If hovered, show a tooltip with the display name of the current matching itemstack
                	StringTextComponent name = new StringTextComponent(toDisplay.getDisplayName().getString());
                    List<ITextComponent> textList = Collections.singletonList(name.mergeStyle(toDisplay.getItem().getRarity(toDisplay).color));
                    GuiUtils.renderCustomTooltip(matrixStack, textList, this.x, this.y);
                }
            }
        }
    }
    
    @Override
    public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
        // Disable click behavior
        return false;
    }
}
