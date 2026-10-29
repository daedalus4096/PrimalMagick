package com.verdantartifice.primalmagick.client.renderers.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.verdantartifice.primalmagick.client.fx.FxDispatcher;
import com.verdantartifice.primalmagick.client.renderers.itemstack.SubModelRenderHelper;
import com.verdantartifice.primalmagick.common.tiles.rituals.RitualAltarTileEntity;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Custom tile entity renderer for ritual altar tile entities.
 * 
 * @author Daedalus4096
 * @see com.verdantartifice.primalmagick.common.tiles.rituals.RitualAltarTileEntity
 */
public class RitualAltarTER implements BlockEntityRenderer<RitualAltarTileEntity, RitualAltarTER.RenderState> {
    private final BlockEntityRendererProvider.Context context;

    public RitualAltarTER(BlockEntityRendererProvider.Context context) {
        this.context = context;
    }

    @Override
    @NotNull
    public RitualAltarTER.RenderState createRenderState() {
        return new RitualAltarTER.RenderState();
    }

    @Override
    public void extractRenderState(@NotNull RitualAltarTileEntity blockEntity, @NotNull RitualAltarTER.RenderState state,
                                   float partialTicks, @NotNull Vec3 cameraPosition, @Nullable ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        state.stack = blockEntity.getSyncedStack().copy();
        state.stackRotation = blockEntity.getLevel() == null ? 0 : (blockEntity.getLevel().getLevelData().getGameTime() % 360) + partialTicks;
        state.orbActive = blockEntity.isActive();
        state.orbColor = blockEntity.getOrbColor();
        float ticks = (float)blockEntity.getActiveCount() + partialTicks;
        state.orbRotationX = Mth.cos(ticks * 0.1F) * 180.0F;
        state.orbRotationY = Mth.sin(ticks * 0.1F) * 180.0F;
        state.orbRotationZ = Mth.sin(ticks * 0.15F) * 360.0F;
    }

    @Override
    public void submit(@NotNull RitualAltarTER.RenderState renderState, @NotNull PoseStack poseStack,
                       @NotNull SubmitNodeCollector submitNodeCollector, @NotNull CameraRenderState cameraRenderState) {
        // Render the held item stack above the altar, if present
        if (!renderState.stack.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(0.5D, 1.5D, 0.5D);
            poseStack.mulPose(Axis.YP.rotationDegrees(renderState.stackRotation));   // Spin the stack around its Y-axis
            poseStack.scale(0.75F, 0.75F, 0.75F);
            SubModelRenderHelper.submitItemModel(
                    Services.ITEMS_REGISTRY.getKey(renderState.stack.getItem()),
                    renderState.stack,
                    poseStack,
                    submitNodeCollector,
                    renderState.lightCoords,
                    OverlayTexture.NO_OVERLAY,
                    renderState.stack.hasFoil(),
                    0
            );
            poseStack.popPose();
        }

        // Render the ritual orb above the altar, if active
        if (renderState.orbActive) {
            final float r = ARGB.redFloat(renderState.orbColor);
            final float g = ARGB.greenFloat(renderState.orbColor);
            final float b = ARGB.blueFloat(renderState.orbColor);
            final float ds = 0.1875F;
            final TextureAtlasSprite sprite = this.context.sprites().get(ManaFontTER.CORE_SPRITE);

            poseStack.pushPose();
            poseStack.translate(0.5D, 2.5D, 0.5D);
            poseStack.mulPose(Axis.YP.rotationDegrees(renderState.orbRotationY)); // Spin the orb like a shulker bullet
            poseStack.mulPose(Axis.XP.rotationDegrees(renderState.orbRotationX));
            poseStack.mulPose(Axis.ZP.rotationDegrees(renderState.orbRotationZ));
            // FIXME Change to translucentBlockSheet once Fabulous graphics bug is fixed
            submitNodeCollector.submitCustomGeometry(poseStack, Sheets.cutoutBlockSheet(), (pose, consumer) -> {
                this.renderCube(consumer, pose, ds, r, g, b, 1.0F, sprite);
            });

            // FIXME Uncomment once Fabulous graphics bug is fixed
//            poseStack.scale(1.5F, 1.5F, 1.5F);
//            submitNodeCollector.submitCustomGeometry(poseStack, Sheets.cutoutBlockSheet(), (pose, consumer) -> {
//                this.renderCube(consumer, pose, ds, r, g, b, 0.5F, sprite);
//            });

            poseStack.popPose();

            FxDispatcher.INSTANCE.ritualGlow(renderState.blockPos, renderState.orbColor);
        }
    }

