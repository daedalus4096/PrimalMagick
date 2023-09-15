package com.verdantartifice.primalmagick.client.gui.widgets;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.PageButton;
import net.minecraft.resources.ResourceLocation;

/**
 * Page-turning button widget for the static book view screen.
 * 
 * @author Daedalus4096
 */
public class StaticBookPageButton extends PageButton {
    private final boolean isForward;
    private final ResourceLocation texture;
    
    public StaticBookPageButton(int pX, int pY, boolean pIsForward, Button.OnPress pOnPress, boolean pPlayTurnSound, ResourceLocation texture) {
        super(pX, pY, pIsForward, pOnPress, pPlayTurnSound);
        this.isForward = pIsForward;
        this.texture = texture;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        int texX = 0 + (this.isHoveredOrFocused() ? 23 : 0);
        int texY = 192 + (!this.isForward ? 13 : 0);
        guiGraphics.blit(this.texture, this.getX(), this.getY(), texX, texY, 23, 13);
    }
}
