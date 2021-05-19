package com.verdantartifice.primalmagic.client.renderers.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.verdantartifice.primalmagic.common.entities.misc.SinCrystalEntity;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.entity.EnderDragonRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;

public class SinCrystalRenderer extends EntityRenderer<SinCrystalEntity> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/end_crystal/end_crystal.png");
    protected static final RenderType RENDER_TYPE = RenderType.getEntityCutoutNoCull(TEXTURE);
    protected static final float ANGLE = (float)Math.sin((Math.PI / 4D));
    
    protected final ModelRenderer glassModelRenderer;
    protected final ModelRenderer runeModelRenderer;

    public SinCrystalRenderer(EntityRendererManager renderManager) {
        super(renderManager);
        this.shadowSize = 0.5F;
        this.glassModelRenderer = new ModelRenderer(64, 32, 0, 0);
        this.glassModelRenderer.addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F);
        this.runeModelRenderer = new ModelRenderer(64, 32, 32, 0);
        this.runeModelRenderer.addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F);
    }

    @Override
    public ResourceLocation getEntityTexture(SinCrystalEntity entity) {
        return TEXTURE;
    }

    @Override
    public boolean shouldRender(SinCrystalEntity livingEntityIn, ClippingHelper camera, double camX, double camY, double camZ) {
        return super.shouldRender(livingEntityIn, camera, camX, camY, camZ) || livingEntityIn.getBeamTarget() != null;
    }

    public float getDeltaY(SinCrystalEntity entity, float partialTicks) {
        float f = (float)entity.innerRotation + partialTicks;
        float f1 = MathHelper.sin(f * 0.2F) / 2.0F + 0.5F;
        f1 = (f1 * f1 + f1) * 0.4F;
        return f1 - 1.4F;
    }

    @Override
    public void render(SinCrystalEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        matrixStackIn.push();
        float deltaY = this.getDeltaY(entityIn, partialTicks);
        float f1 = ((float)entityIn.innerRotation + partialTicks) * 3.0F;
        IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RENDER_TYPE);
        matrixStackIn.push();
        matrixStackIn.scale(2.0F, 2.0F, 2.0F);
        matrixStackIn.translate(0.0D, -0.5D, 0.0D);
        int i = OverlayTexture.NO_OVERLAY;

        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(f1));
        matrixStackIn.translate(0.0D, (double)(1.5F + deltaY / 2.0F), 0.0D);
        matrixStackIn.rotate(new Quaternion(new Vector3f(ANGLE, 0.0F, ANGLE), 60.0F, true));
        this.glassModelRenderer.render(matrixStackIn, ivertexbuilder, packedLightIn, i);
        matrixStackIn.scale(0.875F, 0.875F, 0.875F);
        matrixStackIn.rotate(new Quaternion(new Vector3f(ANGLE, 0.0F, ANGLE), 60.0F, true));
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(f1));
        this.glassModelRenderer.render(matrixStackIn, ivertexbuilder, packedLightIn, i);
        matrixStackIn.scale(0.875F, 0.875F, 0.875F);
        matrixStackIn.rotate(new Quaternion(new Vector3f(ANGLE, 0.0F, ANGLE), 60.0F, true));
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(f1));
        this.runeModelRenderer.render(matrixStackIn, ivertexbuilder, packedLightIn, i);
        matrixStackIn.pop();
        matrixStackIn.pop();
        BlockPos blockpos = entityIn.getBeamTarget();
        if (blockpos != null) {
            float targetX = (float)blockpos.getX() + 0.5F;
            float targetY = (float)blockpos.getY() + 0.5F;
            float targetZ = (float)blockpos.getZ() + 0.5F;
            float dx = (float)((double)targetX - entityIn.getPosX());
            float dy = (float)((double)targetY - entityIn.getPosY());
            float dz = (float)((double)targetZ - entityIn.getPosZ());
            matrixStackIn.translate((double)dx, (double)dy, (double)dz);
            EnderDragonRenderer.func_229059_a_(-dx, -dy + deltaY, -dz, partialTicks, entityIn.innerRotation, matrixStackIn, bufferIn, packedLightIn);
        }

        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }
}
