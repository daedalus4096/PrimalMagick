package com.verdantartifice.primalmagick.platform.services;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface IGuiGraphicsService {
    void renderComponentTooltip(GuiGraphics pGuiGraphics, Font pFont, List<? extends FormattedText> pTooltipLines, int pMouseX, int pMouseY, ItemStack stack);
}
