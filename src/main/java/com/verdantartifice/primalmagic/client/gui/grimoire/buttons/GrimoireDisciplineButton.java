package com.verdantartifice.primalmagic.client.gui.grimoire.buttons;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.gui.grimoire.GrimoireScreen;
import com.verdantartifice.primalmagic.common.research.ResearchDiscipline;

import net.minecraft.client.gui.widget.button.Button;

public class GrimoireDisciplineButton extends Button {

    public GrimoireDisciplineButton(int widthIn, int heightIn, int width, int height, String text, GrimoireScreen screen, ResearchDiscipline discipline) {
        super(widthIn, heightIn, width, height, text, new Handler(text));
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
