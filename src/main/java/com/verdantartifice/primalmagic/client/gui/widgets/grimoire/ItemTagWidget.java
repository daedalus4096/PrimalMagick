package com.verdantartifice.primalmagic.client.gui.widgets.grimoire;

import java.util.Collection;

import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.util.GuiUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.tags.Tag;
import net.minecraft.tags.SerializationTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Display widget for showing all the possible itemstacks for a given tag.  Used
 * on the requirements and recipe pages.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
public class ItemTagWidget extends AbstractWidget {
    protected static final ResourceLocation GRIMOIRE_TEXTURE = new ResourceLocation(PrimalMagic.MODID, "textures/gui/grimoire.png");

    protected ResourceLocation tag;
    protected boolean isComplete;
    
    public ItemTagWidget(ResourceLocation tag, int x, int y, boolean isComplete) {
        super(x, y, 16, 16, TextComponent.EMPTY);
        this.tag = tag;
        this.isComplete = isComplete;
    }
    
    @Override
    public void renderButton(PoseStack matrixStack, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        Tag<Item> itemTag = SerializationTags.getInstance().getItems().getTag(this.tag);
        Collection<Item> tagContents = itemTag.getValues();
        if (tagContents != null && !tagContents.isEmpty()) {
            // Cycle through each matching stack of the tag and display them one at a time
            int index = (int)((System.currentTimeMillis() / 1000L) % tagContents.size());
            Item[] tagContentsArray = tagContents.toArray(new Item[tagContents.size()]);
            ItemStack toDisplay = new ItemStack(tagContentsArray[index], 1);
            GuiUtils.renderItemStack(matrixStack, toDisplay, this.x, this.y, this.getMessage().getString(), false);
            if (this.isComplete) {
                // Render completion checkmark if appropriate
                matrixStack.pushPose();
                matrixStack.translate(this.x + 8, this.y, 200.0F);
                Minecraft.getInstance().getTextureManager().bind(GRIMOIRE_TEXTURE);
                this.blit(matrixStack, 0, 0, 159, 207, 10, 10);
                matrixStack.popPose();
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
