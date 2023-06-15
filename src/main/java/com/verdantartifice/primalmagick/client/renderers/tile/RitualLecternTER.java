package com.verdantartifice.primalmagick.client.renderers.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.verdantartifice.primalmagick.common.blocks.rituals.RitualLecternBlock;
import com.verdantartifice.primalmagick.common.tiles.rituals.RitualLecternTileEntity;

import net.minecraft.client.model.BookModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.EnchantTableRenderer;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Custom tile entity renderer for ritual lectern blocks.
 * 
 * @author Daedalus4096
 * @see {@link com.verdantartifice.primalmagick.common.blocks.rituals.RitualLecternBlock}
 */
public class RitualLecternTER implements BlockEntityRenderer<RitualLecternTileEntity> {
    protected final BookModel bookModel;

    public RitualLecternTER(BlockEntityRendererProvider.Context context) {
        this.bookModel = new BookModel(context.bakeLayer(ModelLayers.BOOK));
    }

    @Override
    public void render(RitualLecternTileEntity tileEntityIn, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        BlockState state = tileEntityIn.getBlockState();
        if (state.getValue(RitualLecternBlock.HAS_BOOK)) {
            matrixStackIn.pushPose();
            matrixStackIn.translate(0.5D, 1.0625D, 0.5D);
            float f = state.getValue(RitualLecternBlock.FACING).getClockWise().toYRot();
            matrixStackIn.mulPose(Axis.YP.rotationDegrees(-f));
            matrixStackIn.mulPose(Axis.ZP.rotationDegrees(67.5F));
            matrixStackIn.translate(0.0D, -0.125D, 0.0D);
            this.bookModel.setupAnim(0.0F, 0.1F, 0.9F, 1.2F);
            VertexConsumer ivertexbuilder = EnchantTableRenderer.BOOK_LOCATION.buffer(bufferIn, RenderType::entitySolid);
            this.bookModel.render(matrixStackIn, ivertexbuilder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
            matrixStackIn.popPose();
        }
    }
}
