package com.verdantartifice.primalmagic.client.renderers.itemstack;

import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.datafixers.util.Pair;
import com.verdantartifice.primalmagic.common.items.tools.TieredShieldItem;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.model.ShieldModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.tileentity.BannerTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.tileentity.BannerTileEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Custom item stack renderer for magical metal shields.
 * 
 * @author Daedalus4096
 * @see {@link com.verdantartifice.primalmagic.common.items.tools.TieredShieldItem}
 */
@OnlyIn(Dist.CLIENT)
public abstract class AbstractTieredShieldISTER extends ItemStackTileEntityRenderer {
    protected final ShieldModel model = new ShieldModel();
    
    @Override
    public void func_239207_a_(ItemStack stack, TransformType p_239207_2_, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        if (stack.getItem() instanceof TieredShieldItem) {
            boolean hasPattern = stack.getChildTag("BlockEntityTag") != null;
            matrixStack.push();
            matrixStack.scale(1.0F, -1.0F, -1.0F);
            RenderMaterial renderMaterial = this.getRenderMaterial(hasPattern);
            IVertexBuilder vertexBuilder = renderMaterial.getSprite().wrapBuffer(ItemRenderer.getEntityGlintVertexBuilder(buffer, this.model.getRenderType(renderMaterial.getAtlasLocation()), true, stack.hasEffect()));
            this.model.func_228294_b_().render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
            if (hasPattern) {
                List<Pair<BannerPattern, DyeColor>> list = BannerTileEntity.getPatternColorData(ShieldItem.getColor(stack), BannerTileEntity.getPatternData(stack));
                BannerTileEntityRenderer.func_241717_a_(matrixStack, buffer, combinedLight, combinedOverlay, this.model.func_228293_a_(), renderMaterial, false, list, stack.hasEffect());
            } else {
                this.model.func_228293_a_().render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
            }
            matrixStack.pop();
        }
    }
    
    protected abstract RenderMaterial getRenderMaterial(boolean hasPattern);
}
