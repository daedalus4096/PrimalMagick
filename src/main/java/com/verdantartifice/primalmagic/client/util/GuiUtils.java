package com.verdantartifice.primalmagic.client.util;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
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
    
    public static void renderCustomTooltip(List<ITextComponent> textList, int x, int y) {
        renderCustomTooltip(textList, x, y, false);
    }
    
    public static void renderCustomTooltip(List<ITextComponent> textList, int x, int y, boolean ignoreMouse) {
        if (textList == null || textList.isEmpty()) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        double scaleFactor = mc.mainWindow.getGuiScaleFactor();
        
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableLighting();

        int max = 240;
        double mouseX = mc.mouseHelper.getMouseX();
        boolean flip = false;
        if (!ignoreMouse && ((max + 24) * scaleFactor + mouseX > mc.mainWindow.getWidth())) {
            max = (int)((mc.mainWindow.getWidth() - mouseX) / scaleFactor - 24);
            if (max < 120) {
                max = 240;
                flip = true;
            }
        }
        
        List<String> parsedList = new ArrayList<>();
        for (ITextComponent text : textList)  {
            List<String> strList = mc.fontRenderer.listFormattedStringToWidth(text.getFormattedText(), max);
            parsedList.addAll(strList);
        }
        
        int totalHeight = -2;
        int widestLineWidth = 0;
        for (String str : parsedList) {
            int lineWidth = mc.fontRenderer.getStringWidth(str);
            if (lineWidth > widestLineWidth) {
                widestLineWidth = lineWidth;
            }
            totalHeight += mc.fontRenderer.FONT_HEIGHT;
        }
        
        int sX = x + 12;
        int sY = y - 12;
        if (parsedList.size() > 1) {
            totalHeight += 2;
        }
        if (sY + totalHeight > mc.mainWindow.getScaledHeight()) {
            sY = mc.mainWindow.getScaledHeight() - totalHeight - 5;
        }
        if (flip) {
            sX -= (widestLineWidth + 24);
        }
        
        float prevZ = mc.getItemRenderer().zLevel;
        mc.getItemRenderer().zLevel = 300.0F;
        int var10 = -267386864;
        drawGradientRect(sX - 3, sY - 4, sX + widestLineWidth + 3, sY - 3, var10, var10);
        drawGradientRect(sX - 3, sY + totalHeight + 3, sX + widestLineWidth + 3, sY + totalHeight + 4, var10, var10);
        drawGradientRect(sX - 3, sY - 3, sX + widestLineWidth + 3, sY + totalHeight + 3, var10, var10);
        drawGradientRect(sX - 4, sY - 3, sX - 3, sY + totalHeight + 3, var10, var10);
        drawGradientRect(sX + widestLineWidth + 3, sY - 3, sX + widestLineWidth + 4, sY + totalHeight + 3, var10, var10);
        int var11 = 1347420415;
        int var12 = (var11 & 0xFEFEFE) >> 1 | var11 & 0xFF000000;
        drawGradientRect(sX - 3, sY - 3 + 1, sX - 3 + 1, sY + totalHeight + 3 - 1, var11, var12);
        drawGradientRect(sX + widestLineWidth + 2, sY - 3 + 1, sX + widestLineWidth + 3, sY + totalHeight + 3 - 1, var11, var12);
        drawGradientRect(sX - 3, sY - 3, sX + widestLineWidth + 3, sY - 3 + 1, var11, var11);
        drawGradientRect(sX - 3, sY + totalHeight + 2, sX + widestLineWidth + 3, sY + totalHeight + 3, var12, var12);

        for (int i = 0; i < parsedList.size(); i++) {
            GlStateManager.pushMatrix();
            GlStateManager.translatef(sX, sY, 0.0F);
            
            String str = parsedList.get(i);
            
            GlStateManager.pushMatrix();
            sY += mc.fontRenderer.FONT_HEIGHT;
            GlStateManager.translatef(0.0F, 0.0F, 301.0F);
            mc.fontRenderer.drawStringWithShadow(str, 0.0F, 0.0F, -1);
            GlStateManager.popMatrix();
            if (i == 0) {
                sY += 2;
            }
            
            GlStateManager.popMatrix();
        }

        mc.getItemRenderer().zLevel = prevZ;
        GlStateManager.enableLighting();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enableRescaleNormal();
    }
    
    public static void drawGradientRect(int left, int top, int right, int bottom, int color1, int color2) {
        boolean blendOn = GL11.glIsEnabled(3042);
        float a1 = (color1 >> 24 & 0xFF) / 255.0F;
        float r1 = (color1 >> 16 & 0xFF) / 255.0F;
        float g1 = (color1 >> 8 & 0xFF) / 255.0F;
        float b1 = (color1 & 0xFF) / 255.0F;
        float a2 = (color2 >> 24 & 0xFF) / 255.0F;
        float r2 = (color2 >> 16 & 0xFF) / 255.0F;
        float g2 = (color2 >> 8 & 0xFF) / 255.0F;
        float b2 = (color2 & 0xFF) / 255.0F;
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        GL11.glDisable(3008);
        GL11.glBlendFunc(770, 771);
        GL11.glShadeModel(7425);
        Tessellator tess = Tessellator.getInstance();
        tess.getBuffer().begin(7, DefaultVertexFormats.POSITION_COLOR);
        tess.getBuffer().pos(right, top, 300.0D).color(r1, g1, b1, a1).endVertex();
        tess.getBuffer().pos(left, top, 300.0D).color(r1, g1, b1, a1).endVertex();
        tess.getBuffer().pos(left, bottom, 300.0D).color(r2, g2, b2, a2).endVertex();
        tess.getBuffer().pos(right, bottom, 300.0D).color(r2, g2, b2, a2).endVertex();
        tess.draw();
        GL11.glShadeModel(7424);
        GlStateManager.blendFunc(770, 771);
        if (!blendOn) {
            GL11.glDisable(3042);
        }
        GL11.glEnable(3008);
        GL11.glEnable(3553);
    }
}
