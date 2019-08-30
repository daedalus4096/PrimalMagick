package com.verdantartifice.primalmagic.common.research;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.gson.JsonObject;

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
    public static ResearchAddendum parse(JsonObject obj) throws Exception {
        ResearchAddendum addendum = create(obj.getAsJsonPrimitive("text").getAsString());
        if (addendum == null) {
            throw new Exception("Illegal addendum text in research JSON");
        }
        return addendum;
    }
    
    @Nonnull
    public String getTextTranslationKey() {
        return this.textTranslationKey;
    }
}
