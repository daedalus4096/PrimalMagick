package com.verdantartifice.primalmagic.common.research;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ResearchStage {
    protected String textTranslationKey;
    
    protected ResearchStage(@Nonnull String textTranslationKey) {
        this.textTranslationKey = textTranslationKey;
    }
    
    @Nullable
    public static ResearchStage create(@Nullable String textTranslationKey) {
        return (textTranslationKey == null) ? null : new ResearchStage(textTranslationKey);
    }
    
    @Nonnull
    public String getTextTranslationKey() {
        return this.textTranslationKey;
    }
}
