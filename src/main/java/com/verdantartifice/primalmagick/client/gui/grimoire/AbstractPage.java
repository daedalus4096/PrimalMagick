package com.verdantartifice.primalmagick.client.gui.grimoire;

import java.awt.Color;

import javax.annotation.Nullable;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

/**
 * Root class for all grimoire pages.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractPage extends GuiComponent {
    private static final ResourceLocation GRIMOIRE_TEXTURE = new ResourceLocation(PrimalMagick.MODID, "textures/gui/grimoire.png");

    /**
     * Render this grimoire page
     * 
     * @param side which side of the grimoire to render on, 0 for left, 1 for right
     * @param x
     * @param y
     * @param mouseX
     * @param mouseY
     */
    public abstract void render(PoseStack matrixStack, int side, int x, int y, int mouseX, int mouseY);
    
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
    
    protected void renderTitle(PoseStack matrixStack, int side, int x, int y, int mouseX, int mouseY, @Nullable ResourceLocation icon) {
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        Minecraft mc = Minecraft.getInstance();
        RenderSystem.setShaderTexture(0, GRIMOIRE_TEXTURE);
        if (this.renderTopTitleBar()) {
            this.blit(matrixStack, x + 10 + (side * 140), y + 18, 24, 184, 96, 5);   // Render the separator bar above the title text
        }
        this.blit(matrixStack, x + 10 + (side * 140), y + 35, 24, 184, 96, 5);   // Render the separator bar below the title text
        Component headerText = Component.translatable(this.getTitleTranslationKey());
        int width = mc.font.width(headerText.getString());
        int indent = 124;
        if (width <= 124) {
            mc.font.draw(matrixStack, headerText, x - 3 + (side * 140) + (indent / 2) - (width / 2), y + 25, Color.BLACK.getRGB());
            if (icon != null) {
                matrixStack.pushPose();
                matrixStack.translate(x - 3 + (side * 140) + (indent / 2) - (width / 2) - 17, y + 21, 0.0F);
                matrixStack.scale(0.06F, 0.06F, 0.06F);
                RenderSystem.setShaderTexture(0, icon);
                this.blit(matrixStack, 0, 0, 0, 0, 255, 255);
                matrixStack.popPose();
            }
        } else {
            // Scale down the title text if necessary to make it fit on one line
            float scale = 124.0F / width;
            matrixStack.pushPose();
            matrixStack.translate(x - 3 + (side * 140) + (indent / 2) - (width / 2 * scale), y + 25 + (1.0F * scale), 0.0F);
            matrixStack.scale(scale, scale, scale);
            mc.font.draw(matrixStack, headerText, 0, 0, Color.BLACK.getRGB());
            if (icon != null) {
                matrixStack.pushPose();
                matrixStack.translate(x - 3 + (side * 140) + (indent / 2) - (width / 2 * scale) - 17, y + 21, 0.0F);
                matrixStack.scale(0.06F, 0.06F, 0.06F);
                RenderSystem.setShaderTexture(0, icon);
                this.blit(matrixStack, 0, 0, 0, 0, 255, 255);
                matrixStack.popPose();
            }
            matrixStack.popPose();
        }
    }
}
