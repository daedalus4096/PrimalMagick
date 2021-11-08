package com.verdantartifice.primalmagick.datagen.research;

import javax.annotation.Nonnull;

import com.google.gson.JsonObject;

public interface IFinishedResearchStage {
    IFinishedResearchStage setEntryKey(@Nonnull String key);
    
    IFinishedResearchStage setStageIndex(int index);
    
    void serialize(JsonObject json);
    
    default JsonObject getStageJson() {
        JsonObject json = new JsonObject();
        this.serialize(json);
        return json;
    }
}
