package com.verdantartifice.primalmagic.client.renderers.itemstack;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.verdantartifice.primalmagic.common.items.wands.ModularWandItem;
import com.verdantartifice.primalmagic.common.wands.WandCap;
import com.verdantartifice.primalmagic.common.wands.WandCore;
import com.verdantartifice.primalmagic.common.wands.WandGem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Custom item stack renderer for a modular wand.
 * 
 * @author Daedalus4096
 * @see {@link com.verdantartifice.primalmagic.common.items.wands.ModularWandItem}
 */
@OnlyIn(Dist.CLIENT)
public class ModularWandISTER extends ItemStackTileEntityRenderer {
    @Override
    public void render(ItemStack itemStack, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        if (itemStack.getItem() instanceof ModularWandItem) {
            Minecraft mc = Minecraft.getInstance();
            ItemRenderer itemRenderer = mc.getItemRenderer();
            
            // Get the wand components so we can extract their model resource locations
            ModularWandItem wand = (ModularWandItem)itemStack.getItem();
            WandCore core = wand.getWandCore(itemStack);
            WandCap cap = wand.getWandCap(itemStack);
            WandGem gem = wand.getWandGem(itemStack);
            
            IVertexBuilder builder = buffer.getBuffer(RenderType.solid());
            if (core != null) {
                // Render the wand core
                IBakedModel model = mc.getModelManager().getModel(core.getWandModelResourceLocation());
                itemRenderer.renderModel(model, itemStack, combinedLight, combinedOverlay, matrixStack, builder);
            }
            if (cap != null) {
                // Render the wand cap
                IBakedModel model = mc.getModelManager().getModel(cap.getWandModelResourceLocation());
                itemRenderer.renderModel(model, itemStack, combinedLight, combinedOverlay, matrixStack, builder);
            }
            if (gem != null) {
                // Render the wand gem
                IBakedModel model = mc.getModelManager().getModel(gem.getModelResourceLocation());
                itemRenderer.renderModel(model, itemStack, combinedLight, combinedOverlay, matrixStack, builder);
            }
        }
    }
}
