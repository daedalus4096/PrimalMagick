package com.verdantartifice.primalmagick.client.gui.widgets;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.gui.widget.ExtendedButton;

/**
 * An unclickable version of a Forge ExtendedButton.
 * 
 * @author Daedalus4096
 */
public class InactiveWidget extends ExtendedButton {
    public InactiveWidget(int x, int y, int width, int height, Component msg) {
        super(x, y, width, height, msg, new Button.OnPress() {
            @Override
            public void onPress(Button pButton) {
                // Do nothing
            }
        });
        this.active = false;
    }
}
