package com.verdantartifice.primalmagic.client.gui.grimoire.buttons;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.gui.grimoire.GrimoireScreen;
import com.verdantartifice.primalmagic.common.research.ResearchEntry;

import net.minecraft.client.gui.widget.button.Button;

public class GrimoireEntryButton extends Button {

    public GrimoireEntryButton(int widthIn, int heightIn, String text, GrimoireScreen screen, ResearchEntry entry) {
        super(widthIn, heightIn, 135, 18, text, new Handler(text));
    }

    private static class Handler implements IPressable {
        private String message;
        
        public Handler(String message) {
            this.message = message;
        }
        
        @Override
        public void onPress(Button p_onPress_1_) {
            PrimalMagic.LOGGER.info("Pressed button for {}", this.message);
        }
    }

}
