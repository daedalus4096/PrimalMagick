package com.verdantartifice.primalmagic.client.gui.widgets;

import java.awt.Color;
import java.util.Collections;

import com.mojang.blaze3d.platform.GlStateManager;
import com.verdantartifice.primalmagic.client.util.GuiUtils;
import com.verdantartifice.primalmagic.common.sources.Source;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ManaCostWidget extends Widget {
    protected Source source;
    protected int amount;

    public ManaCostWidget(Source source, int amount, int xIn, int yIn) {
        super(xIn, yIn, 16, 16, "");
        this.source = source;
        this.amount = amount;
    }
    
    @Override
    public void renderButton(int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        Minecraft mc = Minecraft.getInstance();
        boolean discovered = this.source.isDiscovered(mc.player);
        
        // Draw the colored source icon
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.pushMatrix();
        if (discovered) {
            mc.getTextureManager().bindTexture(this.source.getImage());
            Color sourceColor = new Color(this.source.getColor());
            float r = sourceColor.getRed() / 255.0F;
            float g = sourceColor.getGreen() / 255.0F;
            float b = sourceColor.getBlue() / 255.0F;
            GlStateManager.color4f(r, g, b, 1.0F);
        } else {
            mc.getTextureManager().bindTexture(Source.getUnknownImage());
            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        }
        GlStateManager.translatef(this.x, this.y, 0.0F);
        GlStateManager.scaled(0.0625D, 0.0625D, 0.0625D);
        this.blit(0, 0, 0, 0, 255, 255);
        GlStateManager.popMatrix();
        
        // Draw the amount string
        GlStateManager.pushMatrix();
        ITextComponent amountText = new StringTextComponent(Integer.toString(this.amount));
        int width = mc.fontRenderer.getStringWidth(amountText.getFormattedText());
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.translatef(this.x + 16 - width / 2, this.y + 12, 5.0F);
        GlStateManager.scaled(0.5D, 0.5D, 0.5D);
        mc.fontRenderer.drawStringWithShadow(amountText.getFormattedText(), 0.0F, 0.0F, Color.WHITE.getRGB());
        GlStateManager.popMatrix();
        
        // Draw the tooltip if applicable
        if (this.isHovered()) {
            ITextComponent sourceText = discovered ? 
                    new TranslationTextComponent(this.source.getNameTranslationKey()).applyTextStyle(this.source.getChatColor()) :
                    new TranslationTextComponent(Source.getUnknownTranslationKey());
            ITextComponent labelText = new TranslationTextComponent("primalmagic.crafting.mana_tooltip", this.amount, sourceText);
            GuiUtils.renderCustomTooltip(Collections.singletonList(labelText), this.x, this.y);
        }
    }
    
    @Override
    public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
        // Disable click behavior
        return false;
    }
}
