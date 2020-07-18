package com.verdantartifice.primalmagic.client.renderers.itemstack;

import java.lang.reflect.Method;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.items.wands.ModularStaffItem;
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
 * Custom item stack renderer for a modular staff.
 * 
 * @author Daedalus4096
 * @see {@link com.verdantartifice.primalmagic.common.items.wands.ModularStaffItem}
 */
@OnlyIn(Dist.CLIENT)
public class ModularStaffISTER extends ItemStackTileEntityRenderer {
    private static Method RENDER_MODEL_METHOD;

    static {
        // The renderModel method of ItemRenderer is private, but we need it; so, expose it via reflection
        try {
            RENDER_MODEL_METHOD = ItemRenderer.class.getDeclaredMethod("renderModel", IBakedModel.class, ItemStack.class, int.class, int.class, MatrixStack.class, IVertexBuilder.class);
            RENDER_MODEL_METHOD.setAccessible(true);
        } catch (Exception e) {
            RENDER_MODEL_METHOD = null;
            PrimalMagic.LOGGER.catching(e);
        }
    }

    @Override
    public void render(ItemStack itemStack, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        if (itemStack.getItem() instanceof ModularStaffItem) {
            Minecraft mc = Minecraft.getInstance();
            ItemRenderer itemRenderer = mc.getItemRenderer();
            
            // Get the staff components so we can extract their model resource locations
            ModularWandItem wand = (ModularStaffItem)itemStack.getItem();
            WandCore core = wand.getWandCore(itemStack);
            WandCap cap = wand.getWandCap(itemStack);
            WandGem gem = wand.getWandGem(itemStack);
            
            IVertexBuilder builder = buffer.getBuffer(RenderType.solid());
            try {
                if (core != null) {
                    // Render the staff core
                    IBakedModel model = mc.getModelManager().getModel(core.getStaffModelResourceLocation());
                    RENDER_MODEL_METHOD.invoke(itemRenderer, model, itemStack, combinedLight, combinedOverlay, matrixStack, builder);
                }
                if (cap != null) {
                    // Render the staff cap
                    IBakedModel model = mc.getModelManager().getModel(cap.getStaffModelResourceLocation());
                    RENDER_MODEL_METHOD.invoke(itemRenderer, model, itemStack, combinedLight, combinedOverlay, matrixStack, builder);
                }
                if (gem != null) {
                    // Render the staff gem
                    IBakedModel model = mc.getModelManager().getModel(gem.getModelResourceLocation());
                    RENDER_MODEL_METHOD.invoke(itemRenderer, model, itemStack, combinedLight, combinedOverlay, matrixStack, builder);
                }
            } catch (Exception e) {
                PrimalMagic.LOGGER.catching(e);
            }
        }
    }
}
