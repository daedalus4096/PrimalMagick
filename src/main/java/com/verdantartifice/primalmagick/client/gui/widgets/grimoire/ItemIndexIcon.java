package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import java.util.Optional;

import com.verdantartifice.primalmagick.client.util.GuiUtils;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.phys.Vec3;

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
        
        Optional<Vec3> scaleOpt = this.large ? Optional.empty() : Optional.of(new Vec3(0.67D, 0.67D, 1D));
        GuiUtils.renderItemStack(guiGraphics, this.stack, (int)(x + (this.large ? 0 : -3)), (int)(y + (this.large ? 0 : -2)), "", true, scaleOpt);
    }
}
