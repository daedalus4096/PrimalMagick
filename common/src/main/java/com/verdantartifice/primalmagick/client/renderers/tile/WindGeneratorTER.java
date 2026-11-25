package com.verdantartifice.primalmagick.client.renderers.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.verdantartifice.primalmagick.client.renderers.models.ModelLayersPM;
import com.verdantartifice.primalmagick.client.renderers.tile.model.ManaCubeModel;
import com.verdantartifice.primalmagick.common.blocks.devices.AbstractWindGeneratorBlock;
import com.verdantartifice.primalmagick.common.tiles.devices.WindGeneratorTileEntity;
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
    public static final SpriteId CORE = Sheets.BLOCK_ENTITIES_MAPPER.apply(ResourceUtils.loc("mana_cube"));

    protected final ManaCubeModel manaCubeModel;
    private final BlockEntityRendererProvider.Context context;

    public WindGeneratorTER(BlockEntityRendererProvider.Context context) {
        this.context = context;
        this.manaCubeModel = new ManaCubeModel(context.bakeLayer(ModelLayersPM.MANA_CUBE));
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
        state.coreRotation = blockEntity.getLevel() == null ? 0 : (blockEntity.getLevel().getLevelData().getGameTime() % 360) + partialTicks;
        if (blockEntity.getBlockState().getValue(AbstractWindGeneratorBlock.POWERED)) {
            state.coreRotation *= 30F;
        }
        state.facing = blockEntity.getBlockState().getValue(AbstractWindGeneratorBlock.FACING);
    }

    @Override
    public void submit(@NotNull WindGeneratorTER.RenderState renderState, @NotNull PoseStack poseStack,
                       @NotNull SubmitNodeCollector submitNodeCollector, @NotNull CameraRenderState cameraRenderState) {
        final float baseScale = 0.375F;
        final float coreScale = 0.9F;

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
        poseStack.scale(baseScale, baseScale, baseScale);
        poseStack.scale(coreScale, coreScale, coreScale);
        submitNodeCollector.submitModelPart(this.manaCubeModel.root(), poseStack, CORE.renderType(RenderTypes::entitySolid),
                renderState.lightCoords, OverlayTexture.NO_OVERLAY, this.context.sprites().get(CORE), ARGB.opaque(renderState.coreColor),
                renderState.breakProgress);
        poseStack.popPose();
    }

    public static class RenderState extends BlockEntityRenderState {
        public int coreColor;
        public float coreRotation;
        public Direction facing;
    }
}
