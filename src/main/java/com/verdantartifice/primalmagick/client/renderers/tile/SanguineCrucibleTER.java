package com.verdantartifice.primalmagick.client.renderers.tile;

import java.awt.Color;

import org.joml.Matrix4f;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.verdantartifice.primalmagick.client.fx.FxDispatcher;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.tiles.devices.SanguineCrucibleTileEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;

/**
 * Custom tile entity renderer for sanguine crucible blocks.
 * 
 * @author Daedalus4096
 * @see {@link com.verdantartifice.primalmagick.common.blocks.devices.SanguineCrucibleBlock}
 */
public class SanguineCrucibleTER implements BlockEntityRenderer<SanguineCrucibleTileEntity> {
    protected static final ResourceLocation WATER_TEXTURE = new ResourceLocation("block/water_still");
    protected static final Color COLOR = new Color(Source.BLOOD.getColor()).brighter().brighter();
    protected static final float R = COLOR.getRed() / 255.0F;
    protected static final float G = COLOR.getGreen() / 255.0F;
    protected static final float B = COLOR.getBlue() / 255.0F;
    
    public SanguineCrucibleTER(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(SanguineCrucibleTileEntity tileEntityIn, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        Minecraft mc = Minecraft.getInstance();
        
        matrixStackIn.pushPose();
        matrixStackIn.translate(0.0D, tileEntityIn.getFluidHeight(), 0.0D);
        matrixStackIn.mulPose(Axis.XP.rotationDegrees(90.0F));
        
        @SuppressWarnings("deprecation")
        TextureAtlasSprite sprite = mc.getModelManager().getAtlas(TextureAtlas.LOCATION_BLOCKS).getSprite(WATER_TEXTURE);
        VertexConsumer builder = bufferIn.getBuffer(RenderType.solid());
        Matrix4f matrix = matrixStackIn.last().pose();
        builder.vertex(matrix, 0.0F, 1.0F, 0.0F).color(R, G, B, 1.0F).uv(sprite.getU0(), sprite.getV1()).uv2(0, 240).normal(1, 0, 0).endVertex();
        builder.vertex(matrix, 1.0F, 1.0F, 0.0F).color(R, G, B, 1.0F).uv(sprite.getU1(), sprite.getV1()).uv2(0, 240).normal(1, 0, 0).endVertex();
        builder.vertex(matrix, 1.0F, 0.0F, 0.0F).color(R, G, B, 1.0F).uv(sprite.getU1(), sprite.getV0()).uv2(0, 240).normal(1, 0, 0).endVertex();
        builder.vertex(matrix, 0.0F, 0.0F, 0.0F).color(R, G, B, 1.0F).uv(sprite.getU0(), sprite.getV0()).uv2(0, 240).normal(1, 0, 0).endVertex();
        
        matrixStackIn.popPose();
        
        Level world = tileEntityIn.getLevel();
        RandomSource rand = world.random;
        BlockPos pos = tileEntityIn.getBlockPos();
        if (tileEntityIn.showBubble(rand)) {
            double x = (double)pos.getX() + 0.2D + (rand.nextDouble() * 0.6D);
            double y = (double)pos.getY() + (double)tileEntityIn.getFluidHeight();
            double z = (double)pos.getZ() + 0.2D + (rand.nextDouble() * 0.6D);
            FxDispatcher.INSTANCE.crucibleBubble(x, y, z, R, G, B);
        }
        if (rand.nextDouble() < tileEntityIn.getSmokeChance()) {
            double x = (double)pos.getX() + 0.1D + (rand.nextDouble() * 0.8D);
            double y = (double)pos.getY() + 1.0D;
            double z = (double)pos.getZ() + 0.1D + (rand.nextDouble() * 0.8D);
            world.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0D, 0.1D, 0.0D);
        }
    }
}
