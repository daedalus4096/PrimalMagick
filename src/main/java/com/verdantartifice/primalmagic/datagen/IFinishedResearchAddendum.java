package com.verdantartifice.primalmagic.datagen;

import com.google.gson.JsonObject;

public interface IFinishedResearchAddendum {
    void serialize(JsonObject json);
    
    default JsonObject getAddendumJson() {
        JsonObject json = new JsonObject();
        this.serialize(json);
        return json;
    }
}
