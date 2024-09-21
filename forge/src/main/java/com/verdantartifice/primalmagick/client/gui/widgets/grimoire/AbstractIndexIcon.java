package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import net.minecraft.client.gui.GuiGraphics;

/**
 * Base class for an icon to show on a grimoire topic button.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractIndexIcon {
    protected final boolean large;
    
    protected AbstractIndexIcon(boolean large) {
        this.large = large;
    }
    
    public boolean isLarge() {
        return this.large;
    }
    
    public abstract void render(GuiGraphics guiGraphics, double x, double y);
}
