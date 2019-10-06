package com.verdantartifice.primalmagic.client.renderers.itemstack;

import java.lang.reflect.Method;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.items.wands.MundaneWandItem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MundaneWandTEISR extends ItemStackTileEntityRenderer {
    private static final ModelResourceLocation CORE_MRL = new ModelResourceLocation(new ResourceLocation(PrimalMagic.MODID, "wand_core"), "");
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
        if (itemStackIn.getItem() instanceof MundaneWandItem) {
            Minecraft mc = Minecraft.getInstance();
            ItemRenderer itemRenderer = mc.getItemRenderer();

            IBakedModel model = mc.getModelManager().getModel(CORE_MRL);
            try {
                RENDER_MODEL_METHOD.invoke(itemRenderer, model, Integer.valueOf(-1), itemStackIn);
            } catch (Exception e) {
                PrimalMagic.LOGGER.catching(e);
            }
        }
    }
}
