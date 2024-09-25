package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagick.client.gui.grimoire.TipsPage;
import com.verdantartifice.primalmagick.common.sources.Source;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

/**
 * GUI button to view the grimoire tips page.
 * 
 * @author Daedalus4096
 */
public class TipsButton extends AbstractTopicButton {
    public TipsButton(int widthIn, int heightIn, Component text, GrimoireScreen screen) {
        super(widthIn, heightIn, 123, 18, text, screen, GenericIndexIcon.of(Source.getUnknownImage(), true), new Handler());
    }
    
    private static class Handler implements OnPress {
        @Override
        public void onPress(Button button) {
            if (button instanceof TipsButton gtb) {
                // Set the new grimoire topic and open a new screen for it
                gtb.getScreen().invalidateCurrentTip();
                gtb.getScreen().gotoTopic(TipsPage.TOPIC, false);
            }
        }
    }
}
