package com.verdantartifice.primalmagic.client.gui.grimoire.widgets;

import net.minecraft.client.gui.widget.Widget;

public class ProgressingWidget extends Widget {
    public ProgressingWidget(int xIn, int yIn, String msg) {
        super(xIn, yIn, 135, 18, msg);
        this.active = false;
    }
}
