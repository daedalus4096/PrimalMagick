package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import com.verdantartifice.primalmagick.client.util.GuiUtils;

import net.minecraft.client.gui.GuiGraphics;
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
        if (this.stack.isEmpty()) {
            return;
        }
        
        guiGraphics.pose().pushPose();
        if (!this.large) {
            float sizeScale = 0.67F;
            guiGraphics.pose().scale(sizeScale, sizeScale, sizeScale);
        }
        GuiUtils.renderItemStack(guiGraphics, stack, (int)x, (int)y, null, true);
        guiGraphics.pose().popPose();
    }
}
