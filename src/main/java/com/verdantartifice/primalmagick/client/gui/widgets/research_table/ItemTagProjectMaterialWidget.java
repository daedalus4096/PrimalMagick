package com.verdantartifice.primalmagick.client.gui.widgets.research_table;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagick.client.util.GuiUtils;
import com.verdantartifice.primalmagick.common.theorycrafting.ItemTagProjectMaterial;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Display widget for an item tag research project material.  Used on the research table screen.
 * 
 * @author Daedalus4096
 */
public class ItemTagProjectMaterialWidget extends AbstractProjectMaterialWidget<ItemTagProjectMaterial> {
    public ItemTagProjectMaterialWidget(ItemTagProjectMaterial material, int x, int y, Set<Block> surroundings) {
        super(material, x, y, surroundings);
    }
    
    @Override
    public void renderWidget(GuiGraphics guiGraphics, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        // Draw time-selected stack icon and, if applicable, amount string
        Minecraft mc = Minecraft.getInstance();
        ItemStack toDisplay = this.getStackToDisplay();
        if (!toDisplay.isEmpty()) {
            GuiUtils.renderItemStack(guiGraphics, toDisplay, this.getX(), this.getY(), this.getMessage().getString(), false);
            if (this.material.getQuantity() > 1) {
                Component amountText = Component.literal(Integer.toString(this.material.getQuantity()));
                int width = mc.font.width(amountText);
                guiGraphics.pose().pushPose();
                guiGraphics.pose().translate(this.getX() + 16 - width / 2, this.getY() + 12, 200.0F);
                guiGraphics.pose().scale(0.5F, 0.5F, 0.5F);
                guiGraphics.drawString(mc.font, amountText, 0, 0, Color.WHITE.getRGB());
                guiGraphics.pose().popPose();
            }
        }
        
        // Draw base class stuff
        super.renderWidget(guiGraphics, p_renderButton_1_, p_renderButton_2_, p_renderButton_3_);
    }
    
    @Override
    protected List<Component> getHoverText() {
        Minecraft mc = Minecraft.getInstance();
        ItemStack stack = this.getStackToDisplay();
        List<Component> textList = new ArrayList<>();
        MutableComponent nameComponent = stack.getHoverName().copy();
        if (nameComponent.getStyle().equals(Style.EMPTY)) {
            nameComponent = nameComponent.withStyle(stack.getItem().getRarity(stack).getStyleModifier());
        }
        if (stack.hasCustomHoverName()) {
            nameComponent = nameComponent.withStyle(nameComponent.getStyle().applyFormat(ChatFormatting.ITALIC));
        }
        textList.add(nameComponent);
        if (ItemStack.shouldShowInTooltip(stack.getHideFlags(), ItemStack.TooltipPart.ADDITIONAL)) {
            stack.getItem().appendHoverText(stack, mc.level, textList, mc.options.advancedItemTooltips ? TooltipFlag.Default.ADVANCED : TooltipFlag.Default.NORMAL);
        }
        return textList;
    }

    @Nonnull
    protected ItemStack getStackToDisplay() {
        TagKey<Item> itemTag = ItemTags.create(this.material.getTagName());
        List<Item> tagContents = new ArrayList<Item>();
        ForgeRegistries.ITEMS.tags().getTag(itemTag).forEach(i -> tagContents.add(i));
        if (tagContents != null && !tagContents.isEmpty()) {
            // Cycle through each matching stack of the tag and display them one at a time
            int index = (int)((System.currentTimeMillis() / 1000L) % tagContents.size());
            Item[] tagContentsArray = tagContents.toArray(new Item[tagContents.size()]);
            return new ItemStack(tagContentsArray[index], 1);
        } else {
            return ItemStack.EMPTY;
        }
    }
}
