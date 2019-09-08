package com.verdantartifice.primalmagic.client.gui.grimoire.widgets;

import com.verdantartifice.primalmagic.client.util.GuiUtils;

import net.minecraft.client.gui.widget.Widget;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ItemStackWidget extends Widget {
    protected ItemStack stack;
    
    public ItemStackWidget(ItemStack stack, int x, int y) {
        super(x, y, 16, 16, "");
        this.stack = stack;
    }
    
    @Override
    public void renderButton(int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        GuiUtils.renderItemStack(this.stack, this.x, this.y, this.getMessage(), false);
        if (this.isHovered()) {
            // TODO Render tooltip
        }
    }
    
    @Override
    public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
        // Disable click behavior
        return false;
    }
}
