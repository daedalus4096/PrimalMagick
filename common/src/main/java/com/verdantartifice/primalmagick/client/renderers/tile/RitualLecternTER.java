package com.verdantartifice.primalmagick.client.renderers.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.verdantartifice.primalmagick.common.tiles.rituals.RitualLecternTileEntity;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.object.book.BookModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.EnchantTableRenderer;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Custom tile entity renderer for ritual lectern blocks.
 * 
 * @author Daedalus4096
 * @see com.verdantartifice.primalmagick.common.blocks.rituals.RitualLecternBlock
 */
public class RitualLecternTER implements BlockEntityRenderer<RitualLecternTileEntity, RitualLecternTER.RenderState> {
    private final SpriteGetter sprites;
    private final BookModel bookModel;
    private static final BookModel.State BOOK_STATE = BookModel.State.forAnimation(0.0F, 0.1F, 0.9F, 1.2F);

    public RitualLecternTER(BlockEntityRendererProvider.Context context) {
        this.sprites = context.sprites();
        this.bookModel = new BookModel(context.bakeLayer(ModelLayers.BOOK));
    }

    @Override
    @NotNull
    public RitualLecternTER.RenderState createRenderState() {
        return new RitualLecternTER.RenderState();
    }

    @Override
    public void extractRenderState(@NotNull RitualLecternTileEntity blockEntity, @NotNull RitualLecternTER.RenderState state,
                                   float partialTicks, @NotNull Vec3 cameraPosition, @Nullable ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        state.hasBook = blockEntity.getBlockState().getValue(LecternBlock.HAS_BOOK);
        state.yRot = blockEntity.getBlockState().getValue(LecternBlock.FACING).getClockWise().toYRot();
    }

    @Override
    public void submit(@NotNull RitualLecternTER.RenderState renderState, @NotNull PoseStack poseStack,
                       @NotNull SubmitNodeCollector submitNodeCollector, @NotNull CameraRenderState cameraRenderState) {
        if (renderState.hasBook) {
            poseStack.pushPose();
            poseStack.translate(0.5F, 1.0625F, 0.5F);
            poseStack.mulPose(Axis.YP.rotationDegrees(-renderState.yRot));
            poseStack.mulPose(Axis.ZP.rotationDegrees(67.5F));
            poseStack.translate(0.0F, -0.125F, 0.0F);
            submitNodeCollector.submitModel(this.bookModel, BOOK_STATE, poseStack, renderState.lightCoords, OverlayTexture.NO_OVERLAY, -1, EnchantTableRenderer.BOOK_TEXTURE, this.sprites, 0, renderState.breakProgress);
            poseStack.popPose();
        }
    }

    public static class RenderState extends BlockEntityRenderState {
        public boolean hasBook;
        public float yRot;
    }
}
