package com.verdantartifice.primalmagic.client.gui.grimoire.pages;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.gui.grimoire.GrimoireScreen;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class AbstractRecipePage extends AbstractPage {
    protected static final ResourceLocation OVERLAY = new ResourceLocation(PrimalMagic.MODID, "textures/gui/grimoire_overlay.png");
    
    public abstract void initWidgets(GrimoireScreen screen, int side, int x, int y);
    
    @Override
    public void render(int side, int x, int y, int mouseX, int mouseY) {
        this.renderTitle(side, x, y, mouseX, mouseY);
    }

    @Override
    protected boolean renderTopTitleBar() {
        return false;
    }
}
