package com.verdantartifice.primalmagic.datagen;

import com.google.gson.JsonObject;

public interface IFinishedResearchStage {
    void serialize(JsonObject json);
    
    default JsonObject getStageJson() {
        JsonObject json = new JsonObject();
        this.serialize(json);
        return json;
    }
}
