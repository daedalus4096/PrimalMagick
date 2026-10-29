package com.verdantartifice.primalmagick.client.renderers.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.verdantartifice.primalmagick.client.fx.FxDispatcher;
import com.verdantartifice.primalmagick.common.sources.Sources;
import com.verdantartifice.primalmagick.common.tiles.devices.SanguineCrucibleTileEntity;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.Color;

/**
 * Custom tile entity renderer for sanguine crucible blocks.
 * 
 * @author Daedalus4096
 * @see com.verdantartifice.primalmagick.common.blocks.devices.SanguineCrucibleBlock
 */
public class SanguineCrucibleTER implements BlockEntityRenderer<SanguineCrucibleTileEntity, SanguineCrucibleTER.RenderState> {
    protected static final SpriteId WATER_TEXTURE = Sheets.BLOCKS_MAPPER.defaultNamespaceApply("water_still");
    protected static final Color COLOR = new Color(Sources.BLOOD.getColor()).brighter().brighter();
    protected static final float R = COLOR.getRed() / 255.0F;
    protected static final float G = COLOR.getGreen() / 255.0F;
    protected static final float B = COLOR.getBlue() / 255.0F;

    private final BlockEntityRendererProvider.Context context;

    public SanguineCrucibleTER(BlockEntityRendererProvider.Context context) {
        this.context = context;
    }

    @Override
    @NotNull
    public SanguineCrucibleTER.RenderState createRenderState() {
        return new SanguineCrucibleTER.RenderState();
    }

    @Override
    public void extractRenderState(@NotNull SanguineCrucibleTileEntity blockEntity, @NotNull SanguineCrucibleTER.RenderState state,
                                   float partialTicks, @NotNull Vec3 cameraPosition, @Nullable ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        state.fluidHeight = blockEntity.getFluidHeight();
        Level level = blockEntity.getLevel();
        if (level != null) {
            BlockPos pos = state.blockPos;
            RandomSource rand = level.getRandom();
            state.showBubble = blockEntity.showBubble(rand);
            if (state.showBubble) {
                state.bubblePosX = (double)pos.getX() + 0.2D + (rand.nextDouble() * 0.6D);
                state.bubblePosY = (double)pos.getY() + (double)blockEntity.getFluidHeight();
                state.bubblePosZ = (double)pos.getZ() + 0.2D + (rand.nextDouble() * 0.6D);
            }
            state.showSmoke = rand.nextDouble() < blockEntity.getSmokeChance();
            if (state.showSmoke) {
                state.smokePosX = (double)pos.getX() + 0.1D + (rand.nextDouble() * 0.8D);
                state.smokePosY = (double)pos.getY() + 1.0D;
                state.smokePosZ = (double)pos.getZ() + 0.1D + (rand.nextDouble() * 0.8D);
            }
        }
    }

    private void addVertex(VertexConsumer consumer, PoseStack.Pose pose, float x, float y, TextureAtlasSprite sprite) {
        consumer.addVertex(pose, x, y, 0.0F).setColor(R, G, B, 1.0F).setUv(sprite.getU0(), sprite.getV1()).setUv2(0, 240).setNormal(1, 0, 0);
    }

    @Override
    public void submit(@NotNull SanguineCrucibleTER.RenderState renderState, @NotNull PoseStack poseStack,
                       @NotNull SubmitNodeCollector submitNodeCollector, @NotNull CameraRenderState cameraRenderState) {
        // Render the fluid layer of the crucible
        poseStack.pushPose();
        poseStack.translate(0.0D, renderState.fluidHeight, 0.0D);
        poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
        submitNodeCollector.submitCustomGeometry(poseStack, Sheets.cutoutBlockSheet(), (pose, vertexConsumer) -> {
            final TextureAtlasSprite sprite = this.context.sprites().get(WATER_TEXTURE);
            this.addVertex(vertexConsumer, pose, 0F, 1F, sprite);
            this.addVertex(vertexConsumer, pose, 1F, 1F, sprite);
            this.addVertex(vertexConsumer, pose, 0F, 0F, sprite);
            this.addVertex(vertexConsumer, pose, 0F, 0F, sprite);
        });
        poseStack.popPose();

        // Render a bubble particle, if called for
        if (renderState.showBubble) {
            FxDispatcher.INSTANCE.crucibleBubble(renderState.bubblePosX, renderState.bubblePosY, renderState.bubblePosZ, R, G, B);
        }

        // Render a smoke particle, if called for
        if (renderState.showSmoke && renderState.level != null) {
            renderState.level.addParticle(ParticleTypes.SMOKE, renderState.smokePosX, renderState.smokePosY, renderState.smokePosZ, 0.0D, 0.1D, 0.0D);
        }
    }

    public static class RenderState extends BlockEntityRenderState {
        @Nullable
        public Level level;
        public float fluidHeight;
        public boolean showBubble;
        public double bubblePosX;
        public double bubblePosY;
        public double bubblePosZ;
        public boolean showSmoke;
        public double smokePosX;
        public double smokePosY;
        public double smokePosZ;
    }
}
