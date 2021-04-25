package com.verdantartifice.primalmagic.client.renderers.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.verdantartifice.primalmagic.client.renderers.entity.model.BasicEarthPixieModel;
import com.verdantartifice.primalmagic.common.entities.companions.pixies.BasicEarthPixieEntity;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

/**
 * Entity renderer for a basic earth pixie.
 * 
 * @author Daedalus4096
 */
public class BasicEarthPixieRenderer extends MobRenderer<BasicEarthPixieEntity, BasicEarthPixieModel> {
    // TODO Create custom renderer
    private static final ResourceLocation BAT_TEXTURES = new ResourceLocation("textures/entity/bat.png");

    public BasicEarthPixieRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new BasicEarthPixieModel(), 0.25F);
    }

    @Override
    public ResourceLocation getEntityTexture(BasicEarthPixieEntity entity) {
        return BAT_TEXTURES;
    }

    @Override
    protected void preRenderCallback(BasicEarthPixieEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
        matrixStackIn.scale(0.35F, 0.35F, 0.35F);
    }

    @Override
    protected void applyRotations(BasicEarthPixieEntity entityLiving, MatrixStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
        matrixStackIn.translate(0.0D, (double)(MathHelper.cos(ageInTicks * 0.3F) * 0.1F), 0.0D);
        super.applyRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
    }
}
