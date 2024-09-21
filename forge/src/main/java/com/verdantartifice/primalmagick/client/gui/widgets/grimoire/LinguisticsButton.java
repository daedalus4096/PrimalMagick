package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagick.common.books.BookLanguage;
import com.verdantartifice.primalmagick.common.research.topics.LanguageResearchTopic;

import net.minecraft.client.gui.components.Button;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;

/**
 * GUI button to view the grimoire page for a given linguistics entry.
 * 
 * @author Daedalus4096
 */
public class LinguisticsButton extends AbstractTopicButton {
    protected final Holder.Reference<BookLanguage> language;
    
    public LinguisticsButton(int widthIn, int heightIn, Component text, GrimoireScreen screen, Holder.Reference<BookLanguage> language) {
        super(widthIn, heightIn, 123, 12, text, screen, GenericIndexIcon.of(language.get().getGlyphSprite().withPrefix("textures/gui/sprites/").withSuffix(".png"), false), new Handler());
        this.language = language;
    }
    
    public Holder.Reference<BookLanguage> getLanguage() {
        return this.language;
    }
    
    private static class Handler implements OnPress {
        @Override
        public void onPress(Button button) {
            if (button instanceof LinguisticsButton gab) {
                // Set the new grimoire topic and open a new screen for it
                gab.getScreen().gotoTopic(new LanguageResearchTopic(gab.getLanguage().key(), 0));
            }
        }
    }
}
