package com.verdantartifice.primalmagick.client.gui.widgets;

import java.awt.Color;
import java.util.List;
import java.util.function.BiConsumer;

import com.verdantartifice.primalmagick.client.util.GuiUtils;
import com.verdantartifice.primalmagick.common.items.essence.EssenceItem;
import com.verdantartifice.primalmagick.common.items.essence.EssenceType;
import com.verdantartifice.primalmagick.common.sources.Source;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

/**
 * Class for interactable display widgets which show how much of a type of essence is in an
 * essence cask.
 * 
 * @author Daedalus4096
 */
public class EssenceCaskWidget extends AbstractWidget {
    protected final int index;
    protected final EssenceType essenceType;
    protected final Source source;
    protected int amount;
    protected final BiConsumer<EssenceCaskWidget, Integer> onClick;
    
    public EssenceCaskWidget(int index, EssenceType type, Source source, int amount, int xIn, int yIn, BiConsumer<EssenceCaskWidget, Integer> onClick) {
        super(xIn, yIn, 16, 16, Component.empty());
        this.index = index;
        this.essenceType = type;
        this.source = source;
        this.amount = amount;
        this.onClick = onClick;
        
        Minecraft mc = Minecraft.getInstance();
        ItemStack stack = EssenceItem.getEssence(this.essenceType, this.source);
        List<Component> lines = stack.getTooltipLines(mc.player, mc.options.advancedItemTooltips ? TooltipFlag.Default.ADVANCED : TooltipFlag.Default.NORMAL);
        lines.add(Component.translatable("label.primalmagick.essence_cask.left_click").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        lines.add(Component.translatable("label.primalmagick.essence_cask.right_click").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        this.setTooltip(Tooltip.create(CommonComponents.joinLines(lines)));
    }
    
    public int getIndex() {
        return this.index;
    }
    
    public EssenceType getEssenceType() {
        return this.essenceType;
    }
    
    public Source getSource() {
        return this.source;
    }
    
    public int getAmount() {
        return this.amount;
    }
    
    public void setAmount(int amount) {
        this.amount = amount;
    }
    
    @Override
    public void renderWidget(GuiGraphics guiGraphics, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        Minecraft mc = Minecraft.getInstance();

        // Draw the essence item
        ItemStack tempStack = EssenceItem.getEssence(this.essenceType, this.source);
        GuiUtils.renderItemStack(guiGraphics, tempStack, this.getX(), this.getY(), this.getMessage().getString(), true);

        // Draw the amount string
        Component amountText = Component.literal(Integer.toString(this.amount));
        int width = mc.font.width(amountText);
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(this.getX() + 16 - width / 2, this.getY() + 12, 200.0F);
        guiGraphics.pose().scale(0.5F, 0.5F, 0.5F);
        guiGraphics.drawString(mc.font, amountText, 0, 0, this.amount > 0 ? Color.WHITE.getRGB() : Color.RED.getRGB());
        guiGraphics.pose().popPose();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int clickButton) {
        if (this.active && this.visible && this.clicked(mouseX, mouseY)) {
            this.onClick.accept(this, clickButton);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void updateWidgetNarration(NarrationElementOutput output) {
    }
}
