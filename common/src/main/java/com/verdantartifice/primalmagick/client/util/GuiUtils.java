package com.verdantartifice.primalmagick.client.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Either;
import com.mojang.math.Axis;
import com.verdantartifice.primalmagick.common.misc.IconDefinition;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.data.AtlasIds;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Utility methods for dealing with GUI rendering.
 * 
 * @author Daedalus4096
 */
public class GuiUtils {
    public static boolean renderItemStack(GuiGraphicsExtractor guiGraphics, ItemStack stack, int x, int y, String text, boolean hideStackOverlay) {
        return renderItemStack(guiGraphics, stack, x, y, text, hideStackOverlay, Optional.empty());
    }

    public static boolean renderItemStack(GuiGraphicsExtractor guiGraphics, ItemStack stack, int x, int y, String text, boolean hideStackOverlay, Optional<Vec3> scaleOpt) {
        boolean retVal = false;
        if (stack != null && !stack.isEmpty()) {
            Minecraft mc = Minecraft.getInstance();
            
            guiGraphics.pose().pushMatrix();
            scaleOpt.ifPresent(scale -> {
                guiGraphics.pose().scale((float)scale.x, (float)scale.y);
            });

            // Render the item stack into the GUI and, if applicable, its stack size and/or damage bar
            guiGraphics.item(stack, x, y);
            if (!hideStackOverlay) {
                guiGraphics.itemDecorations(mc.font, stack, x, y, text);
            }
            
            guiGraphics.pose().popMatrix();
            
            retVal = true;
        }
        return retVal;
    }
    
    public static void renderCustomTooltip(GuiGraphicsExtractor guiGraphics, List<Component> textList, int x, int y) {
        Minecraft mc = Minecraft.getInstance();
        Services.GUI_GRAPHICS.renderComponentTooltip(guiGraphics, mc.font, textList, x, y, ItemStack.EMPTY);
    }

    public static void renderComponentTooltipFromElements(GuiGraphicsExtractor guiGraphics, List<Either<FormattedText, TooltipComponent>> elements, int x, int y) {
        Minecraft mc = Minecraft.getInstance();
        Services.GUI_GRAPHICS.renderComponentTooltipFromElements(guiGraphics, mc.font, elements, x, y, ItemStack.EMPTY);
    }
    
    public static void renderSourcesForPlayer(GuiGraphicsExtractor guiGraphics, @Nullable SourceList sources, @Nullable Player player, int startX, int startY) {
        if (sources == null || sources.isEmpty()) {
            return;
        }
        guiGraphics.pose().pushMatrix();
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
        guiGraphics.pose().popMatrix();
    }
    
    public static void renderSourceIcon(GuiGraphicsExtractor guiGraphics, int x, int y, @Nullable Source source, int amount, double z) {
        if (source != null) {
            renderSourceIcon(guiGraphics, x, y, source.getImage(), amount, z);
        }
    }
    
    public static void renderUnknownSourceIcon(GuiGraphicsExtractor guiGraphics, int x, int y, int amount, double z) {
        renderSourceIcon(guiGraphics, x, y, Source.getUnknownImage(), amount, z);
    }
    
