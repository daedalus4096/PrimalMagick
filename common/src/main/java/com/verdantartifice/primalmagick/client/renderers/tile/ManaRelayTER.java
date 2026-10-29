package com.verdantartifice.primalmagick.client.renderers.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.verdantartifice.primalmagick.client.fx.FxDispatcher;
import com.verdantartifice.primalmagick.client.renderers.models.ModelLayersPM;
import com.verdantartifice.primalmagick.client.renderers.tile.model.ManaCubeModel;
import com.verdantartifice.primalmagick.client.renderers.tile.model.ManaRelayFrameModel;
import com.verdantartifice.primalmagick.common.misc.DeviceTier;
import com.verdantartifice.primalmagick.common.tiles.mana.ManaRelayTileEntity;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Custom tile entity renderer for mana relay blocks.
 *
 * @author Daedalus4096
 */
public class ManaRelayTER implements BlockEntityRenderer<ManaRelayTileEntity, ManaRelayTER.RenderState> {
    public static final SpriteId BASIC_FRAME = Sheets.BLOCK_ENTITIES_MAPPER.apply(ResourceUtils.loc("mana_relay/basic_frame"));
    public static final SpriteId ENCHANTED_FRAME = Sheets.BLOCK_ENTITIES_MAPPER.apply(ResourceUtils.loc("mana_relay/enchanted_frame"));
    public static final SpriteId FORBIDDEN_FRAME = Sheets.BLOCK_ENTITIES_MAPPER.apply(ResourceUtils.loc("mana_relay/forbidden_frame"));
    public static final SpriteId HEAVENLY_FRAME = Sheets.BLOCK_ENTITIES_MAPPER.apply(ResourceUtils.loc("mana_relay/heavenly_frame"));
    public static final SpriteId CORE = Sheets.BLOCK_ENTITIES_MAPPER.apply(ResourceUtils.loc("mana_cube"));

    protected final ManaRelayFrameModel frameModel;
    protected final ManaCubeModel manaCubeModel;

    private final BlockEntityRendererProvider.Context context;

    public ManaRelayTER(BlockEntityRendererProvider.Context context) {
        this.context = context;
        this.frameModel = new ManaRelayFrameModel(context.bakeLayer(ModelLayersPM.MANA_RELAY_FRAME));
        this.manaCubeModel = new ManaCubeModel(context.bakeLayer(ModelLayersPM.MANA_CUBE));
    }

    @Override
    @NotNull
    public ManaRelayTER.RenderState createRenderState() {
        return new ManaRelayTER.RenderState();
    }

    @Override
    public void extractRenderState(@NotNull ManaRelayTileEntity blockEntity, @NotNull ManaRelayTER.RenderState state,
                                   float partialTicks, @NotNull Vec3 cameraPosition, @Nullable ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        long time = blockEntity.getLevel() == null ? 0 : blockEntity.getLevel().getLevelData().getGameTime();
        state.tier = blockEntity.getDeviceTier();
        state.bobDelta = 0.125D * Math.sin((time + (double)partialTicks) * (2D * Math.PI / (double)ManaRelayTileEntity.BOB_CYCLE_TIME_TICKS));
        state.coreRotation = 2F * ((time % 360) + partialTicks);
        state.coreColor = blockEntity.getCurrentColor(partialTicks);
    }

    @Override
    public void submit(@NotNull ManaRelayTER.RenderState renderState, @NotNull PoseStack poseStack,
                       @NotNull SubmitNodeCollector submitNodeCollector, @NotNull CameraRenderState cameraRenderState) {
        poseStack.pushPose();
        poseStack.translate(0D, renderState.bobDelta, 0D);

        // Render the relay's frame
        final float baseScale = 0.5F;
        final float tilt = 45.0F;
        poseStack.pushPose();
        poseStack.translate(0.5D, 0.5D, 0.5D);
        poseStack.mulPose(Axis.YP.rotationDegrees(-renderState.coreRotation));   // Spin the frame around its Y-axis
        poseStack.mulPose(Axis.ZP.rotationDegrees(tilt));   // Tilt the frame onto its diagonal
        poseStack.mulPose(Axis.XP.rotationDegrees(tilt));   // Tilt the frame onto its diagonal
        poseStack.scale(baseScale, baseScale, baseScale);
        submitNodeCollector.submitModelPart(this.frameModel.root(), poseStack, this.getFrameSprite(renderState.tier).renderType(RenderTypes::entitySolid),
                renderState.lightCoords, OverlayTexture.NO_OVERLAY, this.context.sprites().get(CORE));
        poseStack.popPose();

        // Render the relay's core
        final float coreScale = 0.375F;
        poseStack.pushPose();
        poseStack.translate(0.5D, 0.5D, 0.5D);
        poseStack.mulPose(Axis.YP.rotationDegrees(renderState.coreRotation));    // Spin the core around its Y-axis
        poseStack.mulPose(Axis.ZP.rotationDegrees(tilt));   // Tilt the core onto its diagonal
        poseStack.mulPose(Axis.XP.rotationDegrees(tilt));   // Tilt the core onto its diagonal
        poseStack.scale(baseScale, baseScale, baseScale);
        poseStack.scale(coreScale, coreScale, coreScale);
        submitNodeCollector.submitModelPart(this.manaCubeModel.root(), poseStack, CORE.renderType(RenderTypes::entityTranslucent),
                renderState.lightCoords, OverlayTexture.NO_OVERLAY, this.context.sprites().get(CORE), renderState.coreColor,
                renderState.breakProgress);
        poseStack.popPose();

        poseStack.popPose();

        // Draw a particle stream rising from the core
        FxDispatcher.INSTANCE.spellcraftingGlow(renderState.blockPos, 0.5D + renderState.bobDelta, renderState.coreColor);
    }

    protected SpriteId getFrameSprite(@Nullable DeviceTier tier) {
        if (tier == null) {
            return BASIC_FRAME;
        }
        return switch (tier) {
            case BASIC -> BASIC_FRAME;
            case ENCHANTED -> ENCHANTED_FRAME;
            case FORBIDDEN -> FORBIDDEN_FRAME;
            case HEAVENLY, CREATIVE -> HEAVENLY_FRAME;
        };
    }

    public static class RenderState extends BlockEntityRenderState {
        @Nullable
        public DeviceTier tier;
        public double bobDelta;
        public float coreRotation;
        public int coreColor;
    }
}
