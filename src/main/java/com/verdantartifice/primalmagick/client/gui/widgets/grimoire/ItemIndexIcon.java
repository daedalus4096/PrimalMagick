package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import org.joml.Matrix4f;

import com.mojang.blaze3d.platform.Lighting;

import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

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
        if (this.stack == null || this.stack.isEmpty()) {
            return;
        }
        
        // Can't just use GuiUtils.renderItemStack because it doesn't allow for custom scaling, boo
        Minecraft mc = Minecraft.getInstance();
        ItemRenderer itemRenderer = mc.getItemRenderer();
        BakedModel bakedModel = itemRenderer.getModel(this.stack, mc.level, mc.player, 0);
        
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(x + 8 + (this.large ? 0 : -3), y + 8 + (this.large ? 0 : -2), 150);
        
        try {
            guiGraphics.pose().mulPoseMatrix((new Matrix4f()).scaling(1.0F, -1.0F, 1.0F));
            guiGraphics.pose().scale(16.0F, 16.0F, 16.0F);
            
            if (!this.large) {
                float sizeScale = 0.67F;
                guiGraphics.pose().scale(sizeScale, sizeScale, sizeScale);  // All this duplicated code, just for this line...
            }
            
            boolean flag = !bakedModel.usesBlockLight();
            if (flag) {
                Lighting.setupForFlatItems();
            }
            itemRenderer.render(this.stack, ItemDisplayContext.GUI, false, guiGraphics.pose(), guiGraphics.bufferSource(), 15728880, OverlayTexture.NO_OVERLAY, bakedModel);
            guiGraphics.flush();
            if (flag) {
                Lighting.setupFor3DItems();
            }
        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.forThrowable(throwable, "Rendering item");
            CrashReportCategory crashreportcategory = crashreport.addCategory("Item being rendered");
            crashreportcategory.setDetail("Item Type", () -> {
                return String.valueOf((Object)this.stack.getItem());
            });
            crashreportcategory.setDetail("Registry Name", () -> String.valueOf(net.minecraftforge.registries.ForgeRegistries.ITEMS.getKey(this.stack.getItem())));
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
        
        guiGraphics.pose().popPose();
    }
}
