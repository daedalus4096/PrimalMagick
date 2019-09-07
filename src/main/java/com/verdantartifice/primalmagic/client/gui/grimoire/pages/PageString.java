package com.verdantartifice.primalmagic.client.gui.grimoire.pages;

import java.awt.Color;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PageString implements IPageElement {
    protected String str;
    
    public PageString(String str) {
        this.str = str;
    }
    
    public String getString() {
        return this.str;
    }

    @Override
    public void render(int side, int x, int y) {
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getInstance().fontRenderer.drawString(this.str.replace("~B", ""), x - 15 + (side * 152), y - 6, Color.BLACK.getRGB());
    }

    @Override
    public int getNextY(int y) {
        Minecraft mc = Minecraft.getInstance();
        y += mc.fontRenderer.FONT_HEIGHT;
        if (this.str.endsWith("~B")) {
            y += (int)(mc.fontRenderer.FONT_HEIGHT * 0.66D);
        }
        return y;
    }
}
