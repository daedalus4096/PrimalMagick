package com.verdantartifice.primalmagic.client.gui.widgets.grimoire;

import java.awt.Color;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Display widget to mark a new section of research entries (e.g. "Updated") in the grimoire index.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
public class SectionHeaderWidget extends AbstractWidget {
    public SectionHeaderWidget(int xIn, int yIn, Component msg) {
        super(xIn, yIn, 123, 12, msg);
    }

    @Override
    public void renderButton(PoseStack matrixStack, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        Minecraft mc = Minecraft.getInstance();
        matrixStack.pushPose();
        matrixStack.translate(0.0F, 0.0F, 1.0F);  // Bump up slightly in the Z-order to prevent the underline from being swallowed
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        int strWidth = mc.font.width(this.getMessage().getString());
        int dy = (this.height - mc.font.lineHeight) / 2;
        if (strWidth <= this.width) {
            mc.font.draw(matrixStack, this.getMessage(), this.x + this.width / 2 - strWidth / 2, this.y + (this.height - 8) / 2, Color.BLACK.getRGB());
        } else {
            // Scale the string down to fit on one line, if need be
            float scale = (float)this.width / (float)strWidth;
            matrixStack.pushPose();
            matrixStack.translate(this.x, this.y + dy + (1.0F * scale), 0.0F);
            matrixStack.scale(scale, scale, scale);
            mc.font.draw(matrixStack, this.getMessage(), 0, 0, Color.BLACK.getRGB());
            matrixStack.popPose();
        }
        matrixStack.popPose();
    }
    
    @Override
    public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
        // Disable click behavior
        return false;
    }

    @Override
    public void updateNarration(NarrationElementOutput output) {
    }
}
