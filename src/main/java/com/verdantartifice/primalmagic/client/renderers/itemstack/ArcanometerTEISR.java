package com.verdantartifice.primalmagic.client.renderers.itemstack;

import java.lang.reflect.Method;

import com.mojang.blaze3d.platform.GlStateManager;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.items.misc.ArcanometerItem;
import com.verdantartifice.primalmagic.common.util.EntityUtils;
import com.verdantartifice.primalmagic.common.util.RayTraceUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Custom item stack renderer for the arcanometer.
 * 
 * @author Daedalus4096
 * @see {@link com.verdantartifice.primalmagic.common.items.misc.ArcanometerItem}
 */
@SuppressWarnings("deprecation")
@OnlyIn(Dist.CLIENT)
public class ArcanometerTEISR extends ItemStackTileEntityRenderer {
    private static final ModelResourceLocation MRL0 = new ModelResourceLocation(new ResourceLocation(PrimalMagic.MODID, "arcanometer_0"), "");
    private static final ModelResourceLocation MRL1 = new ModelResourceLocation(new ResourceLocation(PrimalMagic.MODID, "arcanometer_1"), "");
    private static final ModelResourceLocation MRL2 = new ModelResourceLocation(new ResourceLocation(PrimalMagic.MODID, "arcanometer_2"), "");
    private static final ModelResourceLocation MRL3 = new ModelResourceLocation(new ResourceLocation(PrimalMagic.MODID, "arcanometer_3"), "");
    private static final ModelResourceLocation MRL4 = new ModelResourceLocation(new ResourceLocation(PrimalMagic.MODID, "arcanometer_4"), "");
    private static final ResourceLocation SCAN_STATE_PROPERTY = new ResourceLocation(PrimalMagic.MODID, "scan_state");
    private static Method RENDER_MODEL_METHOD;
    private static boolean isRenderingScreen = false;

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
        if (itemStackIn.getItem() instanceof ArcanometerItem) {
            Minecraft mc = Minecraft.getInstance();
            ItemRenderer itemRenderer = mc.getItemRenderer();

            // Render the base model
            IBakedModel model = mc.getModelManager().getModel(this.getModelResourceLocation(itemStackIn));
            try {
                RENDER_MODEL_METHOD.invoke(itemRenderer, model, Integer.valueOf(-1), itemStackIn);
            } catch (Exception e) {
                PrimalMagic.LOGGER.catching(e);
            }
            
            if (!isRenderingScreen) {
                // We might be asked to show another arcanometer on screen; don't recurse in that case
                isRenderingScreen = true;
                
                // Determine what to show on the screen
                ItemStack screenStack = ItemStack.EMPTY;
                RayTraceResult result = RayTraceUtils.getMouseOver();
                if (result != null) {
                    if (result.getType() == RayTraceResult.Type.ENTITY) {
                        screenStack = EntityUtils.getEntityItemStack(((EntityRayTraceResult)result).getEntity());
                    } else if (result.getType() == RayTraceResult.Type.BLOCK) {
                        screenStack = new ItemStack(mc.world.getBlockState(((BlockRayTraceResult)result).getPos()).getBlock());
                    }
                }
                
                // Render the screen display
                GlStateManager.pushMatrix();
                GlStateManager.translated(0.5D, 0.4375D, 0.405D);
                GlStateManager.rotated(180.0D, 0.0D, 1.0D, 0.0D);
                GlStateManager.scaled(0.2D, 0.2D, 0.0001D);
                itemRenderer.renderItem(screenStack, ItemCameraTransforms.TransformType.GUI);
                GlStateManager.popMatrix();
                
                isRenderingScreen = false;
            }
        }
    }
    
    protected ModelResourceLocation getModelResourceLocation(ItemStack stack) {
        // Determine which model to use based on the scan state of the arcanometer item stack
        Minecraft mc = Minecraft.getInstance();
        IItemPropertyGetter propGetter = stack.getItem().getPropertyGetter(SCAN_STATE_PROPERTY);
        if (propGetter != null) {
            float value = propGetter.call(stack, mc.world, mc.player);
            if (value <= 0.0F) {
                return MRL0;
            } else if (value <= 1.0F) {
                return MRL1;
            } else if (value <= 2.0F) {
                return MRL2;
            } else if (value <= 3.0F) {
                return MRL3;
            } else {
                return MRL4;
            }
        } else {
            return MRL0;
        }
    }
}
