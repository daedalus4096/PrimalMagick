package com.verdantartifice.primalmagic.client.renderers.itemstack;

import java.lang.reflect.Method;

import com.mojang.blaze3d.platform.GlStateManager;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.items.misc.ArcanometerItem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@SuppressWarnings("deprecation")
@OnlyIn(Dist.CLIENT)
public class ArcanometerTEISR extends ItemStackTileEntityRenderer {
    private static final ModelResourceLocation MRL = new ModelResourceLocation(new ResourceLocation(PrimalMagic.MODID, "arcanometer"), "");
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
        if (itemStackIn.getItem() instanceof ArcanometerItem) {
            Minecraft mc = Minecraft.getInstance();
            ItemRenderer itemRenderer = mc.getItemRenderer();

            // Render the base model
            IBakedModel model = mc.getModelManager().getModel(MRL);
//            IBakedModel model = itemRenderer.getModelWithOverrides(itemStackIn, mc.world, mc.player);
            try {
                RENDER_MODEL_METHOD.invoke(itemRenderer, model, Integer.valueOf(-1), itemStackIn);
            } catch (Exception e) {
                PrimalMagic.LOGGER.catching(e);
            }
            
            // Determine what to show on the screen
            ItemStack screenStack = ItemStack.EMPTY;
            if (mc.objectMouseOver != null) {
                if (mc.objectMouseOver.getType() == RayTraceResult.Type.ENTITY) {
                    EntityRayTraceResult entityResult = (EntityRayTraceResult)mc.objectMouseOver;
                    if (entityResult.getEntity() instanceof ItemEntity) {
                        screenStack = ((ItemEntity)entityResult.getEntity()).getItem();
                    }
                } else if (mc.objectMouseOver.getType() == RayTraceResult.Type.BLOCK) {
                    BlockRayTraceResult blockResult = (BlockRayTraceResult)mc.objectMouseOver;
                    screenStack = new ItemStack(mc.world.getBlockState(blockResult.getPos()).getBlock());
                }
            }
            
            // Render the screen display
            GlStateManager.pushLightingAttributes();
            GlStateManager.disableLighting();
            GlStateManager.pushMatrix();
            GlStateManager.translated(0.5D, 0.4375D, 0.405D);
            GlStateManager.rotated(180.0D, 0.0D, 0.0D, 1.0D);
            GlStateManager.scaled(0.2D, -0.2D, 0.0001D);
            itemRenderer.renderItem(screenStack, ItemCameraTransforms.TransformType.GUI);
            GlStateManager.popMatrix();
            GlStateManager.enableLighting();
            GlStateManager.popAttributes();
        }
    }
}
