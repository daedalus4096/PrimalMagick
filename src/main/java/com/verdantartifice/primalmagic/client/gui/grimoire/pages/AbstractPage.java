package com.verdantartifice.primalmagic.client.gui.grimoire.pages;

import java.awt.Color;

import com.mojang.blaze3d.platform.GlStateManager;
import com.verdantartifice.primalmagic.PrimalMagic;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class AbstractPage extends AbstractGui {
    private static final ResourceLocation GRIMOIRE_TEXTURE = new ResourceLocation(PrimalMagic.MODID, "textures/gui/grimoire.png");

    public abstract void render(int side, int x, int y, int mouseX, int mouseY);
    
    protected abstract String getTitleTranslationKey();
    
    protected boolean renderTopTitleBar() {
        return true;
    }
    
    protected void renderTitle(int side, int x, int y, int mouseX, int mouseY) {
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        Minecraft mc = Minecraft.getInstance();
        mc.getTextureManager().bindTexture(GRIMOIRE_TEXTURE);
        if (this.renderTopTitleBar()) {
            this.blit(x + 10 + (side * 140), y + 18, 24, 184, 96, 4);
        }
        this.blit(x + 10 + (side * 140), y + 35, 24, 184, 96, 4);
        String headerText = new TranslationTextComponent(this.getTitleTranslationKey()).getFormattedText();
        int offset = mc.fontRenderer.getStringWidth(headerText);
        int indent = 124;
        if (offset <= 124) {
            mc.fontRenderer.drawString(headerText, x - 3 + (side * 140) + (indent / 2) - (offset / 2), y + 25, Color.BLACK.getRGB());
        } else {
            float scale = 124.0F / offset;
            GlStateManager.pushMatrix();
            GlStateManager.translatef(x - 3 + (side * 140) + (indent / 2) - (offset / 2 * scale), y + 25 + (1.0F * scale), 0.0F);
            GlStateManager.scalef(scale, scale, scale);
            mc.fontRenderer.drawString(headerText, 0, 0, Color.BLACK.getRGB());
            GlStateManager.popMatrix();
        }
    }
}
