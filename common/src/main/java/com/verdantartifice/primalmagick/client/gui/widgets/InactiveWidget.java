package com.verdantartifice.primalmagick.client.gui.widgets;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

/**
 * An unclickable version of a button.
 * 
 * @author Daedalus4096
 */
public class InactiveWidget extends Button {
    public InactiveWidget(int x, int y, int width, int height, Component msg) {
        super(x, y, width, height, msg, new Button.OnPress() {
            @Override
            public void onPress(Button pButton) {
                // Do nothing
            }
        }, Button.DEFAULT_NARRATION);
        this.active = false;
    }
}
