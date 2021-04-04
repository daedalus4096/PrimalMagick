package com.verdantartifice.primalmagic.datagen.research;

import javax.annotation.Nonnull;

import com.google.gson.JsonObject;

public interface IFinishedResearchAddendum {
    IFinishedResearchAddendum setEntryKey(@Nonnull String key);
    
    IFinishedResearchAddendum setAddendumIndex(int index);
    
    void serialize(JsonObject json);
    
    default JsonObject getAddendumJson() {
        JsonObject json = new JsonObject();
        this.serialize(json);
        return json;
    }
}
