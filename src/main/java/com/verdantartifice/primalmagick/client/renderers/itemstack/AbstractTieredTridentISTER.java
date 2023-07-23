package com.verdantartifice.primalmagick.client.renderers.itemstack;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.verdantartifice.primalmagick.common.items.tools.AbstractTieredTridentItem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.TridentModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

/**
 * Custom item stack renderer for magickal metal tridents.
 * 
 * @author Daedalus4096
 * @see {@link com.verdantartifice.primalmagick.common.items.tools.AbstractTieredTridentItem}
 */
public abstract class AbstractTieredTridentISTER extends BlockEntityWithoutLevelRenderer {
    protected TridentModel model;
    
    public AbstractTieredTridentISTER() {
        super(Minecraft.getInstance() == null ? null : Minecraft.getInstance().getBlockEntityRenderDispatcher(), 
                Minecraft.getInstance() == null ? null : Minecraft.getInstance().getEntityModels());
    }
    
    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        this.model = new TridentModel(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.TRIDENT));
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext transformType, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        if (stack.getItem() instanceof AbstractTieredTridentItem) {
            Minecraft mc = Minecraft.getInstance();
            ItemRenderer itemRenderer = mc.getItemRenderer();
            
            boolean render2d = (transformType == ItemDisplayContext.GUI ||
                                transformType == ItemDisplayContext.GROUND ||
                                transformType == ItemDisplayContext.FIXED);
            if (render2d) {
                BakedModel bakedModel = mc.getModelManager().getModel(this.getModelResourceLocation());
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
