package com.verdantartifice.primalmagic.client.gui.widgets.grimoire;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.util.GuiUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ITag;
import net.minecraft.tags.TagCollectionManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
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
        super(x, y, 16, 16, StringTextComponent.EMPTY);
        this.tag = tag;
        this.isComplete = isComplete;
    }
    
    @Override
    public void renderWidget(MatrixStack matrixStack, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        ITag<Item> itemTag = TagCollectionManager.getManager().getItemTags().get(this.tag);
        Collection<Item> tagContents = itemTag.getAllElements();
        if (tagContents != null && !tagContents.isEmpty()) {
            // Cycle through each matching stack of the tag and display them one at a time
            int index = (int)((System.currentTimeMillis() / 1000L) % tagContents.size());
            Item[] tagContentsArray = tagContents.toArray(new Item[tagContents.size()]);
            ItemStack toDisplay = new ItemStack(tagContentsArray[index], 1);
            GuiUtils.renderItemStack(toDisplay, this.x, this.y, this.getMessage().getString(), false);
            if (this.isComplete) {
                // Render completion checkmark if appropriate
                RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                RenderSystem.pushMatrix();
                RenderSystem.translatef(this.x + 8, this.y, 200.0F);
                Minecraft.getInstance().getTextureManager().bindTexture(GRIMOIRE_TEXTURE);
                this.blit(matrixStack, 0, 0, 159, 207, 10, 10);
                RenderSystem.popMatrix();
            }
            if (this.isHovered()) {
                // If hovered, show a tooltip with the display name of the current matching itemstack
                GuiUtils.renderItemTooltip(matrixStack, toDisplay, this.x, this.y);
            }
        }
    }
    
    @Override
    public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
        // Disable click behavior
        return false;
    }
}
