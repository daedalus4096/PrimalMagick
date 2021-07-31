package com.verdantartifice.primalmagic.client.gui.widgets.grimoire;

import java.awt.Color;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagic.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagic.common.sounds.SoundsPM;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Base class for research topic selector buttons (e.g. research disciplines).
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
public abstract class AbstractTopicButton extends Button {
    protected GrimoireScreen screen;
    
    public AbstractTopicButton(int widthIn, int heightIn, int width, int height, Component text, GrimoireScreen screen, OnPress onPress) {
        super(widthIn, heightIn, width, height, text, onPress);
        this.screen = screen;
    }

    public GrimoireScreen getScreen() {
        return this.screen;
    }
    
    @Override
    public void renderButton(PoseStack matrixStack, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        Minecraft mc = this.screen.getMinecraft();
        matrixStack.pushPose();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        if (this.isHovered()) {
            // When hovering, highlight with a transparent grey background
            int alpha = 0x22;
            int color = (alpha << 24);
            fill(matrixStack, this.x - 5, this.y, this.x + this.width + 5, this.y + this.height, color);
        }
        int strWidth = mc.font.width(this.getMessage().getString());
        int dy = (this.height - mc.font.lineHeight) / 2;
        if (strWidth <= this.width) {
            mc.font.draw(matrixStack, this.getMessage(), this.x, this.y + dy, Color.BLACK.getRGB());
        } else {
            // If the button text is too long, scale it down to fit on one line
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
    public void playDownSound(SoundManager handler) {
        handler.play(SimpleSoundInstance.forUI(SoundsPM.PAGE.get(), 1.0F, 1.0F));
    }
}
