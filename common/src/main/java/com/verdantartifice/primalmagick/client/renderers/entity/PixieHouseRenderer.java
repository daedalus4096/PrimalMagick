package com.verdantartifice.primalmagick.client.renderers.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.verdantartifice.primalmagick.client.renderers.entity.layers.PixieHouseOccupantLayer;
import com.verdantartifice.primalmagick.client.renderers.entity.model.PixieHouseModel;
import com.verdantartifice.primalmagick.client.renderers.entity.state.PixieHouseRenderState;
import com.verdantartifice.primalmagick.client.renderers.models.ModelLayersPM;
import com.verdantartifice.primalmagick.common.entities.misc.PixieHouseEntity;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class PixieHouseRenderer extends LivingEntityRenderer<PixieHouseEntity, PixieHouseRenderState, PixieHouseModel> {
    public static final Identifier DEFAULT_SKIN_LOCATION = ResourceUtils.loc("textures/entity/pixie_house.png");

    public PixieHouseRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new PixieHouseModel(pContext.bakeLayer(ModelLayersPM.PIXIE_HOUSE)), 0.0F);
        this.addLayer(new PixieHouseOccupantLayer(this, pContext.getModelSet()));
    }

    @Override
    @NotNull
    public Identifier getTextureLocation(PixieHouseRenderState renderState) {
        return DEFAULT_SKIN_LOCATION;
    }

    @Override
    protected void setupRotations(PixieHouseRenderState pRenderState, PoseStack pPoseStack, float pBodyRot, float pScale) {
        super.setupRotations(pRenderState, pPoseStack, pBodyRot, pScale);
        if (pRenderState.wiggle < PixieHouseEntity.WOBBLE_TIME) {
            pPoseStack.mulPose(Axis.YP.rotationDegrees(Mth.sin(pRenderState.wiggle / 1.5F * (float)Math.PI) * 3.0F));
        }
    }

    @Override
    protected boolean shouldShowName(PixieHouseEntity pEntity, double pDistanceToCameraSq) {
        double f = pEntity.isCrouching() ? 32.0D : 64.0D;
        return !(pDistanceToCameraSq >= (f * f)) && pEntity.isCustomNameVisible();
    }

    @Override
    public PixieHouseRenderState createRenderState() {
        return new PixieHouseRenderState();
    }

    @Override
    public void extractRenderState(PixieHouseEntity pEntity, PixieHouseRenderState pRenderState, float pPartialTicks) {
        super.extractRenderState(pEntity, pRenderState, pPartialTicks);
        pRenderState.wiggle = (float)(pEntity.level().getGameTime() - pEntity.lastHit) + pPartialTicks;
    }
}
