package com.verdantartifice.primalmagick.client.renderers.itemstack;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.verdantartifice.primalmagick.common.items.wands.ModularWandItem;
import com.verdantartifice.primalmagick.common.wands.WandCap;
import com.verdantartifice.primalmagick.common.wands.WandCore;
import com.verdantartifice.primalmagick.common.wands.WandGem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

/**
 * Custom item stack renderer for a modular wand.
 * 
 * @author Daedalus4096
 * @see {@link com.verdantartifice.primalmagick.common.items.wands.ModularWandItem}
 */
public class ModularWandISTER extends BlockEntityWithoutLevelRenderer {
    public ModularWandISTER() {
        super(Minecraft.getInstance() == null ? null : Minecraft.getInstance().getBlockEntityRenderDispatcher(), 
                Minecraft.getInstance() == null ? null : Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(ItemStack itemStack, ItemDisplayContext transformType, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        if (itemStack.getItem() instanceof ModularWandItem wand) {
            Minecraft mc = Minecraft.getInstance();
            ItemRenderer itemRenderer = mc.getItemRenderer();
            
            // Get the wand appearance components so we can extract their model resource locations
            WandCore core = wand.getWandCoreAppearance(itemStack);
            WandCap cap = wand.getWandCapAppearance(itemStack);
            WandGem gem = wand.getWandGemAppearance(itemStack);
            
            VertexConsumer builder = ItemRenderer.getFoilBufferDirect(buffer, RenderType.solid(), false, itemStack.hasFoil());
            if (core != null) {
                // Render the wand core
                BakedModel model = mc.getModelManager().getModel(new ModelResourceLocation(core.getWandModelResourceLocationNamespace(), ""));
                itemRenderer.renderModelLists(model, itemStack, combinedLight, combinedOverlay, matrixStack, builder);
            }
            if (cap != null) {
                // Render the wand cap
                BakedModel model = mc.getModelManager().getModel(new ModelResourceLocation(cap.getWandModelResourceLocationNamespace(), ""));
                itemRenderer.renderModelLists(model, itemStack, combinedLight, combinedOverlay, matrixStack, builder);
            }
            if (gem != null) {
                // Render the wand gem
                BakedModel model = mc.getModelManager().getModel(new ModelResourceLocation(gem.getModelResourceLocationNamespace(), ""));
                itemRenderer.renderModelLists(model, itemStack, combinedLight, combinedOverlay, matrixStack, builder);
            }
        }
    }
}
