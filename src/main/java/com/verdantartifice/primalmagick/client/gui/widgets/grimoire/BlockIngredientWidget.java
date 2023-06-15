package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import javax.annotation.Nullable;

import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagick.client.util.GuiUtils;
import com.verdantartifice.primalmagick.common.crafting.BlockIngredient;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

/**
 * Display widget for showing all the possible blocks for a given block ingredient.  Used on
 * recipe pages.
 * 
 * @author Daedalus4096
 */
public class BlockIngredientWidget extends AbstractWidget {
    protected final BlockIngredient ingredient;
    protected ItemStack toDisplay = ItemStack.EMPTY;
    
    public BlockIngredientWidget(@Nullable BlockIngredient ingredient, int xIn, int yIn) {
        super(xIn, yIn, 16, 16, Component.empty());
        this.ingredient = ingredient;
    }

    @Override
    public void renderButton(PoseStack matrixStack, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        if (this.ingredient != null) {
            Block[] matching = this.ingredient.getMatchingBlocks();
            if (matching != null && matching.length > 0) {
                // Cycle through each matching stack of the ingredient and display them one at a time
                int index = (int)((System.currentTimeMillis() / 1000L) % matching.length);
                Block block = matching[index];
                this.toDisplay = (block != null) ? 
                        new ItemStack(block) : 
                        new ItemStack(Blocks.BARRIER).setHoverName(Component.translatable("primalmagick.grimoire.missing_block"));
                GuiUtils.renderItemStack(matrixStack, this.toDisplay, this.x, this.y, this.getMessage().getString(), false);
            } else {
                this.toDisplay = ItemStack.EMPTY;
            }
        }
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
        if (!this.toDisplay.isEmpty()) {
            // If hovered, show a tooltip with the display name of the current matching itemstack
            matrixStack.pushPose();
            matrixStack.translate(0, 0, 200);
            
            GuiUtils.renderItemTooltip(matrixStack, this.toDisplay, mouseX, mouseY);
            
            matrixStack.popPose();
        }
    }
}
