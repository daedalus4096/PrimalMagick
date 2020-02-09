package com.verdantartifice.primalmagic.client.gui.grimoire.widgets;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.util.GuiUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Display widget for showing all the possible itemstacks for a given tag.  Used
 * on the requirements and recipe pages.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
public class ItemTagWidget extends Widget {
    protected static final ResourceLocation GRIMOIRE_TEXTURE = new ResourceLocation(PrimalMagic.MODID, "textures/gui/grimoire.png");

    protected ResourceLocation tag;
    protected boolean isComplete;
    
    public ItemTagWidget(ResourceLocation tag, int x, int y, boolean isComplete) {
        super(x, y, 16, 16, "");
        this.tag = tag;
        this.isComplete = isComplete;
    }
    
    @Override
    public void renderButton(int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        Tag<Item> itemTag = ItemTags.getCollection().getOrCreate(this.tag);
        Collection<Item> tagContents = itemTag.getAllElements();
        if (tagContents != null && !tagContents.isEmpty()) {
            // Cycle through each matching stack of the tag and display them one at a time
            int index = (int)((System.currentTimeMillis() / 1000L) % tagContents.size());
            Item[] tagContentsArray = tagContents.toArray(new Item[tagContents.size()]);
            ItemStack toDisplay = new ItemStack(tagContentsArray[index], 1);
            GuiUtils.renderItemStack(toDisplay, this.x, this.y, this.getMessage(), false);
            if (this.isComplete) {
                // Render completion checkmark if appropriate
                RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                RenderSystem.pushMatrix();
                RenderSystem.translatef(this.x + 8, this.y, 100.0F);
                Minecraft.getInstance().getTextureManager().bindTexture(GRIMOIRE_TEXTURE);
                this.blit(0, 0, 159, 207, 10, 10);
                RenderSystem.popMatrix();
            }
            if (this.isHovered()) {
                // If hovered, show a tooltip with the display name of the current matching itemstack
                List<ITextComponent> textList = Collections.singletonList(toDisplay.getDisplayName());
                GuiUtils.renderCustomTooltip(textList, this.x, this.y);
            }
        }
    }
    
    @Override
    public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
        // Disable click behavior
        return false;
    }
}
