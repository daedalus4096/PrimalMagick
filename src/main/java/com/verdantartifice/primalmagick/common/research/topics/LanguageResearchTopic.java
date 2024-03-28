package com.verdantartifice.primalmagick.common.research.topics;

import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.common.books.BookLanguage;
import com.verdantartifice.primalmagick.common.books.BookLanguagesPM;

import net.minecraft.resources.ResourceLocation;

/**
 * Research topic that points to a linguistics entry in the Grimoire.
 * 
 * @author Daedalus4096
 */
public class LanguageResearchTopic extends AbstractResearchTopic {
    public LanguageResearchTopic(BookLanguage language, int page) {
        super(AbstractResearchTopic.Type.LANGUAGE, language.languageId().toString(), page);
    }
    
    @Nullable
    public BookLanguage getData() {
        return BookLanguagesPM.LANGUAGES.get().getValue(new ResourceLocation(this.data));
    }
}