    protected void addVertex(VertexConsumer renderer, PoseStack.Pose pose, float x, float y, float z, float r, float g, float b, float a, float u, float v) {
        renderer.addVertex(pose, x, y, z)
                .setColor(r, g, b, a)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setUv(u, v)
                .setUv2(240, 240)
                .setNormal(1, 0, 0);
    }
    
    protected void renderCube(VertexConsumer builder, PoseStack.Pose pose, float ds, float r, float g, float b, float a, TextureAtlasSprite sprite) {
        // Draw the south face of the cube
        this.addVertex(builder, pose, -ds, ds, ds, r, g, b, a, sprite.getU0(), sprite.getV1());
        this.addVertex(builder, pose, -ds, -ds, ds, r, g, b, a, sprite.getU0(), sprite.getV0());
        this.addVertex(builder, pose, ds, -ds, ds, r, g, b, a, sprite.getU1(), sprite.getV0());
        this.addVertex(builder, pose, ds, ds, ds, r, g, b, a, sprite.getU1(), sprite.getV1());
        
        // Draw the north face of the cube
        this.addVertex(builder, pose, -ds, ds, -ds, r, g, b, a, sprite.getU0(), sprite.getV1());
        this.addVertex(builder, pose, ds, ds, -ds, r, g, b, a, sprite.getU1(), sprite.getV1());
        this.addVertex(builder, pose, ds, -ds, -ds, r, g, b, a, sprite.getU1(), sprite.getV0());
        this.addVertex(builder, pose, -ds, -ds, -ds, r, g, b, a, sprite.getU0(), sprite.getV0());
        
        // Draw the east face of the cube
        this.addVertex(builder, pose, ds, ds, -ds, r, g, b, a, sprite.getU0(), sprite.getV1());
        this.addVertex(builder, pose, ds, ds, ds, r, g, b, a, sprite.getU1(), sprite.getV1());
        this.addVertex(builder, pose, ds, -ds, ds, r, g, b, a, sprite.getU1(), sprite.getV0());
        this.addVertex(builder, pose, ds, -ds, -ds, r, g, b, a, sprite.getU0(), sprite.getV0());
        
        // Draw the west face of the cube
        this.addVertex(builder, pose, -ds, -ds, ds, r, g, b, a, sprite.getU1(), sprite.getV0());
        this.addVertex(builder, pose, -ds, ds, ds, r, g, b, a, sprite.getU1(), sprite.getV1());
        this.addVertex(builder, pose, -ds, ds, -ds, r, g, b, a, sprite.getU0(), sprite.getV1());
        this.addVertex(builder, pose, -ds, -ds, -ds, r, g, b, a, sprite.getU0(), sprite.getV0());
        
        // Draw the top face of the cube
        this.addVertex(builder, pose, ds, ds, -ds, r, g, b, a, sprite.getU1(), sprite.getV0());
        this.addVertex(builder, pose, -ds, ds, -ds, r, g, b, a, sprite.getU0(), sprite.getV0());
        this.addVertex(builder, pose, -ds, ds, ds, r, g, b, a, sprite.getU0(), sprite.getV1());
        this.addVertex(builder, pose, ds, ds, ds, r, g, b, a, sprite.getU1(), sprite.getV1());
        
        // Draw the bottom face of the cube
        this.addVertex(builder, pose, ds, -ds, -ds, r, g, b, a, sprite.getU1(), sprite.getV0());
        this.addVertex(builder, pose, ds, -ds, ds, r, g, b, a, sprite.getU1(), sprite.getV1());
        this.addVertex(builder, pose, -ds, -ds, ds, r, g, b, a, sprite.getU0(), sprite.getV1());
        this.addVertex(builder, pose, -ds, -ds, -ds, r, g, b, a, sprite.getU0(), sprite.getV0());
    }
    
    public static class RenderState extends BlockEntityRenderState {
        @NotNull
        public ItemStack stack = ItemStack.EMPTY;
        public float stackRotation;
        public boolean orbActive;
        public int orbColor;
        public float orbRotationX;
        public float orbRotationY;
        public float orbRotationZ;
    }
}
