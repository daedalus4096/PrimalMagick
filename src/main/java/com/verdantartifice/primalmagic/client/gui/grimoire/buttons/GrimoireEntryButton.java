package com.verdantartifice.primalmagic.client.gui.grimoire.buttons;

import java.awt.Color;

import com.mojang.blaze3d.platform.GlStateManager;
import com.verdantartifice.primalmagic.client.gui.grimoire.GrimoireScreen;
import com.verdantartifice.primalmagic.common.research.ResearchEntry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class GrimoireEntryButton extends Button {
    protected GrimoireScreen screen;
    protected ResearchEntry entry;

    public GrimoireEntryButton(int widthIn, int heightIn, String text, GrimoireScreen screen, ResearchEntry entry) {
        super(widthIn, heightIn, 135, 18, text, new Handler());
        this.screen = screen;
        this.entry = entry;
    }
    
    public GrimoireScreen getScreen() {
        return this.screen;
    }
    
    public ResearchEntry getEntry() {
        return this.entry;
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
            this.drawRect(this.x - 5, this.y, this.x + this.width + 5, this.y + this.height, color);
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
    
    private void drawRect(int left, int top, int right, int bottom, int color) {
        if (left < right)
        {
            int i = left;
            left = right;
            right = i;
        }

        if (top < bottom)
        {
            int j = top;
            top = bottom;
            bottom = j;
        }

        float f3 = (float)(color >> 24 & 255) / 255.0F;
        float f = (float)(color >> 16 & 255) / 255.0F;
        float f1 = (float)(color >> 8 & 255) / 255.0F;
        float f2 = (float)(color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture();
        GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color4f(f, f1, f2, f3);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferbuilder.pos((double)left, (double)bottom, 0.0D).endVertex();
        bufferbuilder.pos((double)right, (double)bottom, 0.0D).endVertex();
        bufferbuilder.pos((double)right, (double)top, 0.0D).endVertex();
        bufferbuilder.pos((double)left, (double)top, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture();
        GlStateManager.disableBlend();
    }

    private static class Handler implements IPressable {
        @Override
        public void onPress(Button button) {
            if (button instanceof GrimoireEntryButton) {
                GrimoireEntryButton geb = (GrimoireEntryButton)button;
                GrimoireScreen.HISTORY.add(geb.getScreen().getContainer().getTopic());
                geb.getScreen().getContainer().setTopic(geb.getEntry());
                geb.getScreen().getMinecraft().displayGuiScreen(new GrimoireScreen(
                    geb.getScreen().getContainer(),
                    geb.getScreen().getPlayerInventory(),
                    geb.getScreen().getTitle()
                ));
            }
        }
    }

}
