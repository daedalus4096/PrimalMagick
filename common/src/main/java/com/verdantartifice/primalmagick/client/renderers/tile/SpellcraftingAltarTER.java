package com.verdantartifice.primalmagick.client.renderers.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.verdantartifice.primalmagick.client.fx.FxDispatcher;
import com.verdantartifice.primalmagick.client.renderers.models.ModelLayersPM;
import com.verdantartifice.primalmagick.client.renderers.tile.model.SpellcraftingAltarRingModel;
import com.verdantartifice.primalmagick.common.blocks.crafting.SpellcraftingAltarBlock;
import com.verdantartifice.primalmagick.common.tiles.crafting.SpellcraftingAltarTileEntity;
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
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.util.ARGB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Custom tile entity renderer for spellcrafting altar blocks.
 * 
 * @author Daedalus4096
 */
public class SpellcraftingAltarTER implements BlockEntityRenderer<SpellcraftingAltarTileEntity, SpellcraftingAltarTER.RenderState> {
    private static final SpriteId RING_TEXTURE = Sheets.BLOCK_ENTITIES_MAPPER.apply(ResourceUtils.loc("spellcrafting_altar/spellcrafting_altar_ring"));

    protected final SpellcraftingAltarRingModel ringModel;
    private final BlockEntityRendererProvider.Context context;

    public SpellcraftingAltarTER(BlockEntityRendererProvider.Context context) {
        this.context = context;
        this.ringModel = new SpellcraftingAltarRingModel(context.bakeLayer(ModelLayersPM.SPELLCRAFTING_ALTAR_RING));
    }

    protected void addVertex(VertexConsumer renderer, PoseStack.Pose stack, float x, float y, float z, float r, float g, float b, float u, float v) {
        renderer.addVertex(stack, x, y, z)
                .setColor(r, g, b, 1.0F)
                .setUv(u, v)
                .setUv2(240, 240)
                .setNormal(1, 0, 0);
    }

    @Override
    @NotNull
    public SpellcraftingAltarTER.RenderState createRenderState() {
        return new SpellcraftingAltarTER.RenderState();
    }

    @Override
    public void extractRenderState(@NotNull SpellcraftingAltarTileEntity blockEntity, @NotNull SpellcraftingAltarTER.RenderState state,
                                   float partialTicks, @NotNull Vec3 cameraPosition, @Nullable ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        long time = blockEntity.getLevel().getLevelData().getGameTime();
        state.coreColor = blockEntity.getCurrentColor(partialTicks);
        state.coreRotation = (time % 360) + partialTicks;
        state.ringRotation = blockEntity.getCurrentRotation(partialTicks);
        state.bobDelta = 0.125D * Math.sin((time + (double)partialTicks) * (2D * Math.PI / (double)SpellcraftingAltarTileEntity.BOB_CYCLE_TIME_TICKS));
        state.facingAngle = blockEntity.getBlockState().getValue(SpellcraftingAltarBlock.FACING).getClockWise().toYRot();
    }

