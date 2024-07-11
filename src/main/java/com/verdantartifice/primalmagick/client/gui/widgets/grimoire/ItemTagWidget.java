package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.client.util.GuiUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Display widget for showing all the possible itemstacks for a given tag.  Used
 * on the requirements pages.
 * 
 * @author Daedalus4096
 */
public class ItemTagWidget extends AbstractWidget {
    protected static final ResourceLocation GRIMOIRE_TEXTURE = PrimalMagick.resource("textures/gui/grimoire.png");

    protected final TagKey<Item> tag;
    protected final int amount;
    protected final boolean isComplete;
    protected ItemStack lastStack = ItemStack.EMPTY;
    protected ItemStack currentStack = ItemStack.EMPTY;

    public ItemTagWidget(TagKey<Item> tag, int amount, int x, int y, boolean isComplete) {
        super(x, y, 16, 16, Component.empty());
        this.tag = tag;
        this.amount = amount;
        this.isComplete = isComplete;
    }
    
    public ItemTagWidget(TagKey<Item> tag, int x, int y, boolean isComplete) {
        this(tag, 1, x, y, isComplete);
    }
    
    @Override
    public void renderWidget(GuiGraphics guiGraphics, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        this.lastStack = this.currentStack;
        this.currentStack = this.getDisplayStack();
        if (!this.currentStack.isEmpty()) {
            GuiUtils.renderItemStack(guiGraphics, this.currentStack, this.getX(), this.getY(), this.getMessage().getString(), false);
            if (this.isComplete) {
                // Render completion checkmark if appropriate
                guiGraphics.pose().pushPose();
                guiGraphics.pose().translate(this.getX() + 8, this.getY(), 200.0F);
                guiGraphics.blit(GRIMOIRE_TEXTURE, 0, 0, 159, 207, 10, 10);
                guiGraphics.pose().popPose();
            }
            
            // Update the widget tooltip if necessary
            this.updateTooltip();
        }
        
        // Don't allow the widget to become focused, to prevent keyboard navigation from moving the active tooltip
        this.setFocused(false);
    }
    
    protected void updateTooltip() {
        if (!ItemStack.isSameItemSameComponents(this.currentStack, this.lastStack)) {
            Minecraft mc = Minecraft.getInstance();
            this.setTooltip(Tooltip.create(CommonComponents.joinLines(this.currentStack.getTooltipLines(mc.player, mc.options.advancedItemTooltips ? TooltipFlag.Default.ADVANCED : TooltipFlag.Default.NORMAL))));
        }
    }
    
    @Nonnull
    protected ItemStack getDisplayStack() {
        List<Item> tagContents = new ArrayList<>();
        ForgeRegistries.ITEMS.tags().getTag(this.tag).forEach(tagContents::add);
        if (!tagContents.isEmpty()) {
            // Cycle through each matching stack of the tag and display them one at a time
            int index = (int)((System.currentTimeMillis() / 1000L) % tagContents.size());
            return new ItemStack(tagContents.get(index), this.amount);
        }
        return ItemStack.EMPTY;
    }
    
    @Override
    public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
        // Disable click behavior
        return false;
    }

    @Override
    public void updateWidgetNarration(NarrationElementOutput output) {
    }
}
