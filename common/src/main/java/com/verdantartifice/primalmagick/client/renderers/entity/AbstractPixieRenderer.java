package com.verdantartifice.primalmagick.client.renderers.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagick.client.renderers.entity.model.PixieModel;
import com.verdantartifice.primalmagick.common.entities.companions.pixies.AbstractPixieEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.Mth;

/**
 * Base entity renderer for a pixie.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractPixieRenderer extends MobRenderer<AbstractPixieEntity, PixieModel> {
    public AbstractPixieRenderer(EntityRendererProvider.Context context, PixieModel model) {
        super(context, model, 0.25F);
    }

    @Override
    protected void scale(AbstractPixieEntity entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
        matrixStackIn.scale(0.25F, 0.25F, 0.25F);
    }

    @Override
    protected void setupRotations(AbstractPixieEntity entityLiving, PoseStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks, float scale) {
        matrixStackIn.translate(0.0D, (double)(Mth.cos(ageInTicks * 0.3F) * 0.1F), 0.0D);
        super.setupRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks, scale);
    }
}
