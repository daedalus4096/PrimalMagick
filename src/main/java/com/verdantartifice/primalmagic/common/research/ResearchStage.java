package com.verdantartifice.primalmagic.common.research;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.gson.JsonObject;

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
    public static ResearchStage parse(JsonObject obj) throws Exception {
        ResearchStage stage = create(obj.getAsJsonPrimitive("text").getAsString());
        if (stage == null) {
            throw new Exception("Illegal stage text in research JSON");
        }
        return stage;
    }
    
    @Nonnull
    public String getTextTranslationKey() {
        return this.textTranslationKey;
    }
}
