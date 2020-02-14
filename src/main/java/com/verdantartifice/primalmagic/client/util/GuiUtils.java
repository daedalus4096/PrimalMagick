package com.verdantartifice.primalmagic.client.util;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.sources.SourceList;

import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
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
            RenderSystem.color3f(1.0F, 1.0F, 1.0F);
            Minecraft mc = Minecraft.getInstance();
            ItemRenderer itemRenderer = mc.getItemRenderer();
            
            // Preserve previous values for lighting and rescale-normal GL attributes
            boolean isLightingEnabled = GL11.glIsEnabled(GL11.GL_LIGHTING);
            boolean isRescaleNormalEnabled = GL11.glIsEnabled(32826);
            
            RenderSystem.pushMatrix();
            RenderSystem.translatef(0.0F, 0.0F, 32.0F);   // Bring the item stack up in the Z-order
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.enableRescaleNormal();
            RenderSystem.enableLighting();
            // TODO enable GUI standard item lighting? from RenderHelper?
            
            // Render the item stack into the GUI and, if applicable, its stack size and/or damage bar
            itemRenderer.renderItemAndEffectIntoGUI(stack, x, y);
            if (!hideStackOverlay) {
                itemRenderer.renderItemOverlayIntoGUI(mc.fontRenderer, stack, x, y, text);
            }
            
            RenderSystem.popMatrix();
            
            // Return the lighting and rescale-normal attributes to their previous values
            if (isRescaleNormalEnabled) {
                RenderSystem.enableRescaleNormal();
            } else {
                RenderSystem.disableRescaleNormal();
            }
            if (isLightingEnabled) {
                RenderSystem.enableLighting();
            } else {
                RenderSystem.disableLighting();
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
        RenderSystem.disableRescaleNormal();
        RenderSystem.disableLighting();

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
            RenderSystem.pushMatrix();
            RenderSystem.translatef(sX, sY, 0.0F);
            
            String str = parsedList.get(i);
            
            RenderSystem.pushMatrix();
            sY += mc.fontRenderer.FONT_HEIGHT;
            RenderSystem.translatef(0.0F, 0.0F, 301.0F);
            mc.fontRenderer.drawStringWithShadow(str, 0.0F, 0.0F, -1);
            RenderSystem.popMatrix();
            if (i == 0) {
                sY += 2;
            }
            
            RenderSystem.popMatrix();
        }

        // Restore changed attribute to their previous values
        mc.getItemRenderer().zLevel = prevZ;
        if (isLightingEnabled) {
            RenderSystem.enableLighting();
        } else {
            RenderSystem.disableLighting();
        }
        RenderSystem.enableRescaleNormal();
    }
    
    public static void drawGradientRect(int left, int top, int right, int bottom, int color1, int color2) {
        boolean blendOn = GL11.glIsEnabled(GL11.GL_BLEND);
        
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
        
        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.disableAlphaTest();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.shadeModel(GL11.GL_SMOOTH);
        
        // Draw the rectangle
        Tessellator tess = Tessellator.getInstance();
        tess.getBuffer().begin(7, DefaultVertexFormats.POSITION_COLOR);
        tess.getBuffer().pos(right, top, z).color(r1, g1, b1, a1).endVertex();
        tess.getBuffer().pos(left, top, z).color(r1, g1, b1, a1).endVertex();
        tess.getBuffer().pos(left, bottom, z).color(r2, g2, b2, a2).endVertex();
        tess.getBuffer().pos(right, bottom, z).color(r2, g2, b2, a2).endVertex();
        tess.draw();
        
        // Restore changed GL attributes
        RenderSystem.shadeModel(GL11.GL_FLAT);
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        if (!blendOn) {
            RenderSystem.disableBlend();
        }
        RenderSystem.enableAlphaTest();
        RenderSystem.enableTexture();
    }
    
    public static void renderSourcesForPlayer(@Nullable SourceList sources, @Nullable PlayerEntity player, int startX, int startY) {
        if (sources == null || sources.isEmpty()) {
            return;
        }
        RenderSystem.pushMatrix();
        int x = 0;
        int index = 0;
        
        // Render each source in the list in prescribed order
        for (Source source : sources.getSourcesSorted()) {
            if (source != null) {
                x = startX + (index * 18);
                
                // If the source hasn't been discovered by the player, render an unknown icon instead
                if (source.isDiscovered(player)) {
                    GuiUtils.renderSourceIcon(x, startY, source, sources.getAmount(source), 998);
                } else {
                    GuiUtils.renderUnknownSourceIcon(x, startY, sources.getAmount(source), 998);
                }
                index++;
            }
        }
        RenderSystem.popMatrix();
    }
    
    public static void renderSourceIcon(int x, int y, @Nullable Source source, int amount, double z) {
        if (source != null) {
            renderSourceIcon(x, y, source.getAtlasLocation(), amount, z);
        }
    }
    
    public static void renderUnknownSourceIcon(int x, int y, int amount, double z) {
        renderSourceIcon(x, y, Source.getUnknownAtlasLocation(), amount, z);
    }
    
    protected static void renderSourceIcon(int x, int y, @Nonnull ResourceLocation imageLoc, int amount, double z) {
        // Preserve previous values for blend and lighting GL attributes
        boolean isBlendOn = GL11.glIsEnabled(GL11.GL_BLEND);
        boolean isLightingEnabled = GL11.glIsEnabled(GL11.GL_LIGHTING);
        
        Minecraft mc = Minecraft.getInstance();
        
        RenderSystem.pushMatrix();
        RenderSystem.disableLighting();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        
        RenderSystem.pushMatrix();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        
        // Render the source's icon
        @SuppressWarnings("deprecation")
        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(imageLoc);
        IRenderTypeBuffer.Impl buffer = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
        IVertexBuilder builder = buffer.getBuffer(RenderType.cutout());
        builder.pos(x + 0.0D, y + 16.0D, z).color(1.0F, 1.0F, 1.0F, 1.0F).tex(sprite.getMinU(), sprite.getMaxV()).lightmap(0, 240).normal(1, 0, 0).endVertex();
        builder.pos(x + 16.0D, y + 16.0D, z).color(1.0F, 1.0F, 1.0F, 1.0F).tex(sprite.getMaxU(), sprite.getMaxV()).lightmap(0, 240).normal(1, 0, 0).endVertex();
        builder.pos(x + 16.0D, y + 0.0D, z).color(1.0F, 1.0F, 1.0F, 1.0F).tex(sprite.getMaxU(), sprite.getMinV()).lightmap(0, 240).normal(1, 0, 0).endVertex();
        builder.pos(x + 0.0D, y + 0.0D, z).color(1.0F, 1.0F, 1.0F, 1.0F).tex(sprite.getMinU(), sprite.getMinV()).lightmap(0, 240).normal(1, 0, 0).endVertex();
        buffer.finish();

        RenderSystem.popMatrix();
        
        // Render an amount string for the source, if an amount has been given
        if (amount > 0) {
            RenderSystem.pushMatrix();
            RenderSystem.translated(0.0D, 0.0D, z + 1.0D);
            RenderSystem.scaled(0.5D, 0.5D, 1.0D);
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            String amountStr = Integer.toString(amount);
            int amountWidth = mc.fontRenderer.getStringWidth(amountStr);
            mc.fontRenderer.drawString(amountStr, (32 - amountWidth + (x * 2)), (32 - mc.fontRenderer.FONT_HEIGHT + (y * 2)), Color.WHITE.getRGB());
            RenderSystem.popMatrix();
        }
        
        // Restore changed GL attributes
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        if (!isBlendOn) {
            RenderSystem.disableBlend();
        }
        if (isLightingEnabled) {
            RenderSystem.enableLighting();
        }
        RenderSystem.popMatrix();
    }
}
