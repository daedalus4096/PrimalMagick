package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import com.verdantartifice.primalmagick.client.util.GuiUtils;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import java.awt.Color;

/**
 * Display widget for showing a single itemstack.  Used on the requirements and recipe pages.
 * 
 * @author Daedalus4096
 */
public class ItemStackWidget extends AbstractWidget {
    private static final Identifier COMPLETE = ResourceUtils.loc("grimoire/complete");

    protected ItemStack stack;
    protected boolean isComplete;
    
    public ItemStackWidget(ItemStack stack, int x, int y, boolean isComplete) {
        super(x, y, 16, 16, Component.empty());
        this.stack = stack;
        this.isComplete = isComplete;

        Minecraft mc = Minecraft.getInstance();
        this.setTooltip(Tooltip.create(CommonComponents.joinLines(this.stack.getTooltipLines(Item.TooltipContext.of(mc.level), mc.player, 
                mc.options.advancedItemTooltips ? TooltipFlag.Default.ADVANCED : TooltipFlag.Default.NORMAL))));
}
    
    @Override
    public void extractWidgetRenderState(@NotNull GuiGraphicsExtractor pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        Minecraft mc = Minecraft.getInstance();
        
        // Draw stack icon
        GuiUtils.renderItemStack(pGuiGraphics, this.stack, this.getX(), this.getY(), this.getMessage().getString(), false);
        
        // Draw amount string if applicable
        if (this.stack.getCount() > 1) {
            Component amountText = Component.literal(Integer.toString(this.stack.getCount()));
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
        
        // Don't allow the widget to become focused, to prevent keyboard navigation from moving the active tooltip
        this.setFocused(false);
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
