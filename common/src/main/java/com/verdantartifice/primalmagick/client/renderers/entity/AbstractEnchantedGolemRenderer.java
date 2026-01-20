package com.verdantartifice.primalmagick.client.renderers.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.verdantartifice.primalmagick.client.renderers.entity.model.EnchantedGolemModel;
import com.verdantartifice.primalmagick.client.renderers.entity.state.EnchantedGolemRenderState;
import com.verdantartifice.primalmagick.common.entities.golems.AbstractEnchantedGolemEntity;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import org.jetbrains.annotations.NotNull;

/**
 * Base entity renderer for an enchanted golem.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractEnchantedGolemRenderer<T extends AbstractEnchantedGolemEntity> extends MobRenderer<T, EnchantedGolemRenderState, EnchantedGolemModel> {
    public AbstractEnchantedGolemRenderer(EntityRendererProvider.Context context) {
        super(context, new EnchantedGolemModel(context.bakeLayer(ModelLayers.IRON_GOLEM)), 0.7F);
    }

    @Override
    public EnchantedGolemRenderState createRenderState() {
        return new EnchantedGolemRenderState();
    }

    @Override
    protected void setupRotations(EnchantedGolemRenderState renderState, @NotNull PoseStack poseStack, float bodyRot, float scale) {
        super.setupRotations(renderState, poseStack, bodyRot, scale);
        if (!((double)renderState.walkAnimationSpeed < 0.01D)) {
            float f = 13.0F;
            float f1 = renderState.walkAnimationPos + 6.0F;
            float f2 = (Math.abs(f1 % f - 6.5F) - 3.25F) / 3.25F;
            poseStack.mulPose(Axis.ZP.rotationDegrees(6.5F * f2));
        }
    }
}
