package com.verdantartifice.primalmagic.client.gui.widgets.grimoire;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.verdantartifice.primalmagic.client.util.GuiUtils;
import com.verdantartifice.primalmagic.common.crafting.BlockIngredient;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Display widget for showing all the possible blocks for a given block ingredient.  Used on
 * recipe pages.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
public class BlockIngredientWidget extends Widget {
    protected final BlockIngredient ingredient;
    
    public BlockIngredientWidget(@Nullable BlockIngredient ingredient, int xIn, int yIn) {
        super(xIn, yIn, 16, 16, StringTextComponent.EMPTY);
        this.ingredient = ingredient;
    }

    @Override
    public void renderWidget(MatrixStack matrixStack, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        if (this.ingredient != null) {
            Block[] matching = this.ingredient.getMatchingBlocks();
            if (matching != null && matching.length > 0) {
                // Cycle through each matching stack of the ingredient and display them one at a time
                int index = (int)((System.currentTimeMillis() / 1000L) % matching.length);
                Block block = matching[index];
                ItemStack toDisplay = (block != null) ? 
                        new ItemStack(block) : 
                        new ItemStack(Blocks.BARRIER).setDisplayName(new TranslationTextComponent("primalmagic.grimoire.missing_block"));
                GuiUtils.renderItemStack(toDisplay, this.x, this.y, this.getMessage().getString(), false);
                if (this.isHovered()) {
                    // If hovered, show a tooltip with the display name of the current matching itemstack
                    List<ITextComponent> textList = Collections.singletonList(toDisplay.getDisplayName().deepCopy().mergeStyle(toDisplay.getItem().getRarity(toDisplay).color));
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
