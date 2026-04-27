package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import com.verdantartifice.primalmagick.client.util.GuiUtils;
import com.verdantartifice.primalmagick.common.crafting.BlockIngredient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Display widget for showing all the possible blocks for a given block ingredient.  Used on
 * recipe pages.
 * 
 * @author Daedalus4096
 */
public class BlockIngredientWidget extends AbstractWidget {
    protected final BlockIngredient ingredient;
    protected ItemStack lastStack = ItemStack.EMPTY;
    protected ItemStack currentStack = ItemStack.EMPTY;
    
    public BlockIngredientWidget(@Nullable BlockIngredient ingredient, int xIn, int yIn) {
        super(xIn, yIn, 16, 16, Component.empty());
        this.ingredient = ingredient;
    }

    @Override
    public void extractWidgetRenderState(@NotNull GuiGraphicsExtractor pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.lastStack = this.currentStack;
        this.currentStack = this.getDisplayStack();
        if (!this.currentStack.isEmpty()) {
            GuiUtils.renderItemStack(pGuiGraphics, this.currentStack, this.getX(), this.getY(), this.getMessage().getString(), false);

            // Update the widget's tooltip if necessary
            this.updateTooltip();
        }
        
        // Don't allow the widget to become focused, to prevent keyboard navigation from moving the active tooltip
        this.setFocused(false);
    }
    
    protected void updateTooltip() {
        if (!ItemStack.isSameItemSameComponents(this.currentStack, this.lastStack)) {
            Minecraft mc = Minecraft.getInstance();
            this.setTooltip(Tooltip.create(CommonComponents.joinLines(this.currentStack.getTooltipLines(Item.TooltipContext.of(mc.level), mc.player, 
                    mc.options.advancedItemTooltips ? TooltipFlag.Default.ADVANCED : TooltipFlag.Default.NORMAL))));
        }
    }
    
    @Nonnull
    protected ItemStack getDisplayStack() {
        if (this.ingredient != null) {
            Block[] matching = this.ingredient.getMatchingBlocks();
            if (matching != null && matching.length > 0) {
                // Cycle through each matching stack of the ingredient and display them one at a time
                int index = (int)((System.currentTimeMillis() / 1000L) % matching.length);
                Block block = matching[index];
                if (block == null) {
                    ItemStack retVal = new ItemStack(Blocks.BARRIER);
                    retVal.set(DataComponents.CUSTOM_NAME, Component.translatable("grimoire.primalmagick.missing_block"));
                    return retVal;
                } else {
                    return new ItemStack(block);
                }
            }
        }
        return ItemStack.EMPTY;
    }
    
    @Override
    public boolean mouseClicked(@NotNull MouseButtonEvent event, boolean doubleClick) {
        // Disable click behavior
        return false;
    }

    @Override
    public void updateWidgetNarration(NarrationElementOutput output) {
    }
}
