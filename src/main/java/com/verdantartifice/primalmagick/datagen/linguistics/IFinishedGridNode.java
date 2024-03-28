package com.verdantartifice.primalmagick.datagen.linguistics;

import org.joml.Vector2ic;

import com.google.gson.JsonObject;

public interface IFinishedGridNode {
    Vector2ic getPosition();
    
    IFinishedGridNodeReward getReward();
    
    void serialize(JsonObject json);

    default JsonObject getNodeJson() {
        JsonObject json = new JsonObject();
        this.serialize(json);
        return json;
    }
}
