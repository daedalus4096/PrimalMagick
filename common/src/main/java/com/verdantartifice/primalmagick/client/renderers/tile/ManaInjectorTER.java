package com.verdantartifice.primalmagick.client.renderers.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.verdantartifice.primalmagick.client.fx.FxDispatcher;
import com.verdantartifice.primalmagick.client.renderers.models.ModelLayersPM;
import com.verdantartifice.primalmagick.client.renderers.tile.model.ManaCubeModel;
import com.verdantartifice.primalmagick.client.renderers.tile.model.ManaInjectorFrameRingBottomMiddleModel;
import com.verdantartifice.primalmagick.client.renderers.tile.model.ManaInjectorFrameRingBottomModel;
import com.verdantartifice.primalmagick.client.renderers.tile.model.ManaInjectorFrameRingTopMiddleModel;
import com.verdantartifice.primalmagick.client.renderers.tile.model.ManaInjectorFrameRingTopModel;
import com.verdantartifice.primalmagick.common.misc.DeviceTier;
import com.verdantartifice.primalmagick.common.tiles.mana.ManaInjectorTileEntity;
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
import org.joml.Math;

/**
 * Custom tile entity renderer for mana injector blocks.
 *
 * @author Daedalus4096
 */
public class ManaInjectorTER implements BlockEntityRenderer<ManaInjectorTileEntity, ManaInjectorTER.RenderState> {
    public static final SpriteId BASIC_FRAME_TOP = Sheets.BLOCK_ENTITIES_MAPPER.apply(ResourceUtils.loc("mana_injector/basic_frame_top"));
    public static final SpriteId ENCHANTED_FRAME_TOP = Sheets.BLOCK_ENTITIES_MAPPER.apply(ResourceUtils.loc("mana_injector/enchanted_frame_top"));
    public static final SpriteId FORBIDDEN_FRAME_TOP = Sheets.BLOCK_ENTITIES_MAPPER.apply(ResourceUtils.loc("mana_injector/forbidden_frame_top"));
    public static final SpriteId HEAVENLY_FRAME_TOP = Sheets.BLOCK_ENTITIES_MAPPER.apply(ResourceUtils.loc("mana_injector/heavenly_frame_top"));
    public static final SpriteId FRAME_BOTTOM = Sheets.BLOCK_ENTITIES_MAPPER.apply(ResourceUtils.loc("mana_injector/frame_bottom"));
    public static final SpriteId CORE = Sheets.BLOCK_ENTITIES_MAPPER.apply(ResourceUtils.loc("mana_cube"));

    protected final ManaInjectorFrameRingTopModel ringTopModel;
    protected final ManaInjectorFrameRingTopMiddleModel ringTopMiddleModel;
    protected final ManaInjectorFrameRingBottomMiddleModel ringBottomMiddleModel;
    protected final ManaInjectorFrameRingBottomModel ringBottomModel;
    protected final ManaCubeModel manaCubeModel;

    private final BlockEntityRendererProvider.Context context;

    public ManaInjectorTER(BlockEntityRendererProvider.Context context) {
        this.context = context;
        this.ringTopModel = new ManaInjectorFrameRingTopModel(context.bakeLayer(ModelLayersPM.MANA_INJECTOR_FRAME_TOP));
        this.ringTopMiddleModel = new ManaInjectorFrameRingTopMiddleModel(context.bakeLayer(ModelLayersPM.MANA_INJECTOR_FRAME_TOP_MIDDLE));
        this.ringBottomMiddleModel = new ManaInjectorFrameRingBottomMiddleModel(context.bakeLayer(ModelLayersPM.MANA_INJECTOR_FRAME_BOTTOM_MIDDLE));
        this.ringBottomModel = new ManaInjectorFrameRingBottomModel(context.bakeLayer(ModelLayersPM.MANA_INJECTOR_FRAME_BOTTOM));
        this.manaCubeModel = new ManaCubeModel(context.bakeLayer(ModelLayersPM.MANA_CUBE));
    }

    protected static final int DIP_DURATION = 8;
    protected static final int CYCLE_DURATION = 40;

    @Override
    @NotNull
    public ManaInjectorTER.RenderState createRenderState() {
        return new ManaInjectorTER.RenderState();
    }

    @Override
    public void extractRenderState(@NotNull ManaInjectorTileEntity blockEntity, @NotNull ManaInjectorTER.RenderState state,
                                   float partialTicks, @NotNull Vec3 cameraPosition, @Nullable ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        long time = blockEntity.getLevel().getLevelData().getGameTime();
        state.tier = blockEntity.getDeviceTier();
        state.topDip = this.getDipAmount(time, partialTicks, 0);
        state.topMiddleDip = this.getDipAmount(time, partialTicks, 4);
        state.bottomMiddleDip = this.getDipAmount(time, partialTicks, 8);
        state.bottomDip = this.getDipAmount(time, partialTicks, 12);
        state.coreRotation = 2F * ((time % 360) + partialTicks);
        state.coreColor = blockEntity.getCurrentColor(partialTicks);
    }

