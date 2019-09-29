package com.verdantartifice.primalmagic.client.gui.grimoire.widgets;

import net.minecraft.client.gui.widget.Widget;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ProgressingWidget extends Widget {
    public ProgressingWidget(int xIn, int yIn, String msg) {
        super(xIn, yIn, 119, 18, msg);
        this.active = false;
    }
}
