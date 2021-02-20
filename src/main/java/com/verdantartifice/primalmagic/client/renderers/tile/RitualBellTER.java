package com.verdantartifice.primalmagic.client.renderers.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.tiles.rituals.RitualBellTileEntity;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
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
public class RitualBellTER extends TileEntityRenderer<RitualBellTileEntity> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagic.MODID, "entity/ritual_bell_body");
    public static final RenderMaterial BODY_MATERIAL = new RenderMaterial(AtlasTexture.LOCATION_BLOCKS_TEXTURE, TEXTURE);
    protected final ModelRenderer modelRenderer = new ModelRenderer(32, 32, 0, 0);

    public RitualBellTER(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
        this.modelRenderer.addBox(-3.0F, -6.0F, -3.0F, 6.0F, 7.0F, 6.0F);
        this.modelRenderer.setRotationPoint(8.0F, 12.0F, 8.0F);
        ModelRenderer modelrenderer = new ModelRenderer(32, 32, 0, 13);
        modelrenderer.addBox(4.0F, 4.0F, 4.0F, 8.0F, 2.0F, 8.0F);
        modelrenderer.setRotationPoint(-8.0F, -12.0F, -8.0F);
        this.modelRenderer.addChild(modelrenderer);
    }

    @Override
    public void render(RitualBellTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        float ticks = (float)tileEntityIn.getRingingTicks() + partialTicks;
        this.modelRenderer.rotateAngleX = 0.0F;
        this.modelRenderer.rotateAngleZ = 0.0F;
        
        if (tileEntityIn.isRinging()) {
            float delta = MathHelper.sin(ticks / (float)Math.PI) / (4.0F + ticks / 3.0F);
            if (tileEntityIn.getRingDirection() == Direction.NORTH) {
                this.modelRenderer.rotateAngleX = -delta;
            } else if (tileEntityIn.getRingDirection() == Direction.SOUTH) {
                this.modelRenderer.rotateAngleX = delta;
            } else if (tileEntityIn.getRingDirection() == Direction.EAST) {
                this.modelRenderer.rotateAngleZ = -delta;
            } else if (tileEntityIn.getRingDirection() == Direction.WEST) {
                this.modelRenderer.rotateAngleZ = delta;
            }
        }
        IVertexBuilder ivertexbuilder = BODY_MATERIAL.getBuffer(bufferIn, RenderType::getEntitySolid);
        this.modelRenderer.render(matrixStackIn, ivertexbuilder, combinedLightIn, combinedOverlayIn);
    }
}
