package com.verdantartifice.primalmagick.client.renderers.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagick.common.entities.misc.FriendlyWitchEntity;

import net.minecraft.client.model.WitchModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.WitchItemLayer;
import net.minecraft.resources.ResourceLocation;

/**
 * Entity renderer for a friendly witch.
 * 
 * @author Daedalus4096
 */
public class FriendlyWitchRenderer extends MobRenderer<FriendlyWitchEntity, WitchModel<FriendlyWitchEntity>> {
    private static final ResourceLocation WITCH_LOCATION = new ResourceLocation("textures/entity/witch.png");
    private static final float SCALE = 0.9375F;

    public FriendlyWitchRenderer(EntityRendererProvider.Context context) {
        super(context, new WitchModel<>(context.bakeLayer(ModelLayers.WITCH)), 0.5F);
        this.addLayer(new WitchItemLayer<>(this, context.getItemInHandRenderer()));
    }

    @Override
    public ResourceLocation getTextureLocation(FriendlyWitchEntity entity) {
        return WITCH_LOCATION;
    }

    @Override
    public void render(FriendlyWitchEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        this.model.setHoldingItem(!entity.getMainHandItem().isEmpty());
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    protected void scale(FriendlyWitchEntity entity, PoseStack poseStack, float partialTicks) {
        poseStack.scale(SCALE, SCALE, SCALE);
    }
}
