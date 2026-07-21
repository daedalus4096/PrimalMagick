package com.verdantartifice.primalmagick.client.gui.widgets.research_table;

import com.verdantartifice.primalmagick.client.util.GuiUtils;
import com.verdantartifice.primalmagick.common.theorycrafting.materials.ItemTagProjectMaterial;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
    public void extractWidgetRenderState(@NotNull GuiGraphicsExtractor pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        // Draw time-selected stack icon and, if applicable, amount string
        Minecraft mc = Minecraft.getInstance();
        ItemStack toDisplay = this.getStackToDisplay();
        if (!toDisplay.isEmpty()) {
            GuiUtils.renderItemStack(pGuiGraphics, toDisplay, this.getX(), this.getY(), this.getMessage().getString(), false);
            if (this.material.getQuantity() > 1) {
                Component amountText = Component.literal(Integer.toString(this.material.getQuantity()));
                int width = mc.font.width(amountText);
                pGuiGraphics.pose().pushMatrix();
                pGuiGraphics.pose().translate(this.getX() + 16 - width / 2, this.getY() + 12);
                pGuiGraphics.pose().scale(0.5F, 0.5F);
                pGuiGraphics.text(mc.font, amountText, 0, 0, Color.WHITE.getRGB());
                pGuiGraphics.pose().popMatrix();
            }
        }
        
        // Draw base class stuff
        super.extractWidgetRenderState(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }
    
    @Override
    protected List<Component> getHoverText() {
        Minecraft mc = Minecraft.getInstance();
        ItemStack stack = this.getStackToDisplay();
        return stack.getTooltipLines(Item.TooltipContext.of(mc.level), mc.player, mc.options.advancedItemTooltips ? TooltipFlag.ADVANCED : TooltipFlag.NORMAL);
    }

    @Nonnull
    protected ItemStack getStackToDisplay() {
        TagKey<Item> itemTag = this.material.getTag();
        List<Item> tagContents = new ArrayList<>();
        Services.ITEMS_REGISTRY.getTag(itemTag).ifPresent(tag -> tag.forEach(tagContents::add));
        if (!tagContents.isEmpty()) {
            // Cycle through each matching stack of the tag and display them one at a time
            int index = (int)((System.currentTimeMillis() / 1000L) % tagContents.size());
            Item[] tagContentsArray = tagContents.toArray(Item[]::new);
            return new ItemStack(tagContentsArray[index], 1);
        } else {
            return ItemStack.EMPTY;
        }
    }
}
