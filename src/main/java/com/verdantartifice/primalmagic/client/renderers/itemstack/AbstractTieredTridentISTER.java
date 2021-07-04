package com.verdantartifice.primalmagic.client.renderers.itemstack;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.verdantartifice.primalmagic.common.items.tools.AbstractTieredTridentItem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.model.TridentModel;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Custom item stack renderer for magical metal tridents.
 * 
 * @author Daedalus4096
 * @see {@link com.verdantartifice.primalmagic.common.items.tools.AbstractTieredTridentItem}
 */
@OnlyIn(Dist.CLIENT)
public abstract class AbstractTieredTridentISTER extends ItemStackTileEntityRenderer {
    protected final TridentModel model = new TridentModel();
    
    @Override
    public void func_239207_a_(ItemStack stack, ItemCameraTransforms.TransformType transformType, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        if (stack.getItem() instanceof AbstractTieredTridentItem) {
            Minecraft mc = Minecraft.getInstance();
            ItemRenderer itemRenderer = mc.getItemRenderer();
            
            boolean render2d = (transformType == ItemCameraTransforms.TransformType.GUI ||
                                transformType == ItemCameraTransforms.TransformType.GROUND ||
                                transformType == ItemCameraTransforms.TransformType.FIXED);
            if (render2d) {
                IBakedModel bakedModel = mc.getModelManager().getModel(this.getModelResourceLocation()).getBakedModel();
                matrixStack.push();
                itemRenderer.renderItem(stack, transformType, true, matrixStack, buffer, combinedLight, combinedOverlay, bakedModel);
                matrixStack.pop();
            } else {
                matrixStack.push();
                matrixStack.scale(1.0F, -1.0F, -1.0F);
                IVertexBuilder ivertexbuilder1 = ItemRenderer.getEntityGlintVertexBuilder(buffer, this.model.getRenderType(this.getTextureLocation()), false, stack.hasEffect());
                this.model.render(matrixStack, ivertexbuilder1, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
                matrixStack.pop();
            }
        }
    }
    
    public abstract ModelResourceLocation getModelResourceLocation();
    
    public abstract ResourceLocation getTextureLocation();
}
