package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagick.client.gui.grimoire.LinguisticsIndexPage;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;

/**
 * GUI button to view the grimoire linguistics index page.
 * 
 * @author Daedalus4096
 */
public class LinguisticsIndexButton extends AbstractTopicButton {
    public LinguisticsIndexButton(int widthIn, int heightIn, Component text, GrimoireScreen screen) {
        super(widthIn, heightIn, 123, 17, text, screen, ItemIndexIcon.of(Items.WRITABLE_BOOK, true), new Handler());
    }

    private static class Handler implements OnPress {
        @Override
        public void onPress(Button button) {
            if (button instanceof LinguisticsIndexButton gab) {
                // Set the new grimoire topic and open a new screen for it
                gab.getScreen().gotoTopic(LinguisticsIndexPage.TOPIC);
            }
        }
    }
}
