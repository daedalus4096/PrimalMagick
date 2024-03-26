package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import org.apache.logging.log4j.LogManager;

import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagick.common.books.BookLanguage;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

/**
 * GUI button to view the grimoire page for a given linguistics entry.
 * 
 * @author Daedalus4096
 */
public class LinguisticsButton extends AbstractTopicButton {
    protected final BookLanguage language;
    
    public LinguisticsButton(int widthIn, int heightIn, Component text, GrimoireScreen screen, BookLanguage language) {
        super(widthIn, heightIn, 123, 12, text, screen, null, new Handler());
        this.language = language;
    }
    
    public BookLanguage getLanguage() {
        return this.language;
    }
    
    private static class Handler implements OnPress {
        @Override
        public void onPress(Button button) {
            if (button instanceof LinguisticsButton gab) {
                // Set the new grimoire topic and open a new screen for it
//                gab.getScreen().gotoTopic(new SourceResearchTopic(gab.getSource(), 0));
                LogManager.getLogger().debug("Opening grimoire to topic for language {}", gab.getLanguage().languageId().toString());
            }
        }
    }
}
