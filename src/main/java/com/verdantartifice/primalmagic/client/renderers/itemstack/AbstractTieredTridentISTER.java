package com.verdantartifice.primalmagic.client.renderers.itemstack;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.verdantartifice.primalmagic.common.items.tools.AbstractTieredTridentItem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.model.TridentModel;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Custom item stack renderer for magical metal tridents.
 * 
 * @author Daedalus4096
 * @see {@link com.verdantartifice.primalmagic.common.items.tools.AbstractTieredTridentItem}
 */
@OnlyIn(Dist.CLIENT)
public abstract class AbstractTieredTridentISTER extends BlockEntityWithoutLevelRenderer {
    protected final TridentModel model = new TridentModel();
    
    public AbstractTieredTridentISTER() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }
    
    @Override
    public void renderByItem(ItemStack stack, ItemTransforms.TransformType transformType, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        if (stack.getItem() instanceof AbstractTieredTridentItem) {
            Minecraft mc = Minecraft.getInstance();
            ItemRenderer itemRenderer = mc.getItemRenderer();
            
            boolean render2d = (transformType == ItemTransforms.TransformType.GUI ||
                                transformType == ItemTransforms.TransformType.GROUND ||
                                transformType == ItemTransforms.TransformType.FIXED);
            if (render2d) {
                BakedModel bakedModel = mc.getModelManager().getModel(this.getModelResourceLocation()).getBakedModel();
                matrixStack.pushPose();
                itemRenderer.render(stack, transformType, true, matrixStack, buffer, combinedLight, combinedOverlay, bakedModel);
                matrixStack.popPose();
            } else {
                matrixStack.pushPose();
                matrixStack.scale(1.0F, -1.0F, -1.0F);
                VertexConsumer ivertexbuilder1 = ItemRenderer.getFoilBufferDirect(buffer, this.model.renderType(this.getTextureLocation()), false, stack.hasFoil());
                this.model.renderToBuffer(matrixStack, ivertexbuilder1, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
                matrixStack.popPose();
            }
        }
    }
    
    public abstract ModelResourceLocation getModelResourceLocation();
    
    public abstract ResourceLocation getTextureLocation();
}
