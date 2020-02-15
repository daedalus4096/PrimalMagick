package com.verdantartifice.primalmagic.client.gui.grimoire.widgets;

import java.awt.Color;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.verdantartifice.primalmagic.client.gui.grimoire.GrimoireScreen;
import com.verdantartifice.primalmagic.common.sounds.SoundsPM;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.widget.button.Button;
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
    
    public AbstractTopicButton(int widthIn, int heightIn, int width, int height, String text, GrimoireScreen screen, IPressable onPress) {
        super(widthIn, heightIn, width, height, text, onPress);
        this.screen = screen;
    }

    public GrimoireScreen getScreen() {
        return this.screen;
    }
    
    @Override
    public void renderButton(int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        Minecraft mc = this.screen.getMinecraft();
        RenderSystem.pushMatrix();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        if (this.isHovered()) {
            // When hovering, highlight with a transparent grey background
            int alpha = 0x22;
            int color = (alpha << 24);
            fill(this.x - 5, this.y, this.x + this.width + 5, this.y + this.height, color);
        }
        int strWidth = mc.fontRenderer.getStringWidth(this.getMessage());
        int dy = (this.height - mc.fontRenderer.FONT_HEIGHT) / 2;
        if (strWidth <= this.width) {
            mc.fontRenderer.drawString(this.getMessage(), this.x, this.y + dy, Color.BLACK.getRGB());
        } else {
            // If the button text is too long, scale it down to fit on one line
            float scale = (float)this.width / (float)strWidth;
            RenderSystem.pushMatrix();
            RenderSystem.translatef(this.x, this.y + dy + (1.0F * scale), 0.0F);
            RenderSystem.scalef(scale, scale, scale);
            mc.fontRenderer.drawString(this.getMessage(), 0, 0, Color.BLACK.getRGB());
            RenderSystem.popMatrix();
        }
        RenderSystem.popMatrix();
    }
    
    @Override
    public void playDownSound(SoundHandler handler) {
        handler.play(SimpleSound.master(SoundsPM.PAGE, 1.0F, 1.0F));
    }
}
