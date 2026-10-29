package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

/**
 * Icon to show a generic texture on a grimoire topic button.
 * 
 * @author Daedalus4096
 */
public class GenericIndexIcon extends AbstractIndexIcon {
    protected final Identifier iconLocation;
    
    protected GenericIndexIcon(Identifier loc, boolean large) {
        super(large);
        this.iconLocation = loc;
    }
    
    public static GenericIndexIcon of(Identifier loc, boolean large) {
        return new GenericIndexIcon(loc, large);
    }

    @Override
    public void render(GuiGraphicsExtractor guiGraphics, double x, double y, float scale) {
        if (this.iconLocation != null) {
            float s = this.large ? 0.06F : 0.04F;
            int d = this.large ? 8 : 5;
            guiGraphics.pose().pushMatrix();
            guiGraphics.pose().translate((float)x + d, (float)y + d);
            guiGraphics.pose().scale(s, s);
            guiGraphics.pose().scale(scale, scale);
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, this.iconLocation, (int)(-d / s), (int)(-d / s), 0, 0, 255, 255, 255, 255);
            guiGraphics.pose().popMatrix();
        }
    }
}
