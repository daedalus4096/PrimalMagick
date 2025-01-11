package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.platform.services.IGuiGraphicsService;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class GuiGraphicsServiceNeoforge implements IGuiGraphicsService {
    @Override
    public void renderComponentTooltip(GuiGraphics pGuiGraphics, Font pFont, List<? extends FormattedText> pTooltipLines, int pMouseX, int pMouseY, ItemStack stack) {
        pGuiGraphics.renderComponentTooltip(pFont, pTooltipLines, pMouseX, pMouseY, stack);
    }
}
