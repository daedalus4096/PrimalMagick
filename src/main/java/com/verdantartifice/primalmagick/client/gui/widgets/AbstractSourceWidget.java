package com.verdantartifice.primalmagick.client.gui.widgets;

import java.awt.Color;
import java.util.Collections;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagick.client.util.GuiUtils;
import com.verdantartifice.primalmagick.common.sources.Source;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

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
    public void renderButton(PoseStack matrixStack, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        Minecraft mc = Minecraft.getInstance();
        boolean discovered = this.source.isDiscovered(mc.player);
        
        // Draw the colored source icon
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        matrixStack.pushPose();
        if (discovered) {
            RenderSystem.setShaderTexture(0, this.source.getImage());
        } else {
            RenderSystem.setShaderTexture(0, Source.getUnknownImage());
        }
        matrixStack.translate(this.getX(), this.getY(), 0.0F);
        matrixStack.scale(0.0625F, 0.0625F, 0.0625F);
        this.blit(matrixStack, 0, 0, 0, 0, 255, 255);
        matrixStack.popPose();
        
        // Draw the amount string
        matrixStack.pushPose();
        Component amountText = Component.literal(Integer.toString(this.amount));
        int width = mc.font.width(amountText.getString());
        matrixStack.translate(this.getX() + 16 - width / 2, this.getY() + 12, 5.0F);
        matrixStack.scale(0.5F, 0.5F, 0.5F);
        mc.font.drawShadow(matrixStack, amountText, 0.0F, 0.0F, Color.WHITE.getRGB());
        matrixStack.popPose();
        
        // Draw the tooltip if applicable
        if (this.isHoveredOrFocused()) {
            Component sourceText = discovered ? 
                    this.source.getNameText() :
                    Component.translatable(Source.getUnknownTranslationKey());
            Component labelText = Component.translatable(this.getTooltipTranslationKey(), this.amount, sourceText);
            GuiUtils.renderCustomTooltip(matrixStack, Collections.singletonList(labelText), this.getX(), this.getY());
        }
    }
    
    /**
     * Get the translation key for this widget's tooltip
     * @return the translation key for this widget's tooltip
     */
    protected abstract String getTooltipTranslationKey();
    
    @Override
    public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
        // Disable click behavior
        return false;
    }

    @Override
    public void updateWidgetNarration(NarrationElementOutput output) {
    }
}
