package com.verdantartifice.primalmagic.client.gui.widgets;

import java.awt.Color;
import java.util.Collections;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.verdantartifice.primalmagic.client.util.GuiUtils;
import com.verdantartifice.primalmagic.common.sources.Source;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Base class for display widgets which show a source icon with amount.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
public abstract class AbstractSourceWidget extends Widget {
    protected Source source;
    protected int amount;

    public AbstractSourceWidget(Source source, int amount, int xIn, int yIn) {
        super(xIn, yIn, 16, 16, StringTextComponent.EMPTY);
        this.source = source;
        this.amount = amount;
    }
    
    @Override
    public void renderWidget(MatrixStack matrixStack, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        Minecraft mc = Minecraft.getInstance();
        boolean discovered = this.source.isDiscovered(mc.player);
        
        // Draw the colored source icon
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.pushMatrix();
        if (discovered) {
            mc.getTextureManager().bindTexture(this.source.getImage());
        } else {
            mc.getTextureManager().bindTexture(Source.getUnknownImage());
        }
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.translatef(this.x, this.y, 0.0F);
        RenderSystem.scaled(0.0625D, 0.0625D, 0.0625D);
        this.blit(matrixStack, 0, 0, 0, 0, 255, 255);
        RenderSystem.popMatrix();
        
        // Draw the amount string
        RenderSystem.pushMatrix();
        ITextComponent amountText = new StringTextComponent(Integer.toString(this.amount));
        int width = mc.fontRenderer.getStringWidth(amountText.getString());
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.translatef(this.x + 16 - width / 2, this.y + 12, 5.0F);
        RenderSystem.scaled(0.5D, 0.5D, 0.5D);
        mc.fontRenderer.drawTextWithShadow(matrixStack, amountText, 0.0F, 0.0F, Color.WHITE.getRGB());
        RenderSystem.popMatrix();
        
        // Draw the tooltip if applicable
        if (this.isHovered()) {
            ITextComponent sourceText = discovered ? 
                    new TranslationTextComponent(this.source.getNameTranslationKey()).mergeStyle(this.source.getChatColor()) :
                    new TranslationTextComponent(Source.getUnknownTranslationKey());
            ITextComponent labelText = new TranslationTextComponent(this.getTooltipTranslationKey(), this.amount, sourceText);
            GuiUtils.renderCustomTooltip(matrixStack, Collections.singletonList(labelText), this.x, this.y);
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
}
