package com.verdantartifice.primalmagic.client.renderers.itemstack;

import java.lang.reflect.Method;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.items.wands.ModularWandItem;
import com.verdantartifice.primalmagic.common.wands.WandCap;
import com.verdantartifice.primalmagic.common.wands.WandCore;
import com.verdantartifice.primalmagic.common.wands.WandGem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;

public class ModularWandTEISR extends ItemStackTileEntityRenderer {
    private static Method RENDER_MODEL_METHOD;

    static {
        try {
            RENDER_MODEL_METHOD = ItemRenderer.class.getDeclaredMethod("renderModel", IBakedModel.class, int.class, ItemStack.class);
            RENDER_MODEL_METHOD.setAccessible(true);
        } catch (Exception e) {
            RENDER_MODEL_METHOD = null;
            PrimalMagic.LOGGER.catching(e);
        }
    }

    @Override
    public void renderByItem(ItemStack itemStackIn) {
        if (itemStackIn.getItem() instanceof ModularWandItem) {
            Minecraft mc = Minecraft.getInstance();
            ItemRenderer itemRenderer = mc.getItemRenderer();
            
            ModularWandItem wand = (ModularWandItem)itemStackIn.getItem();
            WandCore core = wand.getWandCore(itemStackIn);
            WandCap cap = wand.getWandCap(itemStackIn);
            WandGem gem = wand.getWandGem(itemStackIn);
            
            try {
                if (core != null) {
                    RENDER_MODEL_METHOD.invoke(itemRenderer, mc.getModelManager().getModel(core.getModelResourceLocation()), Integer.valueOf(-1), itemStackIn);
                }
                if (cap != null) {
                    RENDER_MODEL_METHOD.invoke(itemRenderer, mc.getModelManager().getModel(cap.getModelResourceLocation()), Integer.valueOf(-1), itemStackIn);
                }
                if (gem != null) {
                    RENDER_MODEL_METHOD.invoke(itemRenderer, mc.getModelManager().getModel(gem.getModelResourceLocation()), Integer.valueOf(-1), itemStackIn);
                }
            } catch (Exception e) {
                PrimalMagic.LOGGER.catching(e);
            }
        }
    }
}
