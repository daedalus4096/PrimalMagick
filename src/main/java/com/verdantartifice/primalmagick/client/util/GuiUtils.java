package com.verdantartifice.primalmagick.client.util;

import java.awt.Color;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.SourceList;

import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.phys.Vec3;

/**
 * Utility methods for dealing with GUI rendering.
 * 
 * @author Daedalus4096
 */
public class GuiUtils {
    public static boolean renderItemStack(GuiGraphics guiGraphics, ItemStack stack, int x, int y, String text, boolean hideStackOverlay) {
        boolean retVal = false;
        if (stack != null && !stack.isEmpty()) {
            Minecraft mc = Minecraft.getInstance();
            
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(0.0F, 0.0F, 32.0F);   // Bring the item stack up in the Z-order
            
            // Render the item stack into the GUI and, if applicable, its stack size and/or damage bar
            guiGraphics.renderItem(stack, x, y);
            if (!hideStackOverlay) {
                guiGraphics.renderItemDecorations(mc.font, stack, x, y, text);
            }
            
            guiGraphics.pose().popPose();
            
            retVal = true;
        }
        return retVal;
    }
    
    public static boolean renderItemStack(GuiGraphics guiGraphics, ItemStack stack, int x, int y, String text, boolean hideStackOverlay, Optional<Vec3> scaleOpt) {
        boolean retVal = false;
        if (stack != null && !stack.isEmpty()) {
            Minecraft mc = Minecraft.getInstance();
            ItemRenderer itemRenderer = mc.getItemRenderer();
            BakedModel bakedModel = itemRenderer.getModel(stack, mc.level, mc.player, 0);
            
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(0.0F, 0.0F, 32.0F);
            
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(x + 8, y + 8, 150);
            
            try {
                guiGraphics.pose().mulPoseMatrix((new Matrix4f()).scaling(1.0F, -1.0F, 1.0F));
                guiGraphics.pose().scale(16.0F, 16.0F, 16.0F);
                scaleOpt.ifPresent(scale -> {
                    guiGraphics.pose().scale((float)scale.x, (float)scale.y, (float)scale.z);
                });
                
                boolean flag = !bakedModel.usesBlockLight();
                if (flag) {
                    Lighting.setupForFlatItems();
                }
                itemRenderer.render(stack, ItemDisplayContext.GUI, false, guiGraphics.pose(), guiGraphics.bufferSource(), 15728880, OverlayTexture.NO_OVERLAY, bakedModel);
                guiGraphics.flush();
                if (flag) {
                    Lighting.setupFor3DItems();
                }
            } catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.forThrowable(throwable, "Rendering item");
                CrashReportCategory crashreportcategory = crashreport.addCategory("Item being rendered");
                crashreportcategory.setDetail("Item Type", () -> {
                    return String.valueOf((Object)stack.getItem());
                });
                crashreportcategory.setDetail("Registry Name", () -> String.valueOf(net.minecraftforge.registries.ForgeRegistries.ITEMS.getKey(stack.getItem())));
                crashreportcategory.setDetail("Item Damage", () -> {
                    return String.valueOf(stack.getDamageValue());
                });
                crashreportcategory.setDetail("Item NBT", () -> {
                    return String.valueOf((Object)stack.getTag());
                });
                crashreportcategory.setDetail("Item Foil", () -> {
                    return String.valueOf(stack.hasFoil());
                });
                throw new ReportedException(crashreport);
            }
            
            guiGraphics.pose().popPose();

            if (!hideStackOverlay) {
                guiGraphics.renderItemDecorations(mc.font, stack, x, y, text);
            }
            
            guiGraphics.pose().popPose();
            
            retVal = true;
        }
        return retVal;
    }
    
    public static void renderItemTooltip(GuiGraphics guiGraphics, ItemStack stack, int x, int y) {
        Minecraft mc = Minecraft.getInstance();
        List<Component> lines = stack.getTooltipLines(mc.player, mc.options.advancedItemTooltips ? TooltipFlag.Default.ADVANCED : TooltipFlag.Default.NORMAL);
        guiGraphics.renderComponentTooltip(mc.font, lines, x, y, stack);
    }
    
    public static void renderCustomTooltip(GuiGraphics guiGraphics, List<Component> textList, int x, int y) {
        Minecraft mc = Minecraft.getInstance();
        guiGraphics.renderComponentTooltip(mc.font, textList, x, y, ItemStack.EMPTY);
    }
    
    public static void renderSourcesForPlayer(GuiGraphics guiGraphics, @Nullable SourceList sources, @Nullable Player player, int startX, int startY) {
        if (sources == null || sources.isEmpty()) {
            return;
        }
        guiGraphics.pose().pushPose();
        int x = 0;
        int index = 0;
        
        // Render each source in the list in prescribed order
        for (Source source : sources.getSourcesSorted()) {
            if (source != null) {
                x = startX + (index * 18);
                
                // If the source hasn't been discovered by the player, render an unknown icon instead
                if (source.isDiscovered(player)) {
                    GuiUtils.renderSourceIcon(guiGraphics, x, startY, source, sources.getAmount(source), 998);
                } else {
                    GuiUtils.renderUnknownSourceIcon(guiGraphics, x, startY, sources.getAmount(source), 998);
                }
                index++;
            }
        }
        guiGraphics.pose().popPose();
    }
    
    public static void renderSourceIcon(GuiGraphics guiGraphics, int x, int y, @Nullable Source source, int amount, double z) {
        if (source != null) {
            renderSourceIcon(guiGraphics, x, y, source.getAtlasLocation(), amount, z);
        }
    }
    
    public static void renderUnknownSourceIcon(GuiGraphics guiGraphics, int x, int y, int amount, double z) {
        renderSourceIcon(guiGraphics, x, y, Source.getUnknownAtlasLocation(), amount, z);
    }
    
    protected static void renderSourceIcon(GuiGraphics guiGraphics, int x, int y, @Nonnull ResourceLocation imageLoc, int amount, double z) {
        // Preserve previous value for blend GL attribute
        boolean isBlendOn = GL11.glIsEnabled(GL11.GL_BLEND);
        
        Minecraft mc = Minecraft.getInstance();
        
        guiGraphics.pose().pushPose();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        
        guiGraphics.pose().pushPose();
        
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

        guiGraphics.pose().popPose();
        
        // Render an amount string for the source, if an amount has been given
        if (amount > 0) {
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(0.0D, 0.0D, z + 1.0D);
            guiGraphics.pose().scale(0.5F, 0.5F, 1.0F);
            String amountStr = Integer.toString(amount);
            int amountWidth = mc.font.width(amountStr);
            guiGraphics.drawString(mc.font, amountStr, (32 - amountWidth + (x * 2)), (32 - mc.font.lineHeight + (y * 2)), Color.WHITE.getRGB());
            guiGraphics.pose().popPose();
        }
        
        // Restore changed GL attributes
        if (!isBlendOn) {
            RenderSystem.disableBlend();
        }
        guiGraphics.pose().popPose();
    }
    
    public static void renderSourcesBillboard(PoseStack poseStack, MultiBufferSource buffers, double x, double y, double z, SourceList sources, float partialTicks) {
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
                poseStack.pushPose();
                poseStack.translate(x - interpolatedPlayerX, y - interpolatedPlayerY - 0.5F, z - interpolatedPlayerZ);
                poseStack.mulPose(Axis.YP.rotationDegrees(rotYaw));
                poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
                poseStack.translate(shiftX - startDeltaX, 0.0D, 0.0D);
                poseStack.scale(scale, scale, scale);

                ResourceLocation texLoc = source.isDiscovered(mc.player) ? source.getAtlasLocation() : Source.getUnknownAtlasLocation();
                @SuppressWarnings("deprecation")
                TextureAtlasSprite sprite = mc.getModelManager().getAtlas(TextureAtlas.LOCATION_BLOCKS).getSprite(texLoc);
                VertexConsumer builder = buffers.getBuffer(RenderType.cutout());
                Matrix4f matrix = poseStack.last().pose();
                builder.vertex(matrix, 0.0F, 16.0F, 0.0F).color(1.0F, 1.0F, 1.0F, 1.0F).uv(sprite.getU0(), sprite.getV1()).uv2(240, 240).normal(1, 0, 0).endVertex();
                builder.vertex(matrix, 16.0F, 16.0F, 0.0F).color(1.0F, 1.0F, 1.0F, 1.0F).uv(sprite.getU1(), sprite.getV1()).uv2(240, 240).normal(1, 0, 0).endVertex();
                builder.vertex(matrix, 16.0F, 0.0F, 0.0F).color(1.0F, 1.0F, 1.0F, 1.0F).uv(sprite.getU1(), sprite.getV0()).uv2(240, 240).normal(1, 0, 0).endVertex();
                builder.vertex(matrix, 0.0F, 0.0F, 0.0F).color(1.0F, 1.0F, 1.0F, 1.0F).uv(sprite.getU0(), sprite.getV0()).uv2(240, 240).normal(1, 0, 0).endVertex();

                String amountStr = Integer.toString(amount);
                int amountWidth = mc.font.width(amountStr);
                poseStack.pushPose();
                poseStack.scale(0.5F, 0.5F, -0.5F);
                poseStack.translate(32.0D - amountWidth, 32.0D - mc.font.lineHeight, 0.0D);
                mc.font.drawInBatch(amountStr, 0F, 0F, Color.WHITE.getRGB(), true, poseStack.last().pose(), buffers, Font.DisplayMode.NORMAL, 0, 15728880, mc.font.isBidirectional());
                poseStack.popPose();

                poseStack.popPose();
                shiftX += 16.0D * scale;
            }
        }
    }
}