    @Override
    public void submit(@NotNull SpellcraftingAltarTER.RenderState renderState, @NotNull PoseStack poseStack,
                       @NotNull SubmitNodeCollector submitNodeCollector, @NotNull CameraRenderState cameraRenderState) {
        // Render the altar's ring
        poseStack.pushPose();
        poseStack.translate(0.5D, 0D, 0.5D);
        poseStack.translate(0D, 2.5D, 0D);    // Model position correction
        poseStack.translate(0D, renderState.bobDelta, 0D);    // Bob the ring up and down
        poseStack.mulPose(Axis.ZP.rotationDegrees(180F));
        poseStack.mulPose(Axis.YP.rotationDegrees(-renderState.facingAngle));
        poseStack.mulPose(Axis.YP.rotationDegrees(90F));  // Model rotation correction
        poseStack.mulPose(Axis.YP.rotationDegrees(renderState.ringRotation)); // Spin the ring according to tile control
        submitNodeCollector.submitModelPart(this.ringModel.root(), poseStack, RING_TEXTURE.renderType(RenderTypes::entitySolid),
                renderState.lightCoords, OverlayTexture.NO_OVERLAY, this.context.sprites().get(RING_TEXTURE));
        poseStack.popPose();

        // Render the altar's core
        final float r = ARGB.redFloat(renderState.coreColor);
        final float g = ARGB.greenFloat(renderState.coreColor);
        final float b = ARGB.blueFloat(renderState.coreColor);
        final float ds = 0.1875F;
        final float scale = 0.5F;
        final TextureAtlasSprite sprite = this.context.sprites().get(ManaFontTER.CORE_SPRITE);

        poseStack.pushPose();
        poseStack.translate(0.5D, 1.125D, 0.5D);
        poseStack.mulPose(Axis.YP.rotationDegrees(renderState.coreRotation)); // Spin the core around its Y-axis
        poseStack.mulPose(Axis.ZP.rotationDegrees(45.0F)); // Tilt the core onto its diagonal
        poseStack.mulPose(Axis.XP.rotationDegrees(45.0F)); // Tilt the core onto its diagonal
        poseStack.scale(scale, scale, scale);

        // TODO Abstract into a model instead of plotting individual vertices
        submitNodeCollector.submitCustomGeometry(poseStack, Sheets.cutoutBlockSheet(), (pose, consumer) -> {
            // Draw the south face of the core
            addVertex(consumer, pose, -ds, ds, ds, r, g, b, sprite.getU0(), sprite.getV1());
            addVertex(consumer, pose, -ds, -ds, ds, r, g, b, sprite.getU0(), sprite.getV0());
            addVertex(consumer, pose, ds, -ds, ds, r, g, b, sprite.getU1(), sprite.getV0());
            addVertex(consumer, pose, ds, ds, ds, r, g, b, sprite.getU1(), sprite.getV1());

            // Draw the north face of the core
            addVertex(consumer, pose, -ds, ds, -ds, r, g, b, sprite.getU0(), sprite.getV1());
            addVertex(consumer, pose, ds, ds, -ds, r, g, b, sprite.getU1(), sprite.getV1());
            addVertex(consumer, pose, ds, -ds, -ds, r, g, b, sprite.getU1(), sprite.getV0());
            addVertex(consumer, pose, -ds, -ds, -ds, r, g, b, sprite.getU0(), sprite.getV0());

            // Draw the east face of the core
            addVertex(consumer, pose, ds, ds, -ds, r, g, b, sprite.getU0(), sprite.getV1());
            addVertex(consumer, pose, ds, ds, ds, r, g, b, sprite.getU1(), sprite.getV1());
            addVertex(consumer, pose, ds, -ds, ds, r, g, b, sprite.getU1(), sprite.getV0());
            addVertex(consumer, pose, ds, -ds, -ds, r, g, b, sprite.getU0(), sprite.getV0());

            // Draw the west face of the core
            addVertex(consumer, pose, -ds, -ds, ds, r, g, b, sprite.getU1(), sprite.getV0());
            addVertex(consumer, pose, -ds, ds, ds, r, g, b, sprite.getU1(), sprite.getV1());
            addVertex(consumer, pose, -ds, ds, -ds, r, g, b, sprite.getU0(), sprite.getV1());
            addVertex(consumer, pose, -ds, -ds, -ds, r, g, b, sprite.getU0(), sprite.getV0());

            // Draw the top face of the core
            addVertex(consumer, pose, ds, ds, -ds, r, g, b, sprite.getU1(), sprite.getV0());
            addVertex(consumer, pose, -ds, ds, -ds, r, g, b, sprite.getU0(), sprite.getV0());
            addVertex(consumer, pose, -ds, ds, ds, r, g, b, sprite.getU0(), sprite.getV1());
            addVertex(consumer, pose, ds, ds, ds, r, g, b, sprite.getU1(), sprite.getV1());

            // Draw the bottom face of the core
            addVertex(consumer, pose, ds, -ds, -ds, r, g, b, sprite.getU1(), sprite.getV0());
            addVertex(consumer, pose, ds, -ds, ds, r, g, b, sprite.getU1(), sprite.getV1());
            addVertex(consumer, pose, -ds, -ds, ds, r, g, b, sprite.getU0(), sprite.getV1());
            addVertex(consumer, pose, -ds, -ds, -ds, r, g, b, sprite.getU0(), sprite.getV0());
        });
        poseStack.popPose();

        // Draw a particle stream rising from the core
        FxDispatcher.INSTANCE.spellcraftingGlow(renderState.blockPos, 1.125D, r, g, b);
    }

    public static class RenderState extends BlockEntityRenderState {
        public int coreColor;
        public float coreRotation;
        public float ringRotation;
        public double bobDelta;
        public float facingAngle;
    }
}
