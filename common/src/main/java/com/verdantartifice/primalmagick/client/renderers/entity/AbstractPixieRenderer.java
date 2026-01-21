package com.verdantartifice.primalmagick.client.renderers.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagick.client.renderers.entity.model.PixieModel;
import com.verdantartifice.primalmagick.client.renderers.entity.state.PixieRenderState;
import com.verdantartifice.primalmagick.common.entities.pixies.IPixie;
import com.verdantartifice.primalmagick.common.entities.pixies.companions.AbstractPixieEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;

/**
 * Base entity renderer for a pixie.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractPixieRenderer<T extends Mob & IPixie> extends MobRenderer<T, PixieRenderState, PixieModel> {
    public AbstractPixieRenderer(EntityRendererProvider.Context context, PixieModel model) {
        super(context, model, 0.25F);
    }

    @Override
    public PixieRenderState createRenderState() {
        return new PixieRenderState();
    }

    @Override
    protected void scale(PixieRenderState renderState, PoseStack poseStack) {
        poseStack.scale(0.25F, 0.25F, 0.25F);
    }

    @Override
    protected void setupRotations(PixieRenderState renderState, PoseStack poseStack, float bodyRot, float scale) {
        poseStack.translate(0.0D, Mth.cos(renderState.ageInTicks * 0.3F) * 0.1F, 0.0D);
        super.setupRotations(renderState, poseStack, bodyRot, scale);
    }
}
