package com.verdantartifice.primalmagic.client.gui.grimoire.pages;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface IPageElement {
    public void render(int side, int x, int y);
    public int getNextY(int y);
}
