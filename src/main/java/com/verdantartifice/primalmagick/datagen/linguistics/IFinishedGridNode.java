package com.verdantartifice.primalmagick.datagen.linguistics;

import org.joml.Vector2i;

import com.google.gson.JsonObject;

public interface IFinishedGridNode {
    Vector2i getPosition();
    
    void serialize(JsonObject json);

    default JsonObject getNodeJson() {
        JsonObject json = new JsonObject();
        this.serialize(json);
        return json;
    }
}
