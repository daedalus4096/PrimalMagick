package com.verdantartifice.primalmagick.client.renderers.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.verdantartifice.primalmagick.client.renderers.entity.model.EnchantedGolemModel;
import com.verdantartifice.primalmagick.common.entities.companions.golems.AbstractEnchantedGolemEntity;

import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;

/**
 * Base entity renderer for an enchanted golem.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractEnchantedGolemRenderer<T extends AbstractEnchantedGolemEntity> extends MobRenderer<T, EnchantedGolemModel<T>> {
    public AbstractEnchantedGolemRenderer(EntityRendererProvider.Context context) {
        super(context, new EnchantedGolemModel<>(context.bakeLayer(ModelLayers.IRON_GOLEM)), 0.7F);
    }

    @Override
    protected void setupRotations(T entityLiving, PoseStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
        super.setupRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
        if (!((double)entityLiving.walkAnimation.speed() < 0.01D)) {
            float f = 13.0F;
            float f1 = entityLiving.walkAnimation.position(partialTicks) + 6.0F;
            float f2 = (Math.abs(f1 % f - 6.5F) - 3.25F) / 3.25F;
            matrixStackIn.mulPose(Axis.ZP.rotationDegrees(6.5F * f2));
        }
    }
}
