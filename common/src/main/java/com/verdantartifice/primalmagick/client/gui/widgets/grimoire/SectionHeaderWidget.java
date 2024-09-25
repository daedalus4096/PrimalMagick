package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import java.awt.Color;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

/**
 * Display widget to mark a new section of research entries (e.g. "Updated") in the grimoire index.
 * 
 * @author Daedalus4096
 */
public class SectionHeaderWidget extends AbstractWidget {
    public SectionHeaderWidget(int xIn, int yIn, Component msg) {
        super(xIn, yIn, 123, 12, msg);
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        Minecraft mc = Minecraft.getInstance();
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0.0F, 0.0F, 1.0F);  // Bump up slightly in the Z-order to prevent the underline from being swallowed
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        int strWidth = mc.font.width(this.getMessage().getString());
        int dy = (this.height - mc.font.lineHeight) / 2;
        if (strWidth <= this.width) {
            guiGraphics.drawString(mc.font, this.getMessage(), this.getX() + this.width / 2 - strWidth / 2, this.getY() + (this.height - 8) / 2, Color.BLACK.getRGB(), false);
        } else {
            // Scale the string down to fit on one line, if need be
            float scale = (float)this.width / (float)strWidth;
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(this.getX(), this.getY() + dy + (1.0F * scale), 0.0F);
            guiGraphics.pose().scale(scale, scale, scale);
            guiGraphics.drawString(mc.font, this.getMessage(), 0, 0, Color.BLACK.getRGB(), false);
            guiGraphics.pose().popPose();
        }
        guiGraphics.pose().popPose();
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
