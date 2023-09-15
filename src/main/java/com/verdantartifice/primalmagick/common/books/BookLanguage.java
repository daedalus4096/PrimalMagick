package com.verdantartifice.primalmagick.common.books;

import java.util.function.Function;

import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

/**
 * Definition for a language in which a static book can be written/encoded.
 * 
 * @author Daedalus4096
 */
public record BookLanguage(ResourceLocation languageId, Style style, int complexity, TagKey<BookDefinition> booksTag) {
    private static final Function<BookLanguage, String> MEMOIZED_DESCRIPTION_ID = Util.memoize(BookLanguage::getDescriptionIdInner);
    
    public boolean isComplex() {
        return this.complexity() > 0;
    }
    
    public boolean isTranslatable() {
        return this.complexity() >= 0;
    }
    
    public String getDescriptionId() {
        return MEMOIZED_DESCRIPTION_ID.apply(this);
    }
    
    private static String getDescriptionIdInner(BookLanguage lang) {
        return Util.makeDescriptionId("written_language", BookLanguagesPM.LANGUAGES.get().getKey(lang));
    }
    
    public MutableComponent getName() {
        return Component.translatable(this.getDescriptionId());
    }
}
