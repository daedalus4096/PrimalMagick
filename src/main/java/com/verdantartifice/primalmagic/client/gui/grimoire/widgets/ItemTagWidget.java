package com.verdantartifice.primalmagic.client.gui.grimoire.widgets;

import java.util.Collection;

import com.verdantartifice.primalmagic.client.util.GuiUtils;

import net.minecraft.client.gui.widget.Widget;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

public class ItemTagWidget extends Widget {
    protected ResourceLocation tag;
    
    public ItemTagWidget(ResourceLocation tag, int x, int y) {
        super(x, y, 16, 16, "");
        this.tag = tag;
    }
    
    @Override
    public void renderButton(int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        Tag<Item> itemTag = ItemTags.getCollection().getOrCreate(this.tag);
        Collection<Item> tagContents = itemTag.getAllElements();
        if (tagContents != null && !tagContents.isEmpty()) {
            int index = (int)((System.currentTimeMillis() / 1000L) % tagContents.size());
            Item[] tagContentsArray = tagContents.toArray(new Item[tagContents.size()]);
            ItemStack toDisplay = new ItemStack(tagContentsArray[index], 1);
            GuiUtils.renderItemStack(toDisplay, this.x, this.y, this.getMessage(), false);
            if (this.isHovered()) {
                // TODO Render tooltip
            }
        }
    }
    
    @Override
    public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
        // Disable click behavior
        return false;
    }
}
