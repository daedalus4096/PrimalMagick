package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagick.client.util.GuiUtils;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

/**
 * Icon to show an item stack texture on a grimoire topic button.
 * 
 * @author Daedalus4096
 */
public class ItemIndexIcon extends AbstractIndexIcon {
    protected final ItemStack stack;
    
    protected ItemIndexIcon(ItemLike item, boolean large) {
        super(large);
        this.stack = new ItemStack(item.asItem());
    }
    
    public static ItemIndexIcon of(ItemLike item, boolean large) {
        return new ItemIndexIcon(item, large);
    }
    
    @Override
    public void render(PoseStack poseStack, double x, double y) {
        poseStack.pushPose();
        if (!this.large) {
            float scale = 0.67F;
            poseStack.scale(scale, scale, scale);
        }
        GuiUtils.renderItemStack(poseStack, this.stack, (int)x, (int)y, "", false);
        poseStack.popPose();
    }
}
