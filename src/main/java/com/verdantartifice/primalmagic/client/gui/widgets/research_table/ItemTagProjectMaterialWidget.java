package com.verdantartifice.primalmagic.client.gui.widgets.research_table;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.verdantartifice.primalmagic.client.util.GuiUtils;
import com.verdantartifice.primalmagic.common.theorycrafting.ItemTagProjectMaterial;

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ITag;
import net.minecraft.tags.TagCollectionManager;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
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
    public void renderWidget(MatrixStack matrixStack, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        // Draw time-selected stack icon and, if applicable, amount string
        Minecraft mc = Minecraft.getInstance();
        ItemStack toDisplay = this.getStackToDisplay();
        if (!toDisplay.isEmpty()) {
            GuiUtils.renderItemStack(toDisplay, this.x, this.y, this.getMessage().getString(), false);
            if (this.material.getQuantity() > 1) {
                ITextComponent amountText = new StringTextComponent(Integer.toString(this.material.getQuantity()));
                int width = mc.fontRenderer.getStringPropertyWidth(amountText);
                RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                RenderSystem.pushMatrix();
                RenderSystem.translatef(this.x + 16 - width / 2, this.y + 12, 500.0F);
                RenderSystem.scaled(0.5D, 0.5D, 0.5D);
                mc.fontRenderer.drawTextWithShadow(matrixStack, amountText, 0.0F, 0.0F, Color.WHITE.getRGB());
                RenderSystem.popMatrix();
            }
        }
        
        // Draw base class stuff
        super.renderWidget(matrixStack, p_renderButton_1_, p_renderButton_2_, p_renderButton_3_);
    }
    
    @Override
    protected List<ITextComponent> getHoverText() {
        Minecraft mc = Minecraft.getInstance();
        ItemStack stack = this.getStackToDisplay();
        List<ITextComponent> textList = new ArrayList<>();
        textList.add(stack.getDisplayName().deepCopy().mergeStyle(stack.getItem().getRarity(stack).color));
        stack.getItem().addInformation(stack, mc.world, textList, mc.gameSettings.advancedItemTooltips ? ITooltipFlag.TooltipFlags.ADVANCED : ITooltipFlag.TooltipFlags.NORMAL);
        return textList;
    }

    @Nonnull
    protected ItemStack getStackToDisplay() {
        ITag<Item> itemTag = TagCollectionManager.getManager().getItemTags().get(this.material.getTagName());
        Collection<Item> tagContents = itemTag.getAllElements();
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
