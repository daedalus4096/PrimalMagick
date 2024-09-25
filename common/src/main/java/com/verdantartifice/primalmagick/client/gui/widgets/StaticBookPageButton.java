package com.verdantartifice.primalmagick.client.gui.widgets;

import com.verdantartifice.primalmagick.client.books.ClientBookHelper;
import com.verdantartifice.primalmagick.common.books.BookType;

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
    private final BookType bookType;
    
    public StaticBookPageButton(int pX, int pY, boolean pIsForward, Button.OnPress pOnPress, boolean pPlayTurnSound, BookType bookType) {
        super(pX, pY, pIsForward, pOnPress, pPlayTurnSound);
        this.isForward = pIsForward;
        this.bookType = bookType;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        ResourceLocation rl = ClientBookHelper.getSprites(this.bookType).getPageButton(this.isForward, this.isHoveredOrFocused());
        guiGraphics.blitSprite(rl, this.getX(), this.getY(), 23, 13);
    }
}
