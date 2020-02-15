package com.verdantartifice.primalmagic.client.gui.grimoire.pages;

import java.awt.Color;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * A string element to be rendered on a grimoire page.
 * 
 * @author Daedalus4096
 */
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
        // Render this element's string to the screen
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getInstance().fontRenderer.drawString(this.str.replace("~B", ""), x - 1 + (side * 138), y - 6, Color.BLACK.getRGB());
    }

    @Override
    public int getNextY(int y) {
        Minecraft mc = Minecraft.getInstance();
        y += mc.fontRenderer.FONT_HEIGHT;
        if (this.str.endsWith("~B")) {
            // If this element ends with a <BR> tag, leave some extra vertical space after it
            y += (int)(mc.fontRenderer.FONT_HEIGHT * 0.66D);
        }
        return y;
    }
}
