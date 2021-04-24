package com.verdantartifice.primalmagic.client.renderers.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.verdantartifice.primalmagic.client.renderers.entity.model.EnchantedGolemModel;
import com.verdantartifice.primalmagic.common.entities.companions.golems.AbstractEnchantedGolemEntity;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.math.vector.Vector3f;

/**
 * Base entity renderer for an enchanted golem.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractEnchantedGolemRenderer<T extends AbstractEnchantedGolemEntity> extends MobRenderer<T, EnchantedGolemModel<T>> {
    public AbstractEnchantedGolemRenderer(EntityRendererManager rendererManager) {
        super(rendererManager, new EnchantedGolemModel<>(), 0.7F);
    }

    @Override
    protected void applyRotations(T entityLiving, MatrixStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
        super.applyRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
        if (!((double)entityLiving.limbSwingAmount < 0.01D)) {
            float f = 13.0F;
            float f1 = entityLiving.limbSwing - entityLiving.limbSwingAmount * (1.0F - partialTicks) + 6.0F;
            float f2 = (Math.abs(f1 % f - 6.5F) - 3.25F) / 3.25F;
            matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(6.5F * f2));
        }
    }
}
