package com.verdantartifice.primalmagick.client.books;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.books.BookLanguage;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringDecomposer;

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
    
    protected StyleGuide(List<Entry> entries) {
        this.entries = ImmutableList.copyOf(entries);
    }
    
    public static StyleGuide.Builder builder(BookLanguage language) {
        return new StyleGuide.Builder(language.languageId());
    }
    
    protected List<Entry> getEntries() {
        return this.entries;
    }
    
    public int size() {
        return this.getEntries().size();
    }
    
    /**
     * Gets the style prescribed by this guide for the given word.  If no style is prescribed by
     * this guide for the given word, then an empty style is returned.
     * 
     * @param word the word for which to query
     * @return the style to be used with the word
     */
    public Style getStyle(String word) {
        return this.getStyle(word, Style.EMPTY);
    }
    
    /**
     * Gets the style prescribed by this guide for the given word, merged with the given style.  The
     * formatting elements of the given style take precedence in the merge.  If no style is prescribed
     * by this guide for the given word, then the given style is returned unmodified.
     * 
     * @param word the word for which to query
     * @param encodingStyle a second style to be merged with the word's prescribed style
     * @return the style to be used with the word
     */
    public Style getStyle(String word, Style encodingStyle) {
        return this.entries.stream().filter(entry -> entry.matches(word)).findFirst().<Style>map(entry -> encodingStyle.applyTo(entry.getStyle())).orElse(encodingStyle);
    }
    
    /**
     * Returns the given word with this guide's prescribed style applied to it as a renderable text component.
     * 
     * @param word the word to be styled, if applicable
     * @return the stylized word
     */
    public Component getStylizedWord(String word) {
        return this.getStylizedWord(word, Style.EMPTY);
    }
    
    /**
     * Returns the given word as a renderable text component with a merged style applied to it.  The
     * merged style is a combination of the given style and the style prescribed for the word by this 
     * guide, with formatting elements of the given style taking precedence.  Typically this is used in 
     * cases where the given style specifies an encoding font in which the word should be rendered, 
     * which should not be overridden.
     * 
     * @param word the word to be styled, if applicable
     * @param encodingStyle a second style to be merged with the word's prescribed style
     * @return the stylized word
     */
    public Component getStylizedWord(String word, Style encodingStyle) {
        return this.entries.stream().filter(entry -> entry.matches(word)).findFirst().<Component>map(entry -> entry.getStylizedWord(encodingStyle)).orElse(Component.literal(word).withStyle(encodingStyle));
    }
    
    public static class Entry {
        public static final Codec<Entry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.STRING.fieldOf("translationKey").forGetter(Entry::getTranslationKey), 
                Style.FORMATTING_CODEC.optionalFieldOf("style", Style.EMPTY).forGetter(Entry::getStyle)
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
        
        public boolean matches(String word) {
            return word.equals(StringDecomposer.getPlainText(Component.translatable(this.translationKey)));
        }
        
        public Component getStylizedWord() {
            return Component.translatable(this.translationKey).withStyle(this.style);
        }
        
        public Component getStylizedWord(Style encodingStyle) {
            return Component.translatable(this.translationKey).withStyle(encodingStyle.applyTo(this.style));
        }
        
        public static class Builder {
            private final StyleGuide.Builder parent;
            private final String translationKey;
            private Style style = Style.EMPTY;
            
            protected Builder(String translationKey, StyleGuide.Builder parent) {
                this.parent = parent;
                this.translationKey = translationKey;
            }
            
            public Builder setStyle(Style newStyle) {
                this.style = newStyle;
                return this;
            }
            
            public Builder mergeStyle(Style newStyle) {
                this.style = newStyle.applyTo(this.style);
                return this;
            }
            
            public StyleGuide.Builder end() {
                this.parent.addEntry(new Entry(this.translationKey, this.style));
                return this.parent;
            }
        }
    }
    
    public static class Builder {
        protected final ResourceLocation langId;
        protected final List<Entry> entries = new ArrayList<>();
        
        protected Builder(ResourceLocation langId) {
            this.langId = langId;
        }
        
        protected void addEntry(Entry newEntry) {
            this.entries.add(newEntry);
        }
        
        public StyleGuide.Entry.Builder entry(String translationKey) {
            return new StyleGuide.Entry.Builder(translationKey, this);
        }
        
        public StyleGuide build() {
            return new StyleGuide(this.entries);
        }
        
        public void save(BiConsumer<ResourceLocation, StyleGuide> consumer) {
            consumer.accept(this.langId, this.build());
        }
    }
}
