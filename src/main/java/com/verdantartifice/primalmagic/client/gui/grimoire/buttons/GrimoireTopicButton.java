package com.verdantartifice.primalmagic.client.gui.grimoire.buttons;

import java.awt.Color;

import com.mojang.blaze3d.platform.GlStateManager;
import com.verdantartifice.primalmagic.client.gui.grimoire.GrimoireScreen;
import com.verdantartifice.primalmagic.client.util.GuiUtils;
import com.verdantartifice.primalmagic.common.sounds.SoundsPM;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.widget.button.Button;

public abstract class GrimoireTopicButton extends Button {
    protected GrimoireScreen screen;
    
    public GrimoireTopicButton(int widthIn, int heightIn, int width, int height, String text, GrimoireScreen screen, IPressable onPress) {
        super(widthIn, heightIn, width, height, text, onPress);
        this.screen = screen;
    }

    public GrimoireScreen getScreen() {
        return this.screen;
    }
    
    @Override
    public void renderButton(int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        Minecraft mc = this.screen.getMinecraft();
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        if (this.isHovered()) {
            int alpha = 0x22;
            int color = (alpha << 24);
            GuiUtils.drawRect(this.x - 5, this.y, this.x + this.width + 5, this.y + this.height, color);
        }
        int strWidth = mc.fontRenderer.getStringWidth(this.getMessage());
        int dy = (this.height - mc.fontRenderer.FONT_HEIGHT) / 2;
        if (strWidth <= this.width) {
            mc.fontRenderer.drawString(this.getMessage(), this.x, this.y + dy, Color.BLACK.getRGB());
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
    public void playDownSound(SoundHandler handler) {
        handler.play(SimpleSound.master(SoundsPM.PAGE, 1.0F, 1.0F));
    }
}
