package com.verdantartifice.primalmagic.client.renderers.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.verdantartifice.primalmagic.common.blocks.rituals.RitualLecternBlock;
import com.verdantartifice.primalmagic.common.tiles.rituals.RitualLecternTileEntity;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.model.BookModel;
import net.minecraft.client.renderer.tileentity.EnchantmentTableTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;

/**
 * Custom tile entity renderer for ritual lectern blocks.
 * 
 * @author Daedalus4096
 * @see {@link com.verdantartifice.primalmagic.common.blocks.rituals.RitualLecternBlock}
 */
public class RitualLecternTER extends TileEntityRenderer<RitualLecternTileEntity> {
    protected final BookModel bookModel = new BookModel();

    public RitualLecternTER(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(RitualLecternTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        BlockState state = tileEntityIn.getBlockState();
        if (state.get(RitualLecternBlock.HAS_BOOK)) {
            matrixStackIn.push();
            matrixStackIn.translate(0.5D, 1.0625D, 0.5D);
            float f = state.get(RitualLecternBlock.FACING).rotateY().getHorizontalAngle();
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(-f));
            matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(67.5F));
            matrixStackIn.translate(0.0D, -0.125D, 0.0D);
            this.bookModel.func_228247_a_(0.0F, 0.1F, 0.9F, 1.2F);
            IVertexBuilder ivertexbuilder = EnchantmentTableTileEntityRenderer.TEXTURE_BOOK.getBuffer(bufferIn, RenderType::entitySolid);
            this.bookModel.func_228249_b_(matrixStackIn, ivertexbuilder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
            matrixStackIn.pop();
        }
    }
}
