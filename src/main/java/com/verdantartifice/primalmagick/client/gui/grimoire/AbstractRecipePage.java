package com.verdantartifice.primalmagick.client.gui.grimoire;

import com.verdantartifice.primalmagick.PrimalMagick;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

/**
 * Base class for all grimoire recipe pages.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractRecipePage extends AbstractPage {
    protected static final ResourceLocation OVERLAY = new ResourceLocation(PrimalMagick.MODID, "textures/gui/grimoire_overlay.png");
    
    @Override
    public void render(GuiGraphics guiGraphics, int side, int x, int y, int mouseX, int mouseY) {
        this.renderTitle(guiGraphics, side, x, y, mouseX, mouseY, null);
    }

    @Override
    protected boolean renderTopTitleBar() {
        return false;
    }
    
    protected abstract String getRecipeTypeTranslationKey();
}
