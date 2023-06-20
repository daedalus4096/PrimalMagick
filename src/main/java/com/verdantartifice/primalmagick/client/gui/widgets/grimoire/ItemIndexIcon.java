package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Icon to show an item stack texture on a grimoire topic button.
 * 
 * @author Daedalus4096
 */
public class ItemIndexIcon extends AbstractIndexIcon {
    protected final ItemStack stack;
    
    protected ItemIndexIcon(ItemStack stack, boolean large) {
        super(large);
        this.stack = stack;
    }
    
    public static ItemIndexIcon of(ItemLike item, boolean large) {
        return new ItemIndexIcon(new ItemStack(item.asItem()), large);
    }
    
    public static ItemIndexIcon of(ItemStack stack, boolean large) {
        return new ItemIndexIcon(stack, large);
    }
    
    @Override
    public void render(GuiGraphics guiGraphics, double x, double y) {
        if (this.stack.isEmpty()) {
            return;
        }
        
        // Can't just use GuiUtils.renderItemStack because it doesn't allow for custom scaling, boo
        Minecraft mc = Minecraft.getInstance();
        ItemRenderer itemRenderer = mc.getItemRenderer();
        
        guiGraphics.pose().pushPose();
        
        BakedModel bakedModel = itemRenderer.getModel(this.stack, null, mc.player, 0);
        itemRenderer.blitOffset = bakedModel.isGui3d() ? itemRenderer.blitOffset + 50.0F : itemRenderer.blitOffset + 50.0F;
        
        try {
            mc.textureManager.getTexture(InventoryMenu.BLOCK_ATLAS).setFilter(false, false);
            RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            PoseStack modelViewStack = RenderSystem.getModelViewStack();
            modelViewStack.pushPose();
            double sizeDelta = this.large ? 0D : 3D;
            modelViewStack.translate((double)x - sizeDelta, (double)y - sizeDelta, (double)(100.0D + itemRenderer.blitOffset));
            modelViewStack.translate(8.0D, 8.0D, 0.0D);
            modelViewStack.scale(1.0F, -1.0F, 1.0F);
            modelViewStack.scale(16.0F, 16.0F, 16.0F);
            if (!this.large) {
                float sizeScale = 0.67F;
                modelViewStack.scale(sizeScale, sizeScale, sizeScale);  // All this duplicated code, just for this line...
            }
            RenderSystem.applyModelViewMatrix();
            PoseStack defaultPoseStack = new PoseStack();
            MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
            boolean usesFlatLight = !bakedModel.usesBlockLight();
            if (usesFlatLight) {
                Lighting.setupForFlatItems();
            }
            itemRenderer.render(this.stack, ItemTransforms.TransformType.GUI, false, defaultPoseStack, bufferSource, 15728880, OverlayTexture.NO_OVERLAY, bakedModel);
            bufferSource.endBatch();
            RenderSystem.enableDepthTest();
            if (usesFlatLight) {
                Lighting.setupFor3DItems();
            }
            modelViewStack.popPose();
            RenderSystem.applyModelViewMatrix();
        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.forThrowable(throwable, "Rendering item");
            CrashReportCategory crashreportcategory = crashreport.addCategory("Item being rendered");
            crashreportcategory.setDetail("Item Type", () -> {
               return String.valueOf((Object)this.stack.getItem());
            });
            crashreportcategory.setDetail("Registry Name", () -> String.valueOf(ForgeRegistries.ITEMS.getKey(this.stack.getItem())));
            crashreportcategory.setDetail("Item Damage", () -> {
               return String.valueOf(this.stack.getDamageValue());
            });
            crashreportcategory.setDetail("Item NBT", () -> {
               return String.valueOf((Object)this.stack.getTag());
            });
            crashreportcategory.setDetail("Item Foil", () -> {
               return String.valueOf(this.stack.hasFoil());
            });
            throw new ReportedException(crashreport);
        }

        itemRenderer.blitOffset = bakedModel.isGui3d() ? itemRenderer.blitOffset - 50.0F : itemRenderer.blitOffset - 50.0F;

        guiGraphics.pose().popPose();
    }
}
