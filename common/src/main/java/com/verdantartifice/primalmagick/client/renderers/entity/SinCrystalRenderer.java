package com.verdantartifice.primalmagick.client.renderers.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagick.common.entities.misc.SinCrystalEntity;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.object.crystal.EndCrystalModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EnderDragonRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EndCrystalRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

/**
 * Renderer for a sin crystal entity. Looks and functions like the end dragon's crystals, but are summoned by
 * an Inner Demon entity for use in that fight. They also summon a damage cloud around themselves.
 * 
 * @author Daedalus4096
 */
public class SinCrystalRenderer extends EntityRenderer<SinCrystalEntity, EndCrystalRenderState> {
    protected static final Identifier TEXTURE = Identifier.withDefaultNamespace("textures/entity/end_crystal/end_crystal.png");

    private final EndCrystalModel model;

    public SinCrystalRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.5F;
        this.model = new EndCrystalModel(context.bakeLayer(ModelLayers.END_CRYSTAL));
    }
    
    @Override
    public boolean shouldRender(@NotNull SinCrystalEntity livingEntityIn, @NotNull Frustum camera, double camX, double camY, double camZ) {
        return super.shouldRender(livingEntityIn, camera, camX, camY, camZ) || livingEntityIn.getBeamTarget().isPresent();
    }

    public static float getDeltaY(float timeInTicks) {
        float f1 = Mth.sin(timeInTicks * 0.2F) / 2.0F + 0.5F;
        f1 = (f1 * f1 + f1) * 0.4F;
        return f1 - 1.4F;
    }

    @Override
    @NotNull
    public EndCrystalRenderState createRenderState() {
        return new EndCrystalRenderState();
    }

    @Override
    public void extractRenderState(@NotNull SinCrystalEntity entity, @NotNull EndCrystalRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.ageInTicks = (float)entity.innerRotation + partialTicks;
        state.showsBottom = false;  // Unlike end crystals, sin crystals just hover without a base
        state.beamOffset = entity.getBeamTarget().map(target -> Vec3.atCenterOf(target).subtract(entity.getPosition(partialTicks))).orElse(null);
    }

    @Override
    public void submit(@NotNull EndCrystalRenderState state, @NotNull PoseStack poseStack,
                       @NotNull SubmitNodeCollector submitNodeCollector, @NotNull CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.scale(2.0F, 2.0F, 2.0F);
        poseStack.translate(0.0F, -0.5F, 0.0F);
        submitNodeCollector.submitModel(this.model, state, poseStack, TEXTURE, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
        poseStack.popPose();
        Vec3 beamOffset = state.beamOffset;
        if (beamOffset != null) {
            float crystalY = getDeltaY(state.ageInTicks);
            float deltaX = (float)beamOffset.x;
            float deltaY = (float)beamOffset.y;
            float deltaZ = (float)beamOffset.z;
            poseStack.translate(beamOffset);
            EnderDragonRenderer.submitCrystalBeams(-deltaX, -deltaY + crystalY, -deltaZ, state.ageInTicks, poseStack, submitNodeCollector, state.lightCoords);
        }

        super.submit(state, poseStack, submitNodeCollector, camera);
    }
}
