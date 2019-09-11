package com.verdantartifice.primalmagic.client.gui.grimoire.widgets;

import java.awt.Color;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SectionHeaderWidget extends Widget {
    public SectionHeaderWidget(int xIn, int yIn, String msg) {
        super(xIn, yIn, 135, 18, msg);
    }

    @Override
    public void renderButton(int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        Minecraft mc = Minecraft.getInstance();
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        int strWidth = mc.fontRenderer.getStringWidth(this.getMessage());
        int dy = (this.height - mc.fontRenderer.FONT_HEIGHT) / 2;
        if (strWidth <= this.width) {
            mc.fontRenderer.drawString(this.getMessage(), this.x + this.width / 2 - strWidth / 2, this.y + (this.height - 8) / 2, Color.BLACK.getRGB());
        } else {
            float scale = (float)this.width / (float)strWidth;
            GlStateManager.pushMatrix();
            GlStateManager.translatef(this.x, this.y + dy + (1.0F * scale), 0.0F);
            GlStateManager.scalef(scale, scale, scale);
            mc.fontRenderer.drawString(this.getMessage(), 0, 0, Color.BLACK.getRGB());
            GlStateManager.popMatrix();
        }
        GlStateManager.popMatrix();
    }
    
    @Override
    public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
        // Disable click behavior
        return false;
    }
}
