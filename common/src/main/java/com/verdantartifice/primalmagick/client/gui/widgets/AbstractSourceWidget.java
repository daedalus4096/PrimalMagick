package com.verdantartifice.primalmagick.client.gui.widgets;

import com.verdantartifice.primalmagick.client.util.GuiUtils;
import com.verdantartifice.primalmagick.common.sources.Source;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.util.List;

/**
 * Base class for display widgets which show a source icon with amount.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractSourceWidget extends AbstractWidget {
    protected Source source;
    protected int amount;

    public AbstractSourceWidget(Source source, int amount, int xIn, int yIn) {
        super(xIn, yIn, 16, 16, Component.empty());
        this.source = source;
        this.amount = amount;
    }
    
    public Source getSource() {
        return this.source;
    }
    
    public int getAmount() {
        return this.amount;
    }
    
    public void setAmount(int amount) {
        this.amount = amount;
    }
    
    @Override
    public void renderWidget(GuiGraphicsExtractor guiGraphics, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        Minecraft mc = Minecraft.getInstance();
        boolean discovered = this.source.isDiscovered(mc.player);
        
        // Draw the colored source icon
        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().translate(this.getX(), this.getY());
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, discovered ? this.source.getImage() : Source.getUnknownImage(), 0, 0, 32, 32);
        guiGraphics.pose().popMatrix();
        
        // Draw the amount string
        guiGraphics.pose().pushMatrix();
        Component amountText = Component.literal(this.getAmountString());
        int width = mc.font.width(amountText.getString());
        guiGraphics.pose().translate(this.getX() + 16 - width / 2, this.getY() + 12);
        guiGraphics.pose().scale(0.5F, 0.5F);
        guiGraphics.drawString(mc.font, amountText, 0, 0, this.getAmountStringColor());
        guiGraphics.pose().popMatrix();
        
        // Draw the tooltip if applicable
        if (this.isHoveredOrFocused()) {
            GuiUtils.renderCustomTooltip(guiGraphics, this.getTooltipLines(), p_renderButton_1_, p_renderButton_2_);
        }
    }
    
    @Override
    public boolean mouseClicked(@NotNull MouseButtonEvent event, boolean isDoubleClick) {
        // Disable click behavior
        return false;
    }

    @Override
    public void updateWidgetNarration(@NotNull NarrationElementOutput output) {
    }
    
    protected String getAmountString() {
        return Integer.toString(this.getAmount());
    }
    
    protected int getAmountStringColor() {
        return Color.WHITE.getRGB();
    }
    
    protected Component getSourceText() {
        Minecraft mc = Minecraft.getInstance();
        boolean discovered = this.source.isDiscovered(mc.player);
        return discovered ? 
                this.source.getNameText() :
                Component.translatable(Source.getUnknownTranslationKey());
    }
    
    protected abstract List<Component> getTooltipLines();
}
