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

/**
 * Custom item stack renderer for a mundane wand.
 * 
 * @author Daedalus4096
 * @see {@link com.verdantartifice.primalmagic.common.items.wands.MundaneWandItem}
 */
@OnlyIn(Dist.CLIENT)
public class MundaneWandTEISR extends ItemStackTileEntityRenderer {
    private static final ModelResourceLocation CORE_MRL = new ModelResourceLocation(new ResourceLocation(PrimalMagic.MODID, "mundane_wand_core"), "");
    private static Method RENDER_MODEL_METHOD;

    static {
        // The renderModel method of ItemRenderer is private, but we need it; so, expose it via reflection
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
                // Render the wand core
                RENDER_MODEL_METHOD.invoke(itemRenderer, model, Integer.valueOf(-1), itemStackIn);
            } catch (Exception e) {
                PrimalMagic.LOGGER.catching(e);
            }
        }
    }
}
