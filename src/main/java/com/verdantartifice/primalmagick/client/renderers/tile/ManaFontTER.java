package com.verdantartifice.primalmagick.client.renderers.tile;

import java.awt.Color;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.blocks.mana.AbstractManaFontBlock;
import com.verdantartifice.primalmagick.common.tiles.mana.AbstractManaFontTileEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

/**
 * Custom tile entity renderer for mana font blocks.
 * 
 * @author Daedalus4096
 * @see {@link com.verdantartifice.primalmagick.common.blocks.mana.AncientManaFontBlock}
 */
public class ManaFontTER implements BlockEntityRenderer<AbstractManaFontTileEntity> {
    public static final ResourceLocation TEXTURE = PrimalMagick.resource("entity/mana_font_core");
    
    public ManaFontTER(BlockEntityRendererProvider.Context context) {
    }
    
    protected void addVertex(VertexConsumer renderer, PoseStack stack, float x, float y, float z, float r, float g, float b, float u, float v) {
        renderer.vertex(stack.last().pose(), x, y, z)
                .color(r, g, b, 1.0F)
                .uv(u, v)
                .uv2(240, 240)
                .normal(1, 0, 0)
                .endVertex();
    }
    
    @Override
    public void render(AbstractManaFontTileEntity tileEntityIn, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        Minecraft mc = Minecraft.getInstance();
        Block block = tileEntityIn.getBlockState().getBlock();
        if (tileEntityIn != null && block instanceof AbstractManaFontBlock) {
            // Color the tile entity core according to the block's source
            Color sourceColor = new Color(((AbstractManaFontBlock)block).getSource().getColor());
            float r = sourceColor.getRed() / 255.0F;
            float g = sourceColor.getGreen() / 255.0F;
            float b = sourceColor.getBlue() / 255.0F;
            float ds = 0.1875F;
            int rot = (int)(tileEntityIn.getLevel().getLevelData().getGameTime() % 360);
            float scale = (float)tileEntityIn.getMana() / Math.max(20.0F, (float)tileEntityIn.getManaCapacity());    // Shrink the core as it holds less mana
            
            @SuppressWarnings("deprecation")
            TextureAtlasSprite sprite = mc.getModelManager().getAtlas(TextureAtlas.LOCATION_BLOCKS).getSprite(ManaFontTER.TEXTURE);
            VertexConsumer builder = buffer.getBuffer(RenderType.solid());
            
            matrixStack.pushPose();
            matrixStack.translate(0.5D, 0.5D, 0.5D);
            matrixStack.mulPose(Axis.YP.rotationDegrees(rot));   // Spin the core around its Y-axis
            matrixStack.mulPose(Axis.ZP.rotationDegrees(45.0F)); // Tilt the core onto its diagonal
            matrixStack.mulPose(Axis.XP.rotationDegrees(45.0F)); // Tilt the core onto its diagonal
            matrixStack.scale(scale, scale, scale);
            
            // Draw the south face of the core
            this.addVertex(builder, matrixStack, -ds, ds, ds, r, g, b, sprite.getU0(), sprite.getV1());
            this.addVertex(builder, matrixStack, -ds, -ds, ds, r, g, b, sprite.getU0(), sprite.getV0());
            this.addVertex(builder, matrixStack, ds, -ds, ds, r, g, b, sprite.getU1(), sprite.getV0());
            this.addVertex(builder, matrixStack, ds, ds, ds, r, g, b, sprite.getU1(), sprite.getV1());
            
            // Draw the north face of the core
            this.addVertex(builder, matrixStack, -ds, ds, -ds, r, g, b, sprite.getU0(), sprite.getV1());
            this.addVertex(builder, matrixStack, ds, ds, -ds, r, g, b, sprite.getU1(), sprite.getV1());
            this.addVertex(builder, matrixStack, ds, -ds, -ds, r, g, b, sprite.getU1(), sprite.getV0());
            this.addVertex(builder, matrixStack, -ds, -ds, -ds, r, g, b, sprite.getU0(), sprite.getV0());
            
            // Draw the east face of the core
            this.addVertex(builder, matrixStack, ds, ds, -ds, r, g, b, sprite.getU0(), sprite.getV1());
            this.addVertex(builder, matrixStack, ds, ds, ds, r, g, b, sprite.getU1(), sprite.getV1());
            this.addVertex(builder, matrixStack, ds, -ds, ds, r, g, b, sprite.getU1(), sprite.getV0());
            this.addVertex(builder, matrixStack, ds, -ds, -ds, r, g, b, sprite.getU0(), sprite.getV0());
            
            // Draw the west face of the core
            this.addVertex(builder, matrixStack, -ds, -ds, ds, r, g, b, sprite.getU1(), sprite.getV0());
            this.addVertex(builder, matrixStack, -ds, ds, ds, r, g, b, sprite.getU1(), sprite.getV1());
            this.addVertex(builder, matrixStack, -ds, ds, -ds, r, g, b, sprite.getU0(), sprite.getV1());
            this.addVertex(builder, matrixStack, -ds, -ds, -ds, r, g, b, sprite.getU0(), sprite.getV0());
            
            // Draw the top face of the core
            this.addVertex(builder, matrixStack, ds, ds, -ds, r, g, b, sprite.getU1(), sprite.getV0());
            this.addVertex(builder, matrixStack, -ds, ds, -ds, r, g, b, sprite.getU0(), sprite.getV0());
            this.addVertex(builder, matrixStack, -ds, ds, ds, r, g, b, sprite.getU0(), sprite.getV1());
            this.addVertex(builder, matrixStack, ds, ds, ds, r, g, b, sprite.getU1(), sprite.getV1());
            
            // Draw the bottom face of the core
            this.addVertex(builder, matrixStack, ds, -ds, -ds, r, g, b, sprite.getU1(), sprite.getV0());
            this.addVertex(builder, matrixStack, ds, -ds, ds, r, g, b, sprite.getU1(), sprite.getV1());
            this.addVertex(builder, matrixStack, -ds, -ds, ds, r, g, b, sprite.getU0(), sprite.getV1());
            this.addVertex(builder, matrixStack, -ds, -ds, -ds, r, g, b, sprite.getU0(), sprite.getV0());
            
            matrixStack.popPose();
        }
    }
}
