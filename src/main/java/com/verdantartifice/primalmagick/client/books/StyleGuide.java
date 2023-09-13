package com.verdantartifice.primalmagick.client.books;

import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

/**
 * Represents a mapping of translation keys of localized words to the text styles that should be
 * used to render those words in static books, regardless of whether or not they're encoded in an
 * ancient language.
 * 
 * @author Daedalus4096
 */
public class StyleGuide {
    public static final Codec<StyleGuide> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Entry.CODEC.listOf().fieldOf("entries").forGetter(StyleGuide::getEntries)
        ).apply(instance, StyleGuide::new));
    
    private final List<Entry> entries;
    
    public StyleGuide(List<Entry> entries) {
        this.entries = entries;
    }
    
    public List<Entry> getEntries() {
        return this.entries;
    }
    
    public static class Entry {
        public static final Codec<Entry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.STRING.fieldOf("translationKey").forGetter(Entry::getTranslationKey), 
                Style.FORMATTING_CODEC.fieldOf("style").forGetter(Entry::getStyle)
            ).apply(instance, Entry::new));
        
        private final String translationKey;
        private final Style style;
        
        public Entry(String translationKey, Style style) {
            this.translationKey = translationKey;
            this.style = style;
        }
        
        public String getTranslationKey() {
            return this.translationKey;
        }
        
        public Style getStyle() {
            return this.style;
        }
        
        /**
         * Returns this entry's word with this entry's style applied to it as a renderable text component.
         * 
         * @return the stylized word
         */
        public Component getStylizedWord() {
            return Component.translatable(this.translationKey).withStyle(this.style);
        }
        
        /**
         * Returns this entry's word as a renderable text component with a merged style applied to it.  The
         * merged style is a combination of the given style and this entry's style, with formatting elements
         * of the given style taking precedence.  Typically this is used in cases where the given style
         * specifies an encoding font in which the word should be rendered, which should not be overridden.
         * 
         * @param encodingStyle a second style to be merged with this entry's style
         * @return the stylized word
         */
        public Component getStylizedWord(Style encodingStyle) {
            return Component.translatable(this.translationKey).withStyle(encodingStyle.applyTo(this.style));
        }
    }
}
