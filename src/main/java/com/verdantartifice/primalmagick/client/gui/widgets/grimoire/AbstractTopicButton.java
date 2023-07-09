package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import java.awt.Color;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagick.common.sounds.SoundsPM;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;

/**
 * Base class for research topic selector buttons (e.g. research disciplines).
 * 
 * @author Daedalus4096
 */
public abstract class AbstractTopicButton extends Button {
    protected GrimoireScreen screen;
    protected AbstractIndexIcon icon;
    
    public AbstractTopicButton(int x, int y, int width, int height, Component text, GrimoireScreen screen, AbstractIndexIcon icon, OnPress onPress) {
        super(Button.builder(text, onPress).bounds(x, y, width, height));
        this.screen = screen;
        this.icon = icon;
    }

    public GrimoireScreen getScreen() {
        return this.screen;
    }
    
    @Override
    public void renderWidget(GuiGraphics guiGraphics, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        Minecraft mc = this.screen.getMinecraft();
        guiGraphics.pose().pushPose();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        if (this.isHoveredOrFocused()) {
            // When hovering, highlight with a transparent grey background
            int alpha = 0x22;
            int color = (alpha << 24);
            guiGraphics.fill(this.getX() - 5, this.getY(), this.getX() + this.width + 5, this.getY() + this.height, color);
        }
        int strWidth = mc.font.width(this.getMessage().getString());
        int dx = this.icon == null ? 0 : (this.icon.isLarge() ? 16 : 11);
        int dy = (this.height - mc.font.lineHeight) / 2;
        if (strWidth <= (this.width - dx)) {
            guiGraphics.drawString(mc.font, this.getMessage(), this.getX() + dx, this.getY() + dy, Color.BLACK.getRGB(), false);
            if (this.icon != null) {
                this.icon.render(guiGraphics, this.getX() - 2, this.getY() + dy - (this.icon.isLarge() ? 4 : 1));
            }
        } else {
            // If the button text is too long, scale it down to fit on one line
            float scale = (float)(this.width - dx) / (float)strWidth;
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(this.getX() + dx, this.getY() + dy + (1.0F * scale), 0.0F);
            guiGraphics.pose().scale(scale, scale, scale);
            guiGraphics.drawString(mc.font, this.getMessage(), 0, 0, Color.BLACK.getRGB(), false);
            guiGraphics.pose().popPose();
            if (this.icon != null) {
                this.icon.render(guiGraphics, this.getX() - 2, this.getY() + dy - (this.icon.isLarge() ? 4 : 1));
            }
        }
        guiGraphics.pose().popPose();
    }
    
    @Override
    public void playDownSound(SoundManager handler) {
        handler.play(SimpleSoundInstance.forUI(SoundsPM.PAGE.get(), 1.0F, 1.0F));
    }
}
