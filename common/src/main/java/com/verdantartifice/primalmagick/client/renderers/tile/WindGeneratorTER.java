package com.verdantartifice.primalmagick.client.renderers.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.verdantartifice.primalmagick.common.blocks.devices.AbstractWindGeneratorBlock;
import com.verdantartifice.primalmagick.common.tiles.devices.WindGeneratorTileEntity;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.util.ARGB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Custom tile entity renderer for zephyr engine and void turbine blocks.
 * 
 * @author Daedalus4096
 */
public class WindGeneratorTER implements BlockEntityRenderer<WindGeneratorTileEntity, WindGeneratorTER.RenderState> {
    private final BlockEntityRendererProvider.Context context;

    public WindGeneratorTER(BlockEntityRendererProvider.Context context) {
        this.context = context;
    }

    protected void addVertex(VertexConsumer renderer, PoseStack.Pose pose, float x, float y, float z, float r, float g, float b, float u, float v) {
        renderer.addVertex(pose, x, y, z)
                .setColor(r, g, b, 1.0F)
                .setUv(u, v)
                .setUv2(240, 240)
                .setNormal(1, 0, 0);
    }

    @Override
    @NotNull
    public WindGeneratorTER.RenderState createRenderState() {
        return new WindGeneratorTER.RenderState();
    }

    @Override
    public void extractRenderState(@NotNull WindGeneratorTileEntity blockEntity, @NotNull WindGeneratorTER.RenderState state,
                                   float partialTicks, @NotNull Vec3 cameraPosition, @Nullable ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        state.coreColor = blockEntity.getBlockState().getBlock() instanceof AbstractWindGeneratorBlock windBlock ? windBlock.getCoreColor() : 0;
        state.coreRotation = (blockEntity.getLevel().getLevelData().getGameTime() % 360) + partialTicks;
        if (blockEntity.getBlockState().getValue(AbstractWindGeneratorBlock.POWERED)) {
            state.coreRotation *= 30F;
        }
        state.facing = blockEntity.getBlockState().getValue(AbstractWindGeneratorBlock.FACING);
    }

    @Override
    public void submit(@NotNull WindGeneratorTER.RenderState renderState, @NotNull PoseStack poseStack,
                       @NotNull SubmitNodeCollector submitNodeCollector, @NotNull CameraRenderState cameraRenderState) {
        final float r = ARGB.redFloat(renderState.coreColor);
        final float g = ARGB.greenFloat(renderState.coreColor);
        final float b = ARGB.blueFloat(renderState.coreColor);
        final float ds = 0.1875F;
        final float scale = 0.9F;
        final TextureAtlasSprite sprite = this.context.sprites().get(ManaFontTER.CORE_SPRITE);

        poseStack.pushPose();
        poseStack.translate(0.5D, 0.5D, 0.5D);
        if (!renderState.facing.getAxis().equals(Direction.Axis.Y)) {
            poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
            if (renderState.facing.getAxis().equals(Direction.Axis.X)) {
                poseStack.mulPose(Axis.ZP.rotationDegrees(90.0F));
            }
        }
        poseStack.mulPose(Axis.YP.rotationDegrees(renderState.coreRotation));    // Spin the core around its Y-axis
        poseStack.mulPose(Axis.ZP.rotationDegrees(45.0F));  // Tilt the core onto its diagonal
        poseStack.mulPose(Axis.XP.rotationDegrees(45.0F));  // Tilt the core onto its diagonal
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
    }

    public static class RenderState extends BlockEntityRenderState {
        public int coreColor;
        public float coreRotation;
        public Direction facing;
    }
}
