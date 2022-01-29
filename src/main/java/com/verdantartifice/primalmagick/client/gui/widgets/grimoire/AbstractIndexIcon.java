package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.GuiComponent;

/**
 * Base class for an icon to show on a grimoire topic button.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractIndexIcon extends GuiComponent {
    protected final boolean large;
    
    protected AbstractIndexIcon(boolean large) {
        this.large = large;
    }
    
    public boolean isLarge() {
        return this.large;
    }
    
    public abstract void render(PoseStack poseStack, double x, double y);
}
