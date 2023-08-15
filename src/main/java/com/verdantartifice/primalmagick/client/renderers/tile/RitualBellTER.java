package com.verdantartifice.primalmagick.client.renderers.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.tiles.rituals.RitualBellTileEntity;

import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

/**
 * Custom tile entity renderer for ritual bell blocks.
 * 
 * @author Daedalus4096
 * @see {@link com.verdantartifice.primalmagick.common.blocks.rituals.RitualBellBlock}
 */
@SuppressWarnings("deprecation")
public class RitualBellTER implements BlockEntityRenderer<RitualBellTileEntity> {
    public static final ResourceLocation TEXTURE = PrimalMagick.resource("entity/ritual_bell_body");
    public static final Material BODY_MATERIAL = new Material(TextureAtlas.LOCATION_BLOCKS, TEXTURE);
    protected final ModelPart modelRenderer;

    public RitualBellTER(BlockEntityRendererProvider.Context context) {
        this.modelRenderer = context.bakeLayer(ModelLayers.BELL).getChild("bell_body");
    }

    @Override
    public void render(RitualBellTileEntity tileEntityIn, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        float ticks = (float)tileEntityIn.getRingingTicks() + partialTicks;
        this.modelRenderer.xRot = 0.0F;
        this.modelRenderer.zRot = 0.0F;
        
        if (tileEntityIn.isRinging()) {
            float delta = Mth.sin(ticks / (float)Math.PI) / (4.0F + ticks / 3.0F);
            if (tileEntityIn.getRingDirection() == Direction.NORTH) {
                this.modelRenderer.xRot = -delta;
            } else if (tileEntityIn.getRingDirection() == Direction.SOUTH) {
                this.modelRenderer.xRot = delta;
            } else if (tileEntityIn.getRingDirection() == Direction.EAST) {
                this.modelRenderer.zRot = -delta;
            } else if (tileEntityIn.getRingDirection() == Direction.WEST) {
                this.modelRenderer.zRot = delta;
            }
        }
        VertexConsumer ivertexbuilder = BODY_MATERIAL.buffer(bufferIn, RenderType::entitySolid);
        this.modelRenderer.render(matrixStackIn, ivertexbuilder, combinedLightIn, combinedOverlayIn);
    }
}