    @Override
    public void submit(@NotNull ManaInjectorTER.RenderState renderState, @NotNull PoseStack poseStack,
                       @NotNull SubmitNodeCollector submitNodeCollector, @NotNull CameraRenderState cameraRenderState) {
        // Render the injector's frame rings
        poseStack.pushPose();
        poseStack.translate(0.5D, 0D, 0.5D);

        poseStack.pushPose();
        poseStack.translate(0D, 1.875D + renderState.topDip, 0D);
        SpriteId topSprite = this.getTopFrameSprite(renderState.tier);
        submitNodeCollector.submitModelPart(this.ringTopModel.root(), poseStack, topSprite.renderType(RenderTypes::entitySolid),
                renderState.lightCoords, OverlayTexture.NO_OVERLAY, this.context.sprites().get(topSprite));
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.translate(0D, 1.375D + renderState.topMiddleDip, 0D);
        submitNodeCollector.submitModelPart(this.ringTopMiddleModel.root(), poseStack, FRAME_BOTTOM.renderType(RenderTypes::entitySolid),
                renderState.lightCoords, OverlayTexture.NO_OVERLAY, this.context.sprites().get(FRAME_BOTTOM));
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.translate(0D, 0.875D + renderState.bottomMiddleDip, 0D);
        submitNodeCollector.submitModelPart(this.ringBottomMiddleModel.root(), poseStack, FRAME_BOTTOM.renderType(RenderTypes::entitySolid),
                renderState.lightCoords, OverlayTexture.NO_OVERLAY, this.context.sprites().get(FRAME_BOTTOM));
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.translate(0D, 0.375D + renderState.bottomDip, 0D);
        submitNodeCollector.submitModelPart(this.ringBottomModel.root(), poseStack, FRAME_BOTTOM.renderType(RenderTypes::entitySolid),
                renderState.lightCoords, OverlayTexture.NO_OVERLAY, this.context.sprites().get(FRAME_BOTTOM));
        poseStack.popPose();

        poseStack.popPose();

        // Render the relay's core
        final float tilt = 45.0F;
        final float coreScale = 0.1875F;
        poseStack.pushPose();
        poseStack.translate(0.5D, 0.75D, 0.5D);
        poseStack.mulPose(Axis.YP.rotationDegrees(renderState.coreRotation));    // Spin the core around its Y-axis
        poseStack.mulPose(Axis.ZP.rotationDegrees(tilt));   // Tilt the core onto its diagonal
        poseStack.mulPose(Axis.XP.rotationDegrees(tilt));   // Tilt the core onto its diagonal
        poseStack.scale(coreScale, coreScale, coreScale);
        submitNodeCollector.submitModelPart(this.manaCubeModel.root(), poseStack, CORE.renderType(RenderTypes::entityTranslucent),
                renderState.lightCoords, OverlayTexture.NO_OVERLAY, this.context.sprites().get(CORE), renderState.coreColor,
                renderState.breakProgress);
        poseStack.popPose();

        // Draw a particle stream rising from the core
        FxDispatcher.INSTANCE.spellcraftingGlow(renderState.blockPos, 0.75D, renderState.coreColor);
    }

    protected double getDipAmount(long gameTime, float partialTicks, int dipStartTime) {
        double cycleTime = (gameTime % CYCLE_DURATION) + (double)partialTicks;
        if (cycleTime >= dipStartTime && cycleTime <= (dipStartTime + DIP_DURATION)) {
            return -0.125D * Math.sin((cycleTime - dipStartTime) * (Math.PI / (double)DIP_DURATION));
        } else {
            return 0D;
        }
    }

    protected SpriteId getTopFrameSprite(@Nullable DeviceTier tier) {
        if (tier == null) {
            return BASIC_FRAME_TOP;
        }
        return switch (tier) {
            case BASIC -> BASIC_FRAME_TOP;
            case ENCHANTED -> ENCHANTED_FRAME_TOP;
            case FORBIDDEN -> FORBIDDEN_FRAME_TOP;
            case HEAVENLY, CREATIVE -> HEAVENLY_FRAME_TOP;
        };
    }

    public static class RenderState extends BlockEntityRenderState {
        @Nullable
        public DeviceTier tier;
        public double topDip;
        public double topMiddleDip;
        public double bottomMiddleDip;
        public double bottomDip;
        public float coreRotation;
        public int coreColor;
    }
}
