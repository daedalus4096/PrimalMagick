package com.verdantartifice.primalmagic.client.gui.grimoire;

import com.mojang.blaze3d.vertex.PoseStack;

/**
 * Rendering interface for an element of a grimoire page
 * 
 * @author Daedalus4096
 */
public interface IPageElement {
    /**
     * Render this page element
     * @param matrixStack the current rendering matrix stack
     * @param side the side of the grimoire on which the page lies; 0 for left, 1 for right
     * @param x the page-relative X-coordinate at which to render this element
     * @param y the page-relative Y-coordinate at which to render this element
     */
    public void render(PoseStack matrixStack, int side, int x, int y);
    
    /**
     * Get the Y-coordinate at which to render the next page element
     * @param y the page-relative Y-coordinate of this element
     * @return the page-relative Y-coordinate at which to render the next element
     */
    public int getNextY(int y);
}
