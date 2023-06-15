package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

/**
 * GUI placeholder to show instead of the progress button while waiting for the server to process.
 * 
 * @author Daedalus4096
 */
public class ProgressingWidget extends AbstractWidget {
    public ProgressingWidget(int xIn, int yIn, Component msg) {
        super(xIn, yIn, 119, 20, msg);
        this.active = false;
    }

    @Override
    public void updateWidgetNarration(NarrationElementOutput output) {
    }
}
