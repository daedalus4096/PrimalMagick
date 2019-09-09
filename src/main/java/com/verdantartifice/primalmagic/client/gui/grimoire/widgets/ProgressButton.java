package com.verdantartifice.primalmagic.client.gui.grimoire.widgets;

import com.verdantartifice.primalmagic.PrimalMagic;

import net.minecraft.client.gui.widget.button.Button;

public class ProgressButton extends Button {
    public ProgressButton(int widthIn, int heightIn, String text) {
        super(widthIn, heightIn, 135, 18, text, new Handler());
    }
    
    private static class Handler implements IPressable {
        @Override
        public void onPress(Button button) {
            // TODO Auto-generated method stub
            PrimalMagic.LOGGER.info("Progressing research");
        }
    }
}
