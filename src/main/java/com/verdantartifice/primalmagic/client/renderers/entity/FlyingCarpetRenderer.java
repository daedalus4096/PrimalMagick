package com.verdantartifice.primalmagic.client.renderers.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.renderers.entity.model.FlyingCarpetModel;
import com.verdantartifice.primalmagic.common.entities.misc.FlyingCarpetEntity;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Entity renderer for a flying carpet.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
public class FlyingCarpetRenderer extends EntityRenderer<FlyingCarpetEntity> {
    // TODO Add other texture locations
    private static final ResourceLocation TEXTURE_WHITE = new ResourceLocation(PrimalMagic.MODID, "textures/entity/flying_carpet/flying_carpet_white.png");
    
    protected final FlyingCarpetModel model = new FlyingCarpetModel();

    public FlyingCarpetRenderer(EntityRendererManager renderManager) {
        super(renderManager);
        this.shadowSize = 0.5F;
    }

    @Override
    public ResourceLocation getEntityTexture(FlyingCarpetEntity entity) {
        // TODO Select texture based on entity color value
        return TEXTURE_WHITE;
    }

    @Override
    public void render(FlyingCarpetEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        matrixStackIn.push();
        matrixStackIn.translate(0.0D, 0.375D, 0.0D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180.0F - entityYaw));  // FIXME should be 90 - yaw?
        matrixStackIn.scale(-1.0F, -1.0F, 1.0F);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90.0F));
        
        IVertexBuilder ivertexbuilder = bufferIn.getBuffer(this.model.getRenderType(this.getEntityTexture(entityIn)));
        this.model.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        matrixStackIn.pop();
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }
}
