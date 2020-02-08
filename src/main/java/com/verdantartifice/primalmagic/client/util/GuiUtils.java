package com.verdantartifice.primalmagic.client.util;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.platform.GlStateManager;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.sources.SourceList;

import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Utility methods for dealing with GUI rendering.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
public class GuiUtils {
    public static boolean renderItemStack(ItemStack stack, int x, int y, String text, boolean hideStackOverlay) {
        boolean retVal = false;
        if (stack != null && !stack.isEmpty()) {
            GlStateManager.color3f(1.0F, 1.0F, 1.0F);
            Minecraft mc = Minecraft.getInstance();
            ItemRenderer itemRenderer = mc.getItemRenderer();
            
            // Preserve previous values for lighting and rescale-normal GL attributes
            boolean isLightingEnabled = GL11.glIsEnabled(GL11.GL_LIGHTING);
            boolean isRescaleNormalEnabled = GL11.glIsEnabled(32826);
            
            GlStateManager.pushMatrix();
            GlStateManager.translatef(0.0F, 0.0F, 32.0F);   // Bring the item stack up in the Z-order
            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableRescaleNormal();
            GlStateManager.enableLighting();
            RenderHelper.enableGUIStandardItemLighting();
            
            // Render the item stack into the GUI and, if applicable, its stack size and/or damage bar
            itemRenderer.renderItemAndEffectIntoGUI(stack, x, y);
            if (!hideStackOverlay) {
                itemRenderer.renderItemOverlayIntoGUI(mc.fontRenderer, stack, x, y, text);
            }
            
            GlStateManager.popMatrix();
            
            // Return the lighting and rescale-normal attributes to their previous values
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
        MainWindow mainWindow = mc.getMainWindow();
        double scaleFactor = mainWindow.getGuiScaleFactor();
        
        // Preserve previous value for the lighting GL attribute
        boolean isLightingEnabled = GL11.glIsEnabled(GL11.GL_LIGHTING);
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableLighting();

        // Determine whether to flip the tooltip box to the other side of the mouse
        int max = 240;
        double mouseX = mc.mouseHelper.getMouseX();
        boolean flip = false;
        if (!ignoreMouse && ((max + 24) * scaleFactor + mouseX > mainWindow.getFramebufferWidth())) {
            max = (int)((mainWindow.getFramebufferWidth() - mouseX) / scaleFactor - 24);
            if (max < 120) {
                max = 240;
                flip = true;
            }
        }
        
        // Break up each line of the tooltip to fit in the available X-space
        List<String> parsedList = new ArrayList<>();
        for (ITextComponent text : textList)  {
            List<String> strList = mc.fontRenderer.listFormattedStringToWidth(text.getFormattedText(), max);
            parsedList.addAll(strList);
        }
        
        // Determine the total dimensions of the tooltip text
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
        if (sY + totalHeight > mainWindow.getScaledHeight()) {
            sY = mainWindow.getScaledHeight() - totalHeight - 5;
        }
        if (flip) {
            sX -= (widestLineWidth + 6);
        }
        
        float prevZ = mc.getItemRenderer().zLevel;
        mc.getItemRenderer().zLevel = 300.0F;   // Bring the tooltip up in the Z-order
        
        // Render the tooltip box
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

        // Render the tooltip text
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

        // Restore changed attribute to their previous values
        mc.getItemRenderer().zLevel = prevZ;
        if (isLightingEnabled) {
            GlStateManager.enableLighting();
        } else {
            GlStateManager.disableLighting();
        }
        GlStateManager.enableRescaleNormal();
    }
    
    public static void drawGradientRect(int left, int top, int right, int bottom, int color1, int color2) {
        boolean blendOn = GL11.glIsEnabled(3042);
        
        // Calculate RGBA components for each color
        float a1 = (color1 >> 24 & 0xFF) / 255.0F;
        float r1 = (color1 >> 16 & 0xFF) / 255.0F;
        float g1 = (color1 >> 8 & 0xFF) / 255.0F;
        float b1 = (color1 & 0xFF) / 255.0F;
        float a2 = (color2 >> 24 & 0xFF) / 255.0F;
        float r2 = (color2 >> 16 & 0xFF) / 255.0F;
        float g2 = (color2 >> 8 & 0xFF) / 255.0F;
        float b2 = (color2 & 0xFF) / 255.0F;
        
        // Bring the rect up in the Z-order
        double z = 300.0D;
        
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        GL11.glDisable(3008);
        GL11.glBlendFunc(770, 771);
        GL11.glShadeModel(7425);
        
        // Draw the rectangle
        Tessellator tess = Tessellator.getInstance();
        tess.getBuffer().begin(7, DefaultVertexFormats.POSITION_COLOR);
        tess.getBuffer().pos(right, top, z).color(r1, g1, b1, a1).endVertex();
        tess.getBuffer().pos(left, top, z).color(r1, g1, b1, a1).endVertex();
        tess.getBuffer().pos(left, bottom, z).color(r2, g2, b2, a2).endVertex();
        tess.getBuffer().pos(right, bottom, z).color(r2, g2, b2, a2).endVertex();
        tess.draw();
        
        // Restore changed GL attributes
        GL11.glShadeModel(7424);
        GlStateManager.blendFunc(770, 771);
        if (!blendOn) {
            GL11.glDisable(3042);
        }
        GL11.glEnable(3008);
        GL11.glEnable(3553);
    }
    
    public static void renderSourcesForPlayer(@Nullable SourceList sources, @Nullable PlayerEntity player, int startX, int startY) {
        if (sources == null || sources.isEmpty()) {
            return;
        }
        GlStateManager.pushMatrix();
        int x = 0;
        int index = 0;
        
        // Render each source in the list in prescribed order
        for (Source source : sources.getSourcesSorted()) {
            if (source != null) {
                x = startX + (index * 18);
                
                // If the source hasn't been discovered by the player, render an unknown icon instead
                if (source.isDiscovered(player)) {
                    GuiUtils.renderSourceIcon(x, startY, source, sources.getAmount(source), 999);
                } else {
                    GuiUtils.renderUnknownSourceIcon(x, startY, sources.getAmount(source), 999);
                }
                index++;
            }
        }
        GlStateManager.popMatrix();
    }
    
    public static void renderSourceIcon(int x, int y, @Nullable Source source, int amount, double z) {
        if (source != null) {
            renderSourceIcon(x, y, source.getImage(), amount, z);
        }
    }
    
    public static void renderUnknownSourceIcon(int x, int y, int amount, double z) {
        renderSourceIcon(x, y, Source.getUnknownImage(), amount, z);
    }
    
    protected static void renderSourceIcon(int x, int y, @Nonnull ResourceLocation imageLoc, int amount, double z) {
        // Preserve previous values for blend and lighting GL attributes
        boolean isBlendOn = GL11.glIsEnabled(3042);
        boolean isLightingEnabled = GL11.glIsEnabled(2896);
        
        Minecraft mc = Minecraft.getInstance();
        
        GlStateManager.pushMatrix();
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        
        GlStateManager.pushMatrix();
        
        mc.getTextureManager().bindTexture(imageLoc);
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        
        // Render the source's icon
        Tessellator tess = Tessellator.getInstance();
        BufferBuilder bb = tess.getBuffer();
        bb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        bb.pos(x + 0.0D, y + 16.0D, z).tex(0.0D, 1.0D).endVertex();
        bb.pos(x + 16.0D, y + 16.0D, z).tex(1.0D, 1.0D).endVertex();
        bb.pos(x + 16.0D, y + 0.0D, z).tex(1.0D, 0.0D).endVertex();
        bb.pos(x + 0.0D, y + 0.0D, z).tex(0.0D, 0.0D).endVertex();
        tess.draw();

        GlStateManager.popMatrix();
        
        // Render an amount string for the source, if an amount has been given
        if (amount > 0) {
            GlStateManager.pushMatrix();
            GlStateManager.scaled(0.5D, 0.5D, 1.0D);
            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            String amountStr = Integer.toString(amount);
            int amountWidth = mc.fontRenderer.getStringWidth(amountStr);
            mc.fontRenderer.drawString(amountStr, (32 - amountWidth + (x * 2)), (32 - mc.fontRenderer.FONT_HEIGHT + (y * 2)), Color.WHITE.getRGB());
            GlStateManager.popMatrix();
        }
        
        // Restore changed GL attributes
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        if (!isBlendOn) {
            GlStateManager.disableBlend();
        }
        if (isLightingEnabled) {
            GlStateManager.enableLighting();
        }
        GlStateManager.popMatrix();
    }
}
