package com.verdantartifice.primalmagick.client.renderers.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagick.common.tiles.rituals.RitualBellTileEntity;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.object.bell.BellModel;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Custom tile entity renderer for ritual bell blocks.
 * 
 * @author Daedalus4096
 * @see com.verdantartifice.primalmagick.common.blocks.rituals.RitualBellBlock
 */
public class RitualBellTER implements BlockEntityRenderer<RitualBellTileEntity, RitualBellTER.RenderState> {
    public static final SpriteId BELL_TEXTURE = Sheets.BLOCK_ENTITIES_MAPPER.apply(ResourceUtils.loc("ritual_bell_body"));
    private final SpriteGetter sprites;
    private final BellModel model;

    public RitualBellTER(BlockEntityRendererProvider.Context context) {
        this.sprites = context.sprites();
        this.model = new BellModel(context.bakeLayer(ModelLayers.BELL));
    }

    @Override
    @NotNull
    public RitualBellTER.RenderState createRenderState() {
        return new RitualBellTER.RenderState();
    }

    @Override
    public void extractRenderState(@NotNull RitualBellTileEntity blockEntity, @NotNull RitualBellTER.RenderState state,
                                   float partialTicks, @NotNull Vec3 cameraPosition, @Nullable ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        state.ticks = (float)blockEntity.getRingingTicks() + partialTicks;
        state.shakeDirection = blockEntity.isRinging() ? blockEntity.getRingDirection() : null;
    }

    @Override
    public void submit(@NotNull RitualBellTER.RenderState renderState, @NotNull PoseStack poseStack,
                       @NotNull SubmitNodeCollector submitNodeCollector, @NotNull CameraRenderState cameraRenderState) {
        BellModel.State modelState = new BellModel.State(renderState.ticks, renderState.shakeDirection);
        this.model.setupAnim(modelState);
        submitNodeCollector.submitModel(this.model, modelState, poseStack, renderState.lightCoords, OverlayTexture.NO_OVERLAY, -1, BELL_TEXTURE, this.sprites, 0, renderState.breakProgress);
    }

    public static class RenderState extends BlockEntityRenderState {
        @Nullable
        public Direction shakeDirection;
        public float ticks;
    }
}
