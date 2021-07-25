package com.verdantartifice.primalmagic.client.renderers.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagic.client.renderers.entity.model.PixieModel;
import com.verdantartifice.primalmagic.common.entities.companions.pixies.AbstractPixieEntity;

import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.Mth;

/**
 * Base entity renderer for a pixie.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractPixieRenderer extends MobRenderer<AbstractPixieEntity, PixieModel> {
    public AbstractPixieRenderer(EntityRenderDispatcher renderManagerIn, PixieModel model) {
        super(renderManagerIn, model, 0.25F);
    }

    @Override
    protected void scale(AbstractPixieEntity entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
        matrixStackIn.scale(0.25F, 0.25F, 0.25F);
    }

    @Override
    protected void setupRotations(AbstractPixieEntity entityLiving, PoseStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
        matrixStackIn.translate(0.0D, (double)(Mth.cos(ageInTicks * 0.3F) * 0.1F), 0.0D);
        super.setupRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
    }
}
