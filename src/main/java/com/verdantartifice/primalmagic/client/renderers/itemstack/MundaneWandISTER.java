package com.verdantartifice.primalmagic.client.renderers.itemstack;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.items.wands.MundaneWandItem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

/**
 * Custom item stack renderer for a mundane wand.
 * 
 * @author Daedalus4096
 * @see {@link com.verdantartifice.primalmagic.common.items.wands.MundaneWandItem}
 */
public class MundaneWandISTER extends BlockEntityWithoutLevelRenderer {
    private static final ModelResourceLocation CORE_MRL = new ModelResourceLocation(new ResourceLocation(PrimalMagic.MODID, "mundane_wand_core"), "");
    
    public MundaneWandISTER() {
        super(Minecraft.getInstance() == null ? null : Minecraft.getInstance().getBlockEntityRenderDispatcher(), 
                Minecraft.getInstance() == null ? null : Minecraft.getInstance().getEntityModels());
    }
    
    @Override
    public void renderByItem(ItemStack itemStack, ItemTransforms.TransformType transformType, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        if (itemStack.getItem() instanceof MundaneWandItem) {
            Minecraft mc = Minecraft.getInstance();
            ItemRenderer itemRenderer = mc.getItemRenderer();

            // Render the wand core
            BakedModel model = mc.getModelManager().getModel(CORE_MRL);
            VertexConsumer builder = buffer.getBuffer(RenderType.solid());
            itemRenderer.renderModelLists(model, itemStack, combinedLight, combinedOverlay, matrixStack, builder);
        }
    }
}
