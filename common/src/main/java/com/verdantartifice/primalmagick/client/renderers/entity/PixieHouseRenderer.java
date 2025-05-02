package com.verdantartifice.primalmagick.client.renderers.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.verdantartifice.primalmagick.client.renderers.entity.model.PixieHouseModel;
import com.verdantartifice.primalmagick.client.renderers.models.ModelLayersPM;
import com.verdantartifice.primalmagick.common.entities.misc.PixieHouseEntity;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class PixieHouseRenderer extends LivingEntityRenderer<PixieHouseEntity, PixieHouseModel> {
    public static final ResourceLocation DEFAULT_SKIN_LOCATION = ResourceUtils.loc("textures/entity/pixie_house.png");

    public PixieHouseRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new PixieHouseModel(pContext.bakeLayer(ModelLayersPM.PIXIE_HOUSE)), 0.0F);
    }

    @Override
    public ResourceLocation getTextureLocation(PixieHouseEntity pixieHouseEntity) {
        return DEFAULT_SKIN_LOCATION;
    }

    @Override
    protected void setupRotations(PixieHouseEntity pEntity, PoseStack pPoseStack, float pBob, float pYBodyRot, float pPartialTick, float pScale) {
        super.setupRotations(pEntity, pPoseStack, pBob, pYBodyRot, pPartialTick, pScale);
        float timeSinceLastHit = (float)(pEntity.level().getGameTime() - pEntity.lastHit) + pPartialTick;
        if (timeSinceLastHit < PixieHouseEntity.WOBBLE_TIME) {
            pPoseStack.mulPose(Axis.YP.rotationDegrees(Mth.sin(timeSinceLastHit / 1.5F * (float)Math.PI) * 3.0F));
        }
    }

    @Override
    protected boolean shouldShowName(PixieHouseEntity pEntity) {
        double distSqr = this.entityRenderDispatcher.distanceToSqr(pEntity);
        double f = pEntity.isCrouching() ? 32.0D : 64.0D;
        return !(distSqr >= (f * f)) && pEntity.isCustomNameVisible();
    }
}
