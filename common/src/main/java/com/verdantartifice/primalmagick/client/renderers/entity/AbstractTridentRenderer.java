package com.verdantartifice.primalmagick.client.renderers.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.verdantartifice.primalmagick.common.entities.projectiles.AbstractTridentEntity;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.object.projectile.TridentModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.ThrownTridentRenderState;
import net.minecraft.client.renderer.feature.ItemFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Unit;
import org.jetbrains.annotations.NotNull;

/**
 * Base renderer definition for a thrown magickal metal trident.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractTridentRenderer extends EntityRenderer<AbstractTridentEntity, ThrownTridentRenderState> {
    protected final TridentModel model;

    public AbstractTridentRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new TridentModel(context.bakeLayer(ModelLayers.TRIDENT));
    }

    protected abstract Identifier getTextureLocation();

    @Override
    public void submit(@NotNull ThrownTridentRenderState state, @NotNull PoseStack poseStack, @NotNull SubmitNodeCollector submitNodeCollector, @NotNull CameraRenderState camera) {
        Identifier textureLoc = this.getTextureLocation();
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(state.yRot - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(state.xRot + 90.0F));
        submitNodeCollector.order(0).submitModel(this.model, Unit.INSTANCE, poseStack, textureLoc, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
        if (state.isFoil) {
            submitNodeCollector.order(1).submitModel(this.model, Unit.INSTANCE, poseStack, ItemFeatureRenderer.getFoilRenderType(this.model.renderType(textureLoc), false), state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
        }
        poseStack.popPose();
        super.submit(state, poseStack, submitNodeCollector, camera);
    }

    @Override
    @NotNull
    public ThrownTridentRenderState createRenderState() {
        return new ThrownTridentRenderState();
    }

    @Override
    public void extractRenderState(@NotNull AbstractTridentEntity entity, @NotNull ThrownTridentRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.yRot = entity.getYRot(partialTicks);
        state.xRot = entity.getXRot(partialTicks);
        state.isFoil = entity.hasGlint();
    }
}
