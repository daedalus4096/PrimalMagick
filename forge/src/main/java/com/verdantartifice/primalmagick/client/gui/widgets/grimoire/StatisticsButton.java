package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagick.client.gui.grimoire.StatisticsPage;
import com.verdantartifice.primalmagick.common.items.ItemsPM;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

/**
 * GUI button to view the grimoire statistics page.
 * 
 * @author Daedalus4096
 */
public class StatisticsButton extends AbstractTopicButton {
    public StatisticsButton(int widthIn, int heightIn, Component text, GrimoireScreen screen) {
        super(widthIn, heightIn, 123, 18, text, screen, ItemIndexIcon.of(ItemsPM.THEORY_NOTES.get(), true), new Handler());
    }
    
    private static class Handler implements OnPress {
        @Override
        public void onPress(Button button) {
            if (button instanceof StatisticsButton gsb) {
                // Set the new grimoire topic and open a new screen for it
                gsb.getScreen().gotoTopic(StatisticsPage.TOPIC);
            }
        }
    }
}
