package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.awt.Color;

/**
 * Display widget to mark a new section of research entries (e.g. "Updated") in the grimoire index.
 * 
 * @author Daedalus4096
 */
public class SectionHeaderWidget extends AbstractWidget {
    public SectionHeaderWidget(int xIn, int yIn, Component msg) {
        super(xIn, yIn, 123, 12, msg);
    }

    @Override
    public void extractWidgetRenderState(@NotNull GuiGraphicsExtractor pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        Minecraft mc = Minecraft.getInstance();
        pGuiGraphics.pose().pushMatrix();
        int strWidth = mc.font.width(this.getMessage().getString());
        int dy = (this.height - mc.font.lineHeight) / 2;
        if (strWidth <= this.width) {
            pGuiGraphics.text(mc.font, this.getMessage(), this.getX() + this.width / 2 - strWidth / 2, this.getY() + (this.height - 8) / 2, Color.BLACK.getRGB(), false);
        } else {
            // Scale the string down to fit on one line, if need be
            float scale = (float)this.width / (float)strWidth;
            pGuiGraphics.pose().pushMatrix();
            pGuiGraphics.pose().translate(this.getX(), this.getY() + dy + scale);
            pGuiGraphics.pose().scale(scale, scale);
            pGuiGraphics.text(mc.font, this.getMessage(), 0, 0, Color.BLACK.getRGB(), false);
            pGuiGraphics.pose().popMatrix();
        }
        pGuiGraphics.pose().popMatrix();
    }
    
    @Override
    public boolean mouseClicked(@NotNull MouseButtonEvent event, boolean isDoubleClick) {
        // Disable click behavior
        return false;
    }

    @Override
    public void updateWidgetNarration(@NotNull NarrationElementOutput output) {
    }
}
