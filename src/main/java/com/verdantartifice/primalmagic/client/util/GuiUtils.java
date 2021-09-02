package com.verdantartifice.primalmagic.client.util;

import java.awt.Color;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.sources.SourceList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

/**
 * Utility methods for dealing with GUI rendering.
 * 
 * @author Daedalus4096
 */
public class GuiUtils {
    public static boolean renderItemStack(PoseStack matrixStack, ItemStack stack, int x, int y, String text, boolean hideStackOverlay) {
        boolean retVal = false;
        if (stack != null && !stack.isEmpty()) {
            Minecraft mc = Minecraft.getInstance();
            ItemRenderer itemRenderer = mc.getItemRenderer();
            
            matrixStack.pushPose();
            matrixStack.translate(0.0F, 0.0F, 32.0F);   // Bring the item stack up in the Z-order
            
            // Render the item stack into the GUI and, if applicable, its stack size and/or damage bar
            itemRenderer.renderAndDecorateItem(stack, x, y);
            if (!hideStackOverlay) {
                itemRenderer.renderGuiItemDecorations(mc.font, stack, x, y, text);
            }
            
            matrixStack.popPose();
            
            retVal = true;
        }
        return retVal;
    }
    
    public static void renderItemTooltip(PoseStack matrixStack, ItemStack stack, int x, int y) {
        Minecraft mc = Minecraft.getInstance();
        List<Component> lines = stack.getTooltipLines(mc.player, mc.options.advancedItemTooltips ? TooltipFlag.Default.ADVANCED : TooltipFlag.Default.NORMAL);
        net.minecraftforge.fmlclient.gui.GuiUtils.drawHoveringText(stack, matrixStack, lines, x, y, mc.screen.width, mc.screen.height, -1, mc.font);
    }
    
    public static void renderCustomTooltip(PoseStack matrixStack, List<Component> textList, int x, int y) {
        Minecraft mc = Minecraft.getInstance();
        ItemStack stack = ItemStack.EMPTY;
        net.minecraftforge.fmlclient.gui.GuiUtils.drawHoveringText(stack, matrixStack, textList, x, y, mc.screen.width, mc.screen.height, -1, mc.font);
    }
    
    public static void renderSourcesForPlayer(PoseStack matrixStack, @Nullable SourceList sources, @Nullable Player player, int startX, int startY) {
        if (sources == null || sources.isEmpty()) {
            return;
        }
        matrixStack.pushPose();
        int x = 0;
        int index = 0;
        
        // Render each source in the list in prescribed order
        for (Source source : sources.getSourcesSorted()) {
            if (source != null) {
                x = startX + (index * 18);
                
                // If the source hasn't been discovered by the player, render an unknown icon instead
                if (source.isDiscovered(player)) {
                    GuiUtils.renderSourceIcon(matrixStack, x, startY, source, sources.getAmount(source), 998);
                } else {
                    GuiUtils.renderUnknownSourceIcon(matrixStack, x, startY, sources.getAmount(source), 998);
                }
                index++;
            }
        }
        matrixStack.popPose();
    }
    
    public static void renderSourceIcon(PoseStack matrixStack, int x, int y, @Nullable Source source, int amount, double z) {
        if (source != null) {
            renderSourceIcon(matrixStack, x, y, source.getAtlasLocation(), amount, z);
        }
    }
    
    public static void renderUnknownSourceIcon(PoseStack matrixStack, int x, int y, int amount, double z) {
        renderSourceIcon(matrixStack, x, y, Source.getUnknownAtlasLocation(), amount, z);
    }
    
