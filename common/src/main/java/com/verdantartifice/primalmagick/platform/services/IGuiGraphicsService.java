package com.verdantartifice.primalmagick.platform.services;

import com.mojang.datafixers.util.Either;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface IGuiGraphicsService {
    void renderComponentTooltip(GuiGraphicsExtractor pGuiGraphics, Font pFont, List<? extends FormattedText> pTooltipLines, int pMouseX, int pMouseY, ItemStack stack);
    void renderComponentTooltipFromElements(GuiGraphicsExtractor pGuiGraphics, Font pFont, List<Either<FormattedText, TooltipComponent>> pElements, int pMouseX, int pMouseY, ItemStack stack);
}
