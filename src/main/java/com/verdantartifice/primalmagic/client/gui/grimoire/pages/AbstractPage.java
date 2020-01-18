package com.verdantartifice.primalmagic.client.gui.grimoire.pages;

import java.awt.Color;

import com.mojang.blaze3d.platform.GlStateManager;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.gui.grimoire.GrimoireScreen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Root class for all grimoire pages.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
public abstract class AbstractPage extends AbstractGui {
    private static final ResourceLocation GRIMOIRE_TEXTURE = new ResourceLocation(PrimalMagic.MODID, "textures/gui/grimoire.png");

    /**
     * Render this grimoire page
     * 
     * @param side which side of the grimoire to render on, 0 for left, 1 for right
     * @param x
     * @param y
     * @param mouseX
     * @param mouseY
     */
    public abstract void render(int side, int x, int y, int mouseX, int mouseY);
    
    /**
     * Get the translation key for this page's title
     * @return the translation key for this page's title
     */
    protected abstract String getTitleTranslationKey();
    
    /**
     * Create the widgets to show on this grimoire page
     * 
     * @param screen the screen object to which widgets should be added
     * @param side which side of the grimoire to render on, 0 for left, 1 for right
     * @param x
     * @param y
     */
    public abstract void initWidgets(GrimoireScreen screen, int side, int x, int y);
    
    protected boolean renderTopTitleBar() {
        return true;
    }
    
    protected void renderTitle(int side, int x, int y, int mouseX, int mouseY) {
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        Minecraft mc = Minecraft.getInstance();
        mc.getTextureManager().bindTexture(GRIMOIRE_TEXTURE);
        if (this.renderTopTitleBar()) {
            this.blit(x + 10 + (side * 140), y + 18, 24, 184, 96, 5);   // Render the separator bar above the title text
        }
        this.blit(x + 10 + (side * 140), y + 35, 24, 184, 96, 5);   // Render the separator bar below the title text
        String headerText = new TranslationTextComponent(this.getTitleTranslationKey()).getFormattedText();
        int offset = mc.fontRenderer.getStringWidth(headerText);
        int indent = 124;
        if (offset <= 124) {
            mc.fontRenderer.drawString(headerText, x - 3 + (side * 140) + (indent / 2) - (offset / 2), y + 25, Color.BLACK.getRGB());
        } else {
            // Scale down the title text if necessary to make it fit on one line
            float scale = 124.0F / offset;
            GlStateManager.pushMatrix();
            GlStateManager.translatef(x - 3 + (side * 140) + (indent / 2) - (offset / 2 * scale), y + 25 + (1.0F * scale), 0.0F);
            GlStateManager.scalef(scale, scale, scale);
            mc.fontRenderer.drawString(headerText, 0, 0, Color.BLACK.getRGB());
            GlStateManager.popMatrix();
        }
    }
}
