package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.client.util.GuiUtils;
import com.verdantartifice.primalmagick.common.crafting.BlockIngredient;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
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
    public void renderWidget(GuiGraphics guiGraphics, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        if (this.ingredient != null) {
            Block[] matching = this.ingredient.getMatchingBlocks();
            if (matching != null && matching.length > 0) {
                // Cycle through each matching stack of the ingredient and display them one at a time
                int index = (int)((System.currentTimeMillis() / 1000L) % matching.length);
                Block block = matching[index];
                this.toDisplay = (block != null) ? 
                        new ItemStack(block) : 
                        new ItemStack(Blocks.BARRIER).setHoverName(Component.translatable("grimoire.primalmagick.missing_block"));
                GuiUtils.renderItemStack(guiGraphics, this.toDisplay, this.getX(), this.getY(), this.getMessage().getString(), false);
            } else {
                this.toDisplay = ItemStack.EMPTY;
            }
        }
        if (!this.toDisplay.isEmpty()) {
            Minecraft mc = Minecraft.getInstance();
            this.setTooltip(Tooltip.create(CommonComponents.joinLines(this.toDisplay.getTooltipLines(mc.player, mc.options.advancedItemTooltips ? TooltipFlag.Default.ADVANCED : TooltipFlag.Default.NORMAL))));
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
}
