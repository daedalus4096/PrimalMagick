package com.verdantartifice.primalmagick.platform;

import com.mojang.datafixers.util.Either;
import com.verdantartifice.primalmagick.platform.services.IGuiGraphicsService;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class GuiGraphicsServiceForge implements IGuiGraphicsService {
    @Override
    public void renderComponentTooltip(GuiGraphics pGuiGraphics, Font pFont, List<? extends FormattedText> pTooltipLines, int pMouseX, int pMouseY, ItemStack stack) {
        pGuiGraphics.renderComponentTooltip(pFont, pTooltipLines, pMouseX, pMouseY, stack);
    }

    @Override
    public void renderComponentTooltipFromElements(GuiGraphics pGuiGraphics, Font pFont, List<Either<FormattedText, TooltipComponent>> pElements, int pMouseX, int pMouseY, ItemStack stack) {
        pGuiGraphics.renderComponentTooltipFromElements(pFont, pElements, pMouseX, pMouseY, stack);
    }
}
