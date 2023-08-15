package com.verdantartifice.primalmagick.client.renderers.itemstack;

import java.util.concurrent.atomic.AtomicBoolean;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.items.misc.ArcanometerItem;
import com.verdantartifice.primalmagick.common.util.EntityUtils;
import com.verdantartifice.primalmagick.common.util.RayTraceUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.entity.PartEntity;

/**
 * Custom item stack renderer for the arcanometer.
 * 
 * @author Daedalus4096
 * @see {@link com.verdantartifice.primalmagick.common.items.misc.ArcanometerItem}
 */
@SuppressWarnings("deprecation")
public class ArcanometerISTER extends BlockEntityWithoutLevelRenderer {
    private static final ModelResourceLocation MRL0 = new ModelResourceLocation(PrimalMagick.resource("arcanometer_0"), "");
    private static final ModelResourceLocation MRL1 = new ModelResourceLocation(PrimalMagick.resource("arcanometer_1"), "");
    private static final ModelResourceLocation MRL2 = new ModelResourceLocation(PrimalMagick.resource("arcanometer_2"), "");
    private static final ModelResourceLocation MRL3 = new ModelResourceLocation(PrimalMagick.resource("arcanometer_3"), "");
    private static final ModelResourceLocation MRL4 = new ModelResourceLocation(PrimalMagick.resource("arcanometer_4"), "");
    private static AtomicBoolean isRenderingScreen = new AtomicBoolean(false);
    
    public ArcanometerISTER() {
        super(Minecraft.getInstance() == null ? null : Minecraft.getInstance().getBlockEntityRenderDispatcher(), 
                Minecraft.getInstance() == null ? null : Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(ItemStack itemStack, ItemDisplayContext transformType, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        if (itemStack.getItem() instanceof ArcanometerItem) {
            Minecraft mc = Minecraft.getInstance();
            ItemRenderer itemRenderer = mc.getItemRenderer();

            // Render the base model
            BakedModel model = mc.getModelManager().getModel(this.getModelResourceLocation(itemStack));
            itemRenderer.renderModelLists(model, itemStack, combinedLight, combinedOverlay, matrixStack, buffer.getBuffer(RenderType.solid()));
            
            // We might be asked to show another arcanometer on screen; don't recurse in that case
            if (!isRenderingScreen.getAndSet(true)) {
                // Determine what to show on the screen
                ItemStack screenStack = ItemStack.EMPTY;
                HitResult result = RayTraceUtils.getMouseOver(mc.level);
                if (result != null) {
                    if (result.getType() == HitResult.Type.ENTITY) {
                        Entity entity = ((EntityHitResult)result).getEntity();
                        if (entity != null) {
                            screenStack = EntityUtils.getEntityItemStack(entity);
                            if (!screenStack.isEmpty()) {
                                this.renderScreenItem(itemRenderer, screenStack, matrixStack, buffer, combinedLight, combinedOverlay);
                            } else if (entity instanceof PartEntity<?> partEntity) {
                                this.renderScreenEntity(mc.getEntityRenderDispatcher(), partEntity.getParent(), matrixStack, buffer, combinedLight, combinedOverlay);
                            } else {
                                this.renderScreenEntity(mc.getEntityRenderDispatcher(), entity, matrixStack, buffer, combinedLight, combinedOverlay);
                            }
                        }
                    } else if (result.getType() == HitResult.Type.BLOCK) {
                        screenStack = new ItemStack(mc.level.getBlockState(((BlockHitResult)result).getBlockPos()).getBlock());
                        this.renderScreenItem(itemRenderer, screenStack, matrixStack, buffer, combinedLight, combinedOverlay);
                    }
                }
                
                isRenderingScreen.set(false);
            }
        }
    }
    
    private void renderScreenItem(ItemRenderer itemRenderer, ItemStack screenStack, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        Minecraft mc = Minecraft.getInstance();
        matrixStack.pushPose();
        matrixStack.translate(0.5D, 0.4375D, 0.405D);
        matrixStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        matrixStack.scale(0.2F, 0.2F, 0.0001F);
        itemRenderer.renderStatic(screenStack, ItemDisplayContext.GUI, combinedLight, combinedOverlay, matrixStack, buffer, mc.level, 0);
        matrixStack.popPose();
    }
    
    private void renderScreenEntity(EntityRenderDispatcher erm, Entity entity, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        float scale = 0.175F;
        float size = Math.max(entity.getBbWidth(), entity.getBbHeight());
        if ((double)size > 1.0D) {
           scale /= size;
        }
        matrixStack.pushPose();
        matrixStack.translate(0.5D, 0.35D, 0.405D);
        matrixStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        matrixStack.scale(scale, scale, 0.0001F);
        erm.render(entity, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F, matrixStack, buffer, combinedLight);
        matrixStack.popPose();
    }
    
    protected ModelResourceLocation getModelResourceLocation(ItemStack stack) {
        // Determine which model to use based on the scan state of the arcanometer item stack
        Minecraft mc = Minecraft.getInstance();
        ItemPropertyFunction propGetter = ItemProperties.getProperty(ItemsPM.ARCANOMETER.get(), ArcanometerItem.SCAN_STATE_PROPERTY);
        if (propGetter != null) {
            float value = propGetter.call(stack, mc.level, mc.player, 0);
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
