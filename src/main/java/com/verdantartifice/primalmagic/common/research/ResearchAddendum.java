package com.verdantartifice.primalmagic.common.research;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ResearchAddendum {
    protected String textTranslationKey;
    
    protected ResearchAddendum(@Nonnull String textTranslationKey) {
        this.textTranslationKey = textTranslationKey;
    }
    
    @Nullable
    public static ResearchAddendum create(@Nullable String textTranslationKey) {
        return (textTranslationKey == null) ? null : new ResearchAddendum(textTranslationKey);
    }
    
    @Nonnull
    public String getTextTranslationKey() {
        return this.textTranslationKey;
    }
}
