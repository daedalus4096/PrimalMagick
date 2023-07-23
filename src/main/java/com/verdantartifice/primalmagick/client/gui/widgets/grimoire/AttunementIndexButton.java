package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagick.client.gui.grimoire.AttunementIndexPage;
import com.verdantartifice.primalmagick.common.sources.Source;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

/**
 * GUI button to view the grimoire attunement index page.
 * 
 * @author Daedalus4096
 */
public class AttunementIndexButton extends AbstractTopicButton {
    public AttunementIndexButton(int widthIn, int heightIn, Component text, GrimoireScreen screen) {
        super(widthIn, heightIn, 123, 18, text, screen, GenericIndexIcon.of(Source.EARTH.getImage(), true), new Handler());
    }
    
    private static class Handler implements OnPress {
        @Override
        public void onPress(Button button) {
            if (button instanceof AttunementIndexButton gab) {
                // Set the new grimoire topic and open a new screen for it
                gab.getScreen().gotoTopic(AttunementIndexPage.TOPIC);
            }
        }
    }
}
