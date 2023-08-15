package com.verdantartifice.primalmagick.client.gui.grimoire;

import com.verdantartifice.primalmagick.PrimalMagick;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;

/**
 * Base class for all grimoire recipe pages.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractRecipePage extends AbstractPage {
    protected static final ResourceLocation OVERLAY = PrimalMagick.resource("textures/gui/grimoire_overlay.png");
    
    protected final RegistryAccess registryAccess;
    
    public AbstractRecipePage(RegistryAccess registryAccess) {
        this.registryAccess = registryAccess;
    }
    
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