    protected static void renderSourceIcon(PoseStack matrixStack, int x, int y, @Nonnull ResourceLocation imageLoc, int amount, double z) {
        // Preserve previous value for blend GL attribute
        boolean isBlendOn = GL11.glIsEnabled(GL11.GL_BLEND);
        
        Minecraft mc = Minecraft.getInstance();
        
        matrixStack.pushPose();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        
        matrixStack.pushPose();
        
        // Render the source's icon
        @SuppressWarnings("deprecation")
        TextureAtlasSprite sprite = mc.getModelManager().getAtlas(TextureAtlas.LOCATION_BLOCKS).getSprite(imageLoc);
        MultiBufferSource.BufferSource buffer = mc.renderBuffers().bufferSource();
        VertexConsumer builder = buffer.getBuffer(RenderType.cutout());
        builder.vertex(x + 0.0D, y + 16.0D, z).color(1.0F, 1.0F, 1.0F, 1.0F).uv(sprite.getU0(), sprite.getV1()).uv2(240, 240).normal(1, 0, 0).endVertex();
        builder.vertex(x + 16.0D, y + 16.0D, z).color(1.0F, 1.0F, 1.0F, 1.0F).uv(sprite.getU1(), sprite.getV1()).uv2(240, 240).normal(1, 0, 0).endVertex();
        builder.vertex(x + 16.0D, y + 0.0D, z).color(1.0F, 1.0F, 1.0F, 1.0F).uv(sprite.getU1(), sprite.getV0()).uv2(240, 240).normal(1, 0, 0).endVertex();
        builder.vertex(x + 0.0D, y + 0.0D, z).color(1.0F, 1.0F, 1.0F, 1.0F).uv(sprite.getU0(), sprite.getV0()).uv2(240, 240).normal(1, 0, 0).endVertex();
        buffer.endBatch();

        matrixStack.popPose();
        
        // Render an amount string for the source, if an amount has been given
        if (amount > 0) {
            matrixStack.pushPose();
            matrixStack.translate(0.0D, 0.0D, z + 1.0D);
            matrixStack.scale(0.5F, 0.5F, 1.0F);
            String amountStr = Integer.toString(amount);
            int amountWidth = mc.font.width(amountStr);
            mc.font.draw(matrixStack, amountStr, (32 - amountWidth + (x * 2)), (32 - mc.font.lineHeight + (y * 2)), Color.WHITE.getRGB());
            matrixStack.popPose();
        }
        
        // Restore changed GL attributes
        if (!isBlendOn) {
            RenderSystem.disableBlend();
        }
        matrixStack.popPose();
    }
    
    public static void renderSourcesBillboard(PoseStack matrixStack, MultiBufferSource buffers, double x, double y, double z, SourceList sources, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        
        double interpolatedPlayerX = mc.player.xo + (partialTicks * (mc.player.getX() - mc.player.xo));
        double interpolatedPlayerY = mc.player.yo + (partialTicks * (mc.player.getY() - mc.player.yo));
        double interpolatedPlayerZ = mc.player.zo + (partialTicks * (mc.player.getZ() - mc.player.zo));
        double dx = (interpolatedPlayerX - x + 0.5D);
        double dz = (interpolatedPlayerZ - z + 0.5D);
        float rotYaw = 180.0F + (float)(Mth.atan2(dx, dz) * 180.0D / Math.PI);
        float scale = 0.03F;
        double shiftX = 0.0D;
        double startDeltaX = ((16.0D * sources.getSources().size()) / 2.0D) * scale;

        for (Source source : sources.getSourcesSorted()) {
            int amount = sources.getAmount(source);
            if (amount > 0) {
                matrixStack.pushPose();
                matrixStack.translate(x - interpolatedPlayerX, y - interpolatedPlayerY - 0.5F, z - interpolatedPlayerZ);
                matrixStack.mulPose(Vector3f.YP.rotationDegrees(rotYaw));
                matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
                matrixStack.translate(shiftX - startDeltaX, 0.0D, 0.0D);
                matrixStack.scale(scale, scale, scale);

                ResourceLocation texLoc = source.isDiscovered(mc.player) ? source.getAtlasLocation() : Source.getUnknownAtlasLocation();
                @SuppressWarnings("deprecation")
                TextureAtlasSprite sprite = mc.getModelManager().getAtlas(TextureAtlas.LOCATION_BLOCKS).getSprite(texLoc);
                VertexConsumer builder = buffers.getBuffer(RenderType.cutout());
                Matrix4f matrix = matrixStack.last().pose();
                builder.vertex(matrix, 0.0F, 16.0F, 0.0F).color(1.0F, 1.0F, 1.0F, 1.0F).uv(sprite.getU0(), sprite.getV1()).uv2(240, 240).normal(1, 0, 0).endVertex();
                builder.vertex(matrix, 16.0F, 16.0F, 0.0F).color(1.0F, 1.0F, 1.0F, 1.0F).uv(sprite.getU1(), sprite.getV1()).uv2(240, 240).normal(1, 0, 0).endVertex();
                builder.vertex(matrix, 16.0F, 0.0F, 0.0F).color(1.0F, 1.0F, 1.0F, 1.0F).uv(sprite.getU1(), sprite.getV0()).uv2(240, 240).normal(1, 0, 0).endVertex();
                builder.vertex(matrix, 0.0F, 0.0F, 0.0F).color(1.0F, 1.0F, 1.0F, 1.0F).uv(sprite.getU0(), sprite.getV0()).uv2(240, 240).normal(1, 0, 0).endVertex();

                String amountStr = Integer.toString(amount);
                int amountWidth = mc.font.width(amountStr);
                matrixStack.pushPose();
                matrixStack.scale(0.5F, 0.5F, 0.5F);
                matrixStack.translate(32.0D - amountWidth, 32.0D - mc.font.lineHeight, 0.0D);
                mc.font.drawShadow(matrixStack, amountStr, 0F, 0F, Color.WHITE.getRGB());
                matrixStack.popPose();

                matrixStack.popPose();
                shiftX += 16.0D * scale;
            }
        }
    }
}
