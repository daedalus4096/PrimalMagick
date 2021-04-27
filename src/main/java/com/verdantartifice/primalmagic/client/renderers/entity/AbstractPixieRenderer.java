package com.verdantartifice.primalmagic.client.renderers.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.verdantartifice.primalmagic.client.renderers.entity.model.PixieModel;
import com.verdantartifice.primalmagic.common.entities.companions.pixies.AbstractPixieEntity;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.math.MathHelper;

/**
 * Base entity renderer for a pixie.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractPixieRenderer extends MobRenderer<AbstractPixieEntity, PixieModel> {
    public AbstractPixieRenderer(EntityRendererManager renderManagerIn, PixieModel model) {
        super(renderManagerIn, model, 0.25F);
    }

    @Override
    protected void preRenderCallback(AbstractPixieEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
        matrixStackIn.scale(0.25F, 0.25F, 0.25F);
    }

    @Override
    protected void applyRotations(AbstractPixieEntity entityLiving, MatrixStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
        matrixStackIn.translate(0.0D, (double)(MathHelper.cos(ageInTicks * 0.3F) * 0.1F), 0.0D);
        super.applyRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
    }
}