    protected static void renderSourceIcon(GuiGraphicsExtractor guiGraphics, int x, int y, @Nonnull Identifier imageLoc, int amount, double z) {
        Minecraft mc = Minecraft.getInstance();
        
        guiGraphics.pose().pushMatrix();

        guiGraphics.pose().pushMatrix();
        
        // Render the source's icon
        MultiBufferSource.BufferSource buffer = mc.renderBuffers().bufferSource();
        Identifier guiAtlas = AtlasIds.GUI;
        SpriteId texSprite = new SpriteId(guiAtlas, imageLoc);
        VertexConsumer builder = texSprite.buffer(mc.getAtlasManager(), buffer, RenderTypes::itemCutout);
        TextureAtlasSprite sprite = mc.getAtlasManager().get(texSprite);
        builder.addVertex(x + 0.0F, y + 16.0F, (float)z).setColor(1.0F, 1.0F, 1.0F, 1.0F).setUv(sprite.getU0(), sprite.getV1()).setUv2(240, 240).setNormal(1, 0, 0);
        builder.addVertex(x + 16.0F, y + 16.0F, (float)z).setColor(1.0F, 1.0F, 1.0F, 1.0F).setUv(sprite.getU1(), sprite.getV1()).setUv2(240, 240).setNormal(1, 0, 0);
        builder.addVertex(x + 16.0F, y + 0.0F, (float)z).setColor(1.0F, 1.0F, 1.0F, 1.0F).setUv(sprite.getU1(), sprite.getV0()).setUv2(240, 240).setNormal(1, 0, 0);
        builder.addVertex(x + 0.0F, y + 0.0F, (float)z).setColor(1.0F, 1.0F, 1.0F, 1.0F).setUv(sprite.getU0(), sprite.getV0()).setUv2(240, 240).setNormal(1, 0, 0);
        buffer.endBatch();

        guiGraphics.pose().popMatrix();
        
        // Render an amount string for the source, if an amount has been given
        if (amount > 0) {
            guiGraphics.pose().pushMatrix();
            guiGraphics.pose().scale(0.5F, 0.5F);
            String amountStr = Integer.toString(amount);
            int amountWidth = mc.font.width(amountStr);
            guiGraphics.text(mc.font, amountStr, (32 - amountWidth + (x * 2)), (32 - mc.font.lineHeight + (y * 2)), Color.WHITE.getRGB());
            guiGraphics.pose().popMatrix();
        }
        
        guiGraphics.pose().popMatrix();
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

                Identifier texLoc = source.isDiscovered(mc.player) ? source.getImage() : Source.getUnknownImage();
                Identifier guiAtlas = AtlasIds.GUI;
                SpriteId texSprite = new SpriteId(guiAtlas, texLoc);
                VertexConsumer builder = texSprite.buffer(mc.getAtlasManager(), buffers, RenderTypes::itemCutout);
                TextureAtlasSprite sprite = mc.getAtlasManager().get(texSprite);
                Matrix4f matrix = poseStack.last().pose();
                builder.addVertex(matrix, 0.0F, 16.0F, 0.0F).setColor(1.0F, 1.0F, 1.0F, 1.0F).setUv(sprite.getU0(), sprite.getV1()).setUv2(240, 240).setNormal(1, 0, 0);
                builder.addVertex(matrix, 16.0F, 16.0F, 0.0F).setColor(1.0F, 1.0F, 1.0F, 1.0F).setUv(sprite.getU1(), sprite.getV1()).setUv2(240, 240).setNormal(1, 0, 0);
                builder.addVertex(matrix, 16.0F, 0.0F, 0.0F).setColor(1.0F, 1.0F, 1.0F, 1.0F).setUv(sprite.getU1(), sprite.getV0()).setUv2(240, 240).setNormal(1, 0, 0);
                builder.addVertex(matrix, 0.0F, 0.0F, 0.0F).setColor(1.0F, 1.0F, 1.0F, 1.0F).setUv(sprite.getU0(), sprite.getV0()).setUv2(240, 240).setNormal(1, 0, 0);

                String amountStr = Integer.toString(amount);
                int amountWidth = mc.font.width(amountStr);
                poseStack.pushPose();
                poseStack.scale(0.5F, 0.5F, -0.5F);
                poseStack.translate(32.0D - amountWidth, 32.0D - mc.font.lineHeight, 0.0D);
                mc.font.drawInBatch(amountStr, 0F, 0F, Color.WHITE.getRGB(), true, poseStack.last().pose(), buffers, Font.DisplayMode.NORMAL, 0, 15728880);
                poseStack.popPose();

                poseStack.popPose();
                shiftX += 16.0D * scale;
            }
        }
    }
    
    public static void renderIconFromDefinition(GuiGraphicsExtractor guiGraphics, IconDefinition iconDef, int x, int y) {
        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().translate(x, y);
        if (iconDef.isItem()) {
            GuiUtils.renderItemStack(guiGraphics, new ItemStack(iconDef.asItem()), 0, 0, null, true);
        } else if (iconDef.isTag()) {
            GuiUtils.renderItemStack(guiGraphics, getTagDisplayStack(iconDef.asTagKey()), 0, 0, null, true);
        } else {
            guiGraphics.pose().scale(0.0625F, 0.0625F);
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, iconDef.getLocation(), 0, 0, 0, 0, 255, 255, 256, 256);
        }
        guiGraphics.pose().popMatrix();
    }
    
    protected static ItemStack getTagDisplayStack(TagKey<Item> key) {
        return getTagDisplayStack(key, System.currentTimeMillis(), 1000L);
    }
    
    protected static ItemStack getTagDisplayStack(TagKey<Item> key, long time, long millisPerItem) {
        List<Item> tagContents = new ArrayList<>();
        Services.ITEMS_REGISTRY.getTag(key).ifPresent(tag -> tag.forEach(tagContents::add));
        if (!tagContents.isEmpty()) {
            // Cycle through each matching stack of the tag and display them one at a time
            int index = (int)((time / millisPerItem) % tagContents.size());
            return new ItemStack(tagContents.get(index));
        }
        return ItemStack.EMPTY;
    }
}
