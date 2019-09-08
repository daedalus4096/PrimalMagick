package com.verdantartifice.primalmagic.client.util;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiUtils {
    public static void drawRect(int left, int top, int right, int bottom, int color) {
        int temp;
        if (left < right) {
            temp = left;
            left = right;
            right = temp;
        }
        if (top < bottom) {
            temp = top;
            top = bottom;
            bottom = temp;
        }

        float a = (float)(color >> 24 & 255) / 255.0F;
        float r = (float)(color >> 16 & 255) / 255.0F;
        float g = (float)(color >> 8 & 255) / 255.0F;
        float b = (float)(color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture();
        GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color4f(r, g, b, a);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferbuilder.pos((double)left, (double)bottom, 0.0D).endVertex();
        bufferbuilder.pos((double)right, (double)bottom, 0.0D).endVertex();
        bufferbuilder.pos((double)right, (double)top, 0.0D).endVertex();
        bufferbuilder.pos((double)left, (double)top, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture();
        GlStateManager.disableBlend();
    }
    
    public static boolean renderItemStack(ItemStack stack, int x, int y, String text, boolean hideStackOverlay) {
        boolean retVal = false;
        if (stack != null && !stack.isEmpty()) {
            GlStateManager.color3f(1.0F, 1.0F, 1.0F);
            Minecraft mc = Minecraft.getInstance();
            ItemRenderer itemRenderer = mc.getItemRenderer();
            
            boolean isLightingEnabled = GL11.glIsEnabled(GL11.GL_LIGHTING);
            boolean isRescaleNormalEnabled = GL11.glIsEnabled(32826);
            
            GlStateManager.pushMatrix();
            GlStateManager.translatef(0.0F, 0.0F, 32.0F);
            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableRescaleNormal();
            GlStateManager.enableLighting();
            RenderHelper.enableGUIStandardItemLighting();
            // OpenGlHelper.setLightmapTextureCoords ???
            itemRenderer.renderItemAndEffectIntoGUI(stack, x, y);
            if (!hideStackOverlay) {
                itemRenderer.renderItemOverlayIntoGUI(mc.fontRenderer, stack, x, y, text);
            }
            GlStateManager.popMatrix();
            
            if (isRescaleNormalEnabled) {
                GlStateManager.enableRescaleNormal();
            } else {
                GlStateManager.disableRescaleNormal();
            }
            if (isLightingEnabled) {
                GlStateManager.enableLighting();
            } else {
                GlStateManager.disableLighting();
            }
            
            retVal = true;
        }
        return retVal;
    }
}
