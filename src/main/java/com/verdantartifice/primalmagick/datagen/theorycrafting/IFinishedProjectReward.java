package com.verdantartifice.primalmagick.datagen.theorycrafting;

import com.google.gson.JsonObject;

public interface IFinishedProjectReward {
    void serialize(JsonObject json);
    
    default JsonObject getRewardJson() {
        JsonObject json = new JsonObject();
        this.serialize(json);
        return json;
    }
}
