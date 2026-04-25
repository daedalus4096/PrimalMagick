package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import com.verdantartifice.primalmagick.client.util.GuiUtils;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Display widget for showing all the possible itemstacks for a given tag.  Used
 * on the requirements pages.
 * 
 * @author Daedalus4096
 */
public class ItemTagWidget extends AbstractWidget {
    private static final Identifier COMPLETE = ResourceUtils.loc("grimoire/complete");

    protected final TagKey<Item> tag;
    protected final int amount;
    protected final boolean isComplete;
    protected ItemStack lastStack = ItemStack.EMPTY;
    protected ItemStack currentStack = ItemStack.EMPTY;

    public ItemTagWidget(TagKey<Item> tag, int amount, int x, int y, boolean isComplete) {
        super(x, y, 16, 16, Component.empty());
        this.tag = tag;
        this.amount = amount;
        this.isComplete = isComplete;
    }
    
    public ItemTagWidget(TagKey<Item> tag, int x, int y, boolean isComplete) {
        this(tag, 1, x, y, isComplete);
    }
    
    @Override
    public void extractWidgetRenderState(@NotNull GuiGraphicsExtractor pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        Minecraft mc = Minecraft.getInstance();
        this.lastStack = this.currentStack;
        this.currentStack = this.getDisplayStack();
        if (!this.currentStack.isEmpty()) {
            GuiUtils.renderItemStack(pGuiGraphics, this.currentStack, this.getX(), this.getY(), this.getMessage().getString(), false);
            
            // Draw amount string if applicable
            if (this.amount > 1) {
                Component amountText = Component.literal(Integer.toString(this.amount));
                int width = mc.font.width(amountText.getString());
                pGuiGraphics.pose().pushMatrix();
                pGuiGraphics.pose().translate(this.getX() + 16 - width / 2, this.getY() + 12);
                pGuiGraphics.pose().scale(0.5F, 0.5F);
                pGuiGraphics.text(mc.font, amountText, 0, 0, Color.WHITE.getRGB());
                pGuiGraphics.pose().popMatrix();
            }
            
            if (this.isComplete) {
                // Render completion checkmark if appropriate
                pGuiGraphics.pose().pushMatrix();
                pGuiGraphics.pose().translate(this.getX() + 8, this.getY());
                pGuiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, COMPLETE, 0, 0, 10, 10);
                pGuiGraphics.pose().popMatrix();
            }
            
            // Update the widget tooltip if necessary
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
        List<Item> tagContents = new ArrayList<>();
        Services.ITEMS_REGISTRY.getTag(this.tag).ifPresent(tag -> tag.forEach(tagContents::add));
        if (!tagContents.isEmpty()) {
            // Cycle through each matching stack of the tag and display them one at a time
            int index = (int)((System.currentTimeMillis() / 1000L) % tagContents.size());
            return new ItemStack(tagContents.get(index), this.amount);
        }
        return ItemStack.EMPTY;
    }
    
    @Override
    public boolean mouseClicked(@NotNull MouseButtonEvent event, boolean isDoubleClick) {
        // Disable click behavior
        return false;
    }

    @Override
    public void updateWidgetNarration(@NotNull NarrationElementOutput output) {
    }
}
