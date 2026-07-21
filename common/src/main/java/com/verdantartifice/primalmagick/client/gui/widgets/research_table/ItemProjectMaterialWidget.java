package com.verdantartifice.primalmagick.client.gui.widgets.research_table;

import com.verdantartifice.primalmagick.client.util.GuiUtils;
import com.verdantartifice.primalmagick.common.theorycrafting.materials.ItemProjectMaterial;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.util.List;
import java.util.Set;

/**
 * Display widget for an item research project material.  Used on the research table screen.
 * 
 * @author Daedalus4096
 */
public class ItemProjectMaterialWidget extends AbstractProjectMaterialWidget<ItemProjectMaterial> {
    public ItemProjectMaterialWidget(ItemProjectMaterial material, int x, int y, Set<Block> surroundings) {
        super(material, x, y, surroundings);
    }
    
    @Override
    public void extractWidgetRenderState(@NotNull GuiGraphicsExtractor pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        // Draw stack icon and, if applicable, amount string
        Minecraft mc = Minecraft.getInstance();
        GuiUtils.renderItemStack(pGuiGraphics, this.material.getItemStack(), this.getX(), this.getY(), this.getMessage().getString(), false);
        if (this.material.getItemStack().getCount() > 1) {
            Component amountText = Component.literal(Integer.toString(this.material.getItemStack().getCount()));
            int width = mc.font.width(amountText);
            pGuiGraphics.pose().pushMatrix();
            pGuiGraphics.pose().translate(this.getX() + 16 - width / 2, this.getY() + 12);
            pGuiGraphics.pose().scale(0.5F, 0.5F);
            pGuiGraphics.text(mc.font, amountText, 0, 0, Color.WHITE.getRGB());
            pGuiGraphics.pose().popMatrix();
        }
        
        // Draw base class stuff
        super.extractWidgetRenderState(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }
    
    @Override
    protected List<Component> getHoverText() {
        Minecraft mc = Minecraft.getInstance();
        ItemStack stack = this.material.getItemStack();
        return stack.getTooltipLines(Item.TooltipContext.of(mc.level), mc.player, mc.options.advancedItemTooltips ? TooltipFlag.ADVANCED : TooltipFlag.NORMAL);
    }
}
