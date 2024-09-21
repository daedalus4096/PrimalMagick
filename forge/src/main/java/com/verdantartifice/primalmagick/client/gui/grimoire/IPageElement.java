package com.verdantartifice.primalmagick.client.gui.grimoire;

import net.minecraft.client.gui.GuiGraphics;

/**
 * Rendering interface for an element of a grimoire page
 * 
 * @author Daedalus4096
 */
public interface IPageElement {
    /**
     * Render this page element
     * @param guiGraphics the current graphics control object
     * @param side the side of the grimoire on which the page lies; 0 for left, 1 for right
     * @param x the page-relative X-coordinate at which to render this element
     * @param y the page-relative Y-coordinate at which to render this element
     */
    public void render(GuiGraphics guiGraphics, int side, int x, int y);
    
    /**
     * Get the Y-coordinate at which to render the next page element
     * @param y the page-relative Y-coordinate of this element
     * @return the page-relative Y-coordinate at which to render the next element
     */
    public default int getNextY(int y) {
        return y + this.getHeight();
    }
    
    /**
     * Get the height of this page element.
     * @return the height of this page element
     */
    public int getHeight();
}
