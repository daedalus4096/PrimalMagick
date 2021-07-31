package com.verdantartifice.primalmagic.client.renderers.itemstack;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.verdantartifice.primalmagic.common.items.tools.AbstractTieredTridentItem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.TridentModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.ItemStack;
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
    protected TridentModel model;
    
    public AbstractTieredTridentISTER() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }
    
    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        this.model = new TridentModel(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.TRIDENT));
    }

    @Override
    public void renderByItem(ItemStack stack, ItemTransforms.TransformType transformType, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        if (stack.getItem() instanceof AbstractTieredTridentItem) {
            matrixStack.pushPose();
            matrixStack.scale(1.0F, -1.0F, -1.0F);
            VertexConsumer ivertexbuilder1 = ItemRenderer.getFoilBufferDirect(buffer, this.model.renderType(this.getTextureLocation()), false, stack.hasFoil());
            this.model.renderToBuffer(matrixStack, ivertexbuilder1, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
            matrixStack.popPose();
        }
    }
    
    public abstract ModelResourceLocation getModelResourceLocation();
    
    public abstract ResourceLocation getTextureLocation();
}
