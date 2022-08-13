package com.verdantartifice.primalmagick.client.gui.widgets;

import java.awt.Color;
import java.util.function.BiConsumer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagick.client.util.GuiUtils;
import com.verdantartifice.primalmagick.common.items.essence.EssenceItem;
import com.verdantartifice.primalmagick.common.items.essence.EssenceType;
import com.verdantartifice.primalmagick.common.sources.Source;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

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
    public void renderButton(PoseStack matrixStack, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        Minecraft mc = Minecraft.getInstance();

        // Draw the essence item
        ItemStack tempStack = EssenceItem.getEssence(this.essenceType, this.source);
        GuiUtils.renderItemStack(matrixStack, tempStack, this.x, this.y, this.getMessage().getString(), true);

        // Draw the amount string
        Component amountText = Component.literal(Integer.toString(this.amount));
        int width = mc.font.width(amountText);
        matrixStack.pushPose();
        matrixStack.translate(this.x + 16 - width / 2, this.y + 12, 500.0F);
        matrixStack.scale(0.5F, 0.5F, 0.5F);
        mc.font.drawShadow(matrixStack, amountText, 0.0F, 0.0F, this.amount > 0 ? Color.WHITE.getRGB() : Color.RED.getRGB());
        matrixStack.popPose();
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
    public void renderToolTip(PoseStack matrixStack, int mouseX, int mouseY) {
        // Render tooltip
        matrixStack.pushPose();
        matrixStack.translate(0, 0, 200);
        ItemStack tempStack = EssenceItem.getEssence(this.essenceType, this.source);
        GuiUtils.renderItemTooltip(matrixStack, tempStack, mouseX, mouseY);
        matrixStack.popPose();
    }

    @Override
    public void updateNarration(NarrationElementOutput p_169152_) {
    }
}
