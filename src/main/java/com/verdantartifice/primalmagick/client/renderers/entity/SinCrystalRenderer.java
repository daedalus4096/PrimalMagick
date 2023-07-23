package com.verdantartifice.primalmagick.client.renderers.entity;

import org.joml.Quaternionf;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.verdantartifice.primalmagick.common.entities.misc.SinCrystalEntity;

import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EnderDragonRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class SinCrystalRenderer extends EntityRenderer<SinCrystalEntity> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/end_crystal/end_crystal.png");
    protected static final RenderType RENDER_TYPE = RenderType.entityCutoutNoCull(TEXTURE);
    protected static final float ANGLE = (float)Math.sin((Math.PI / 4D));
    
    protected final ModelPart glass;
    protected final ModelPart cube;

    public SinCrystalRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.5F;
        ModelPart model = context.bakeLayer(ModelLayers.END_CRYSTAL);
        this.glass = model.getChild("glass");
        this.cube = model.getChild("cube");
    }
    
    @Override
    public ResourceLocation getTextureLocation(SinCrystalEntity entity) {
        return TEXTURE;
    }

    @Override
    public boolean shouldRender(SinCrystalEntity livingEntityIn, Frustum camera, double camX, double camY, double camZ) {
        return super.shouldRender(livingEntityIn, camera, camX, camY, camZ) || livingEntityIn.getBeamTarget() != null;
    }

    public static float getDeltaY(SinCrystalEntity entity, float partialTicks) {
        float f = (float)entity.innerRotation + partialTicks;
        float f1 = Mth.sin(f * 0.2F) / 2.0F + 0.5F;
        f1 = (f1 * f1 + f1) * 0.4F;
        return f1 - 1.4F;
    }

    @Override
    public void render(SinCrystalEntity entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        matrixStackIn.pushPose();
        float deltaY = getDeltaY(entityIn, partialTicks);
        float f1 = ((float)entityIn.innerRotation + partialTicks) * 3.0F;
        VertexConsumer ivertexbuilder = bufferIn.getBuffer(RENDER_TYPE);
        matrixStackIn.pushPose();
        matrixStackIn.scale(2.0F, 2.0F, 2.0F);
        matrixStackIn.translate(0.0D, -0.5D, 0.0D);
        int i = OverlayTexture.NO_OVERLAY;

        matrixStackIn.mulPose(Axis.YP.rotationDegrees(f1));
        matrixStackIn.translate(0.0D, (double)(1.5F + deltaY / 2.0F), 0.0D);
        matrixStackIn.mulPose((new Quaternionf()).setAngleAxis(((float)Math.PI / 3F), ANGLE, 0.0F, ANGLE));
        this.glass.render(matrixStackIn, ivertexbuilder, packedLightIn, i);
        matrixStackIn.scale(0.875F, 0.875F, 0.875F);
        matrixStackIn.mulPose((new Quaternionf()).setAngleAxis(((float)Math.PI / 3F), ANGLE, 0.0F, ANGLE));
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(f1));
        this.glass.render(matrixStackIn, ivertexbuilder, packedLightIn, i);
        matrixStackIn.scale(0.875F, 0.875F, 0.875F);
        matrixStackIn.mulPose((new Quaternionf()).setAngleAxis(((float)Math.PI / 3F), ANGLE, 0.0F, ANGLE));
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(f1));
        this.cube.render(matrixStackIn, ivertexbuilder, packedLightIn, i);
        matrixStackIn.popPose();
        matrixStackIn.popPose();
        BlockPos blockpos = entityIn.getBeamTarget();
        if (blockpos != null) {
            float targetX = (float)blockpos.getX() + 0.5F;
            float targetY = (float)blockpos.getY() + 0.5F;
            float targetZ = (float)blockpos.getZ() + 0.5F;
            float dx = (float)((double)targetX - entityIn.getX());
            float dy = (float)((double)targetY - entityIn.getY());
            float dz = (float)((double)targetZ - entityIn.getZ());
            matrixStackIn.translate((double)dx, (double)dy, (double)dz);
            EnderDragonRenderer.renderCrystalBeams(-dx, -dy + deltaY, -dz, partialTicks, entityIn.innerRotation, matrixStackIn, bufferIn, packedLightIn);
        }

        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }
}
