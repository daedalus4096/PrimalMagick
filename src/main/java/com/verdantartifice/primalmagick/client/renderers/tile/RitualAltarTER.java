package com.verdantartifice.primalmagick.client.renderers.tile;

import java.awt.Color;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.verdantartifice.primalmagick.client.fx.FxDispatcher;
import com.verdantartifice.primalmagick.common.tiles.rituals.RitualAltarTileEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

/**
 * Custom tile entity renderer for ritual altar tile entities.
 * 
 * @author Daedalus4096
 * @see {@link com.verdantartifice.primalmagick.common.tiles.rituals.RitualAltarTileEntity}
 */
public class RitualAltarTER implements BlockEntityRenderer<RitualAltarTileEntity> {
    public RitualAltarTER(BlockEntityRendererProvider.Context context) {
    }
    
    protected void addVertex(VertexConsumer renderer, PoseStack stack, float x, float y, float z, float r, float g, float b, float a, float u, float v) {
        renderer.vertex(stack.last().pose(), x, y, z)
                .color(r, g, b, a)
                .uv(u, v)
                .uv2(240, 240)
                .normal(1, 0, 0)
                .endVertex();
    }
    
    protected void renderCube(VertexConsumer builder, PoseStack matrixStack, float ds, float r, float g, float b, float a, TextureAtlasSprite sprite) {
        // Draw the south face of the cube
        this.addVertex(builder, matrixStack, -ds, ds, ds, r, g, b, a, sprite.getU0(), sprite.getV1());
        this.addVertex(builder, matrixStack, -ds, -ds, ds, r, g, b, a, sprite.getU0(), sprite.getV0());
        this.addVertex(builder, matrixStack, ds, -ds, ds, r, g, b, a, sprite.getU1(), sprite.getV0());
        this.addVertex(builder, matrixStack, ds, ds, ds, r, g, b, a, sprite.getU1(), sprite.getV1());
        
        // Draw the north face of the cube
        this.addVertex(builder, matrixStack, -ds, ds, -ds, r, g, b, a, sprite.getU0(), sprite.getV1());
        this.addVertex(builder, matrixStack, ds, ds, -ds, r, g, b, a, sprite.getU1(), sprite.getV1());
        this.addVertex(builder, matrixStack, ds, -ds, -ds, r, g, b, a, sprite.getU1(), sprite.getV0());
        this.addVertex(builder, matrixStack, -ds, -ds, -ds, r, g, b, a, sprite.getU0(), sprite.getV0());
        
        // Draw the east face of the cube
        this.addVertex(builder, matrixStack, ds, ds, -ds, r, g, b, a, sprite.getU0(), sprite.getV1());
        this.addVertex(builder, matrixStack, ds, ds, ds, r, g, b, a, sprite.getU1(), sprite.getV1());
        this.addVertex(builder, matrixStack, ds, -ds, ds, r, g, b, a, sprite.getU1(), sprite.getV0());
        this.addVertex(builder, matrixStack, ds, -ds, -ds, r, g, b, a, sprite.getU0(), sprite.getV0());
        
        // Draw the west face of the cube
        this.addVertex(builder, matrixStack, -ds, -ds, ds, r, g, b, a, sprite.getU1(), sprite.getV0());
        this.addVertex(builder, matrixStack, -ds, ds, ds, r, g, b, a, sprite.getU1(), sprite.getV1());
        this.addVertex(builder, matrixStack, -ds, ds, -ds, r, g, b, a, sprite.getU0(), sprite.getV1());
        this.addVertex(builder, matrixStack, -ds, -ds, -ds, r, g, b, a, sprite.getU0(), sprite.getV0());
        
        // Draw the top face of the cube
        this.addVertex(builder, matrixStack, ds, ds, -ds, r, g, b, a, sprite.getU1(), sprite.getV0());
        this.addVertex(builder, matrixStack, -ds, ds, -ds, r, g, b, a, sprite.getU0(), sprite.getV0());
        this.addVertex(builder, matrixStack, -ds, ds, ds, r, g, b, a, sprite.getU0(), sprite.getV1());
        this.addVertex(builder, matrixStack, ds, ds, ds, r, g, b, a, sprite.getU1(), sprite.getV1());
        
        // Draw the bottom face of the cube
        this.addVertex(builder, matrixStack, ds, -ds, -ds, r, g, b, a, sprite.getU1(), sprite.getV0());
        this.addVertex(builder, matrixStack, ds, -ds, ds, r, g, b, a, sprite.getU1(), sprite.getV1());
        this.addVertex(builder, matrixStack, -ds, -ds, ds, r, g, b, a, sprite.getU0(), sprite.getV1());
        this.addVertex(builder, matrixStack, -ds, -ds, -ds, r, g, b, a, sprite.getU0(), sprite.getV0());
    }
    
    @Override
    public void render(RitualAltarTileEntity tileEntityIn, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLightIn, int combinedOverlayIn) {
        if (tileEntityIn == null) {
            return;
        }

        // Render the held item stack above the altar
        Minecraft mc = Minecraft.getInstance();
        ItemStack stack = tileEntityIn.getSyncedStack().copy();
        if (!stack.isEmpty()) {
            int rot = (int)(tileEntityIn.getLevel().getLevelData().getGameTime() % 360);
            matrixStack.pushPose();
            matrixStack.translate(0.5D, 1.5D, 0.5D);
            matrixStack.mulPose(Axis.YP.rotationDegrees(rot));   // Spin the stack around its Y-axis
            matrixStack.scale(0.75F, 0.75F, 0.75F);
            Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.GUI, combinedLightIn, combinedOverlayIn, matrixStack, buffer, tileEntityIn.getLevel(), 0);
            matrixStack.popPose();
        }

        // Render the ritual orb above the altar if active
        if (tileEntityIn.isActive()) {
            Color color = tileEntityIn.getOrbColor();
            float r = color.getRed() / 255.0F;
            float g = color.getGreen() / 255.0F;
            float b = color.getBlue() / 255.0F;
            float ds = 0.1875F;
            float ticks = (float)tileEntityIn.getActiveCount() + partialTicks;

            @SuppressWarnings("deprecation")
            TextureAtlasSprite sprite = mc.getModelManager().getAtlas(TextureAtlas.LOCATION_BLOCKS).getSprite(ManaFontTER.TEXTURE);
            VertexConsumer builder = buffer.getBuffer(RenderType.solid());  // FIXME Revert to translucent once Fabulous graphics bug is fixed
            
            matrixStack.pushPose();
            matrixStack.translate(0.5D, 2.5D, 0.5D);
            matrixStack.mulPose(Axis.YP.rotationDegrees(Mth.sin(ticks * 0.1F) * 180.0F)); // Spin the orb like a shulker bullet
            matrixStack.mulPose(Axis.XP.rotationDegrees(Mth.cos(ticks * 0.1F) * 180.0F));
            matrixStack.mulPose(Axis.ZP.rotationDegrees(Mth.sin(ticks * 0.15F) * 360.0F));
            this.renderCube(builder, matrixStack, ds, r, g, b, 1.0F, sprite);
            
            // FIXME Uncomment once Fabulous graphics bug is fixed
//            matrixStack.scale(1.5F, 1.5F, 1.5F);
//            this.renderCube(builder, matrixStack, ds, r, g, b, 0.5F, sprite);
            
            matrixStack.popPose();
            
            FxDispatcher.INSTANCE.ritualGlow(tileEntityIn.getBlockPos(), color.getRGB());
        }
    }
}
