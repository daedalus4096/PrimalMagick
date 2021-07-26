package com.verdantartifice.primalmagic.client.renderers.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.tiles.rituals.RitualBellTileEntity;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Custom tile entity renderer for ritual bell blocks.
 * 
 * @author Daedalus4096
 * @see {@link com.verdantartifice.primalmagic.common.blocks.rituals.RitualBellBlock}
 */
@SuppressWarnings("deprecation")
@OnlyIn(Dist.CLIENT)
public class RitualBellTER implements BlockEntityRenderer<RitualBellTileEntity> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagic.MODID, "entity/ritual_bell_body");
    public static final Material BODY_MATERIAL = new Material(TextureAtlas.LOCATION_BLOCKS, TEXTURE);
    protected final ModelPart modelRenderer = new ModelPart(32, 32, 0, 0);

    public RitualBellTER(BlockEntityRendererProvider.Context context) {
        this.modelRenderer.addBox(-3.0F, -6.0F, -3.0F, 6.0F, 7.0F, 6.0F);
        this.modelRenderer.setPos(8.0F, 12.0F, 8.0F);
        ModelPart modelrenderer = new ModelPart(32, 32, 0, 13);
        modelrenderer.addBox(4.0F, 4.0F, 4.0F, 8.0F, 2.0F, 8.0F);
        modelrenderer.setPos(-8.0F, -12.0F, -8.0F);
        this.modelRenderer.addChild(modelrenderer);
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
