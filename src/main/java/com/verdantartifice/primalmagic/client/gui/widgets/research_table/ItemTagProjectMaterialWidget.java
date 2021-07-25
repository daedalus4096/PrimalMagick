package com.verdantartifice.primalmagic.client.gui.widgets.research_table;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagic.client.util.GuiUtils;
import com.verdantartifice.primalmagic.common.theorycrafting.ItemTagProjectMaterial;

import net.minecraft.client.Minecraft;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.tags.Tag;
import net.minecraft.tags.SerializationTags;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Display widget for an item tag research project material.  Used on the research table screen.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
public class ItemTagProjectMaterialWidget extends AbstractProjectMaterialWidget {
    protected ItemTagProjectMaterial material;
    
    public ItemTagProjectMaterialWidget(ItemTagProjectMaterial material, int x, int y) {
        super(material, x, y);
        this.material = material;
    }
    
    @Override
    public void renderButton(PoseStack matrixStack, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        // Draw time-selected stack icon and, if applicable, amount string
        Minecraft mc = Minecraft.getInstance();
        ItemStack toDisplay = this.getStackToDisplay();
        if (!toDisplay.isEmpty()) {
            GuiUtils.renderItemStack(matrixStack, toDisplay, this.x, this.y, this.getMessage().getString(), false);
            if (this.material.getQuantity() > 1) {
                Component amountText = new TextComponent(Integer.toString(this.material.getQuantity()));
                int width = mc.font.width(amountText);
                matrixStack.pushPose();
                matrixStack.translate(this.x + 16 - width / 2, this.y + 12, 500.0F);
                matrixStack.scale(0.5F, 0.5F, 0.5F);
                mc.font.drawShadow(matrixStack, amountText, 0.0F, 0.0F, Color.WHITE.getRGB());
                matrixStack.popPose();
            }
        }
        
        // Draw base class stuff
        super.renderButton(matrixStack, p_renderButton_1_, p_renderButton_2_, p_renderButton_3_);
    }
    
    @Override
    protected List<Component> getHoverText() {
        Minecraft mc = Minecraft.getInstance();
        ItemStack stack = this.getStackToDisplay();
        List<Component> textList = new ArrayList<>();
        textList.add(stack.getHoverName().copy().withStyle(stack.getItem().getRarity(stack).color));
        stack.getItem().appendHoverText(stack, mc.level, textList, mc.options.advancedItemTooltips ? TooltipFlag.Default.ADVANCED : TooltipFlag.Default.NORMAL);
        return textList;
    }

    @Nonnull
    protected ItemStack getStackToDisplay() {
        Tag<Item> itemTag = SerializationTags.getInstance().getItems().getTag(this.material.getTagName());
        Collection<Item> tagContents = itemTag.getValues();
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
