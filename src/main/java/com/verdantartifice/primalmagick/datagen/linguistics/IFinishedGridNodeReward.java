package com.verdantartifice.primalmagick.datagen.linguistics;

import com.google.gson.JsonObject;

public interface IFinishedGridNodeReward {
    void serialize(JsonObject json);

    default JsonObject getRewardJson() {
        JsonObject json = new JsonObject();
        this.serialize(json);
        return json;
    }
}
