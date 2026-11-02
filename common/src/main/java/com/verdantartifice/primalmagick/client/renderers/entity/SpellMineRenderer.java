package com.verdantartifice.primalmagick.client.renderers.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.verdantartifice.primalmagick.client.renderers.entity.model.SpellMineModel;
import com.verdantartifice.primalmagick.client.renderers.entity.state.SpellMineRenderState;
import com.verdantartifice.primalmagick.client.renderers.models.ModelLayersPM;
import com.verdantartifice.primalmagick.common.entities.projectiles.SpellMineEntity;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

/**
 * Entity renderer for a spell mine.
 * 
 * @author Daedalus4096
 */
public class SpellMineRenderer extends EntityRenderer<SpellMineEntity, SpellMineRenderState> {
    protected static final Identifier TEXTURE = ResourceUtils.loc("textures/entity/spell_projectile.png");
    protected static final RenderType TRANSLUCENT_TYPE = RenderTypes.entityTranslucent(TEXTURE);

    protected final SpellMineModel model;

    public SpellMineRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new SpellMineModel(context.bakeLayer(ModelLayersPM.SPELL_MINE));
    }

    @Override
    public void submit(@NotNull SpellMineRenderState state, @NotNull PoseStack poseStack,
                       @NotNull SubmitNodeCollector submitNodeCollector, @NotNull CameraRenderState camera) {
        float ticks = state.ageInTicks;
        float alphaFactor = state.armed ? 0.25F : 1.0F;    // Fade out the mine if it's armed
        int coreColor = ARGB.color(ARGB.as8BitChannel(1.0F * alphaFactor), state.color);
        int glowColor = ARGB.color(ARGB.as8BitChannel(0.5F * alphaFactor), state.color);
        double bob = 0.25D * Mth.sin(ticks * 0.1F);      // Calculate a vertical bobbing displacement
        poseStack.pushPose();
        poseStack.translate(0.0D, 0.5D + bob, 0.0D);
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.sin(ticks * 0.1F) * 180.0F)); // Spin the mine like a shulker bullet
        poseStack.mulPose(Axis.XP.rotationDegrees(Mth.cos(ticks * 0.1F) * 180.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.sin(ticks * 0.15F) * 360.0F));
        poseStack.scale(-0.5F, -0.5F, 0.5F);
        this.model.setupAnim(state);

        // Render the core of the mine
        submitNodeCollector.submitModel(this.model, state, poseStack, this.model.renderType(TEXTURE), state.lightCoords, OverlayTexture.NO_OVERLAY, coreColor, null, state.outlineColor, null);

        // Render the transparent glow of the mine
        poseStack.scale(1.5F, 1.5F, 1.5F);
        submitNodeCollector.submitModel(this.model, state, poseStack, TRANSLUCENT_TYPE, state.lightCoords, OverlayTexture.NO_OVERLAY, glowColor, null, state.outlineColor, null);

        poseStack.popPose();
        super.submit(state, poseStack, submitNodeCollector, camera);
    }

    @Override
    @NotNull
    public SpellMineRenderState createRenderState() {
        return new SpellMineRenderState();
    }

    @Override
    public void extractRenderState(@NotNull SpellMineEntity entity, @NotNull SpellMineRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.yRot = entity.getYRot(partialTicks);
        state.xRot = entity.getXRot(partialTicks);
        state.color = entity.getColor();
        state.armed = entity.isArmed();
    }
}
