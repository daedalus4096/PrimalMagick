package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

/**
 * Icon to show a generic texture on a grimoire topic button.
 * 
 * @author Daedalus4096
 */
public class GenericIndexIcon extends AbstractIndexIcon {
    protected final ResourceLocation iconLocation;
    
    protected GenericIndexIcon(ResourceLocation loc, boolean large) {
        super(large);
        this.iconLocation = loc;
    }
    
    public static GenericIndexIcon of(ResourceLocation loc, boolean large) {
        return new GenericIndexIcon(loc, large);
    }

    @Override
    public void render(GuiGraphics guiGraphics, double x, double y, float scale) {
        if (this.iconLocation != null) {
            float s = scale * (this.large ? 0.06F : 0.04F);
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(x, y, 0D);
            guiGraphics.pose().scale(s, s, 1F);
            guiGraphics.blit(this.iconLocation, 0, 0, 0, 0, 255, 255);
            guiGraphics.pose().popPose();
        }
    }
}
