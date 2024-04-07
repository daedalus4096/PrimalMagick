package com.verdantartifice.primalmagick.common.research.topics;

import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.common.books.BookLanguage;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

/**
 * Research topic that points to a linguistics entry in the Grimoire.
 * 
 * @author Daedalus4096
 */
public class LanguageResearchTopic extends AbstractResearchTopic {
    public LanguageResearchTopic(ResourceKey<BookLanguage> language, int page) {
        super(AbstractResearchTopic.Type.LANGUAGE, language.location().toString(), page);
    }
    
    @Nullable
    public ResourceKey<BookLanguage> getData() {
        return ResourceKey.create(RegistryKeysPM.BOOK_LANGUAGES, new ResourceLocation(this.data));
    }
}
