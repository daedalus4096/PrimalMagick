package com.verdantartifice.primalmagick.datagen.theorycrafting;

import com.google.gson.JsonObject;

public interface IFinishedProjectMaterial {
    void serialize(JsonObject json);
    
    default JsonObject getMaterialJson() {
        JsonObject json = new JsonObject();
        this.serialize(json);
        return json;
    }
}
