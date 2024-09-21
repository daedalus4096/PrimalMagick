package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagick.common.research.topics.SourceResearchTopic;
import com.verdantartifice.primalmagick.common.sources.Source;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

/**
 * GUI button to view the grimoire page for a given source attunement.
 * 
 * @author Daedalus4096
 */
public class AttunementButton extends AbstractTopicButton {
    protected Source source;
    
    public AttunementButton(int widthIn, int heightIn, Component text, GrimoireScreen screen, Source source) {
        super(widthIn, heightIn, 123, 12, text, screen, GenericIndexIcon.of(source.getImage(), false), new Handler());
        this.source = source;
    }
    
    public Source getSource() {
        return this.source;
    }
    
    private static class Handler implements OnPress {
        @Override
        public void onPress(Button button) {
            if (button instanceof AttunementButton gab) {
                // Set the new grimoire topic and open a new screen for it
                gab.getScreen().gotoTopic(new SourceResearchTopic(gab.getSource(), 0));
            }
        }
    }
}
