package com.verdantartifice.primalmagick.datagen.linguistics;

import com.google.gson.JsonObject;

public interface IFinishedGridNode {
    void serialize(JsonObject json);

    default JsonObject getGridJson() {
        JsonObject json = new JsonObject();
        this.serialize(json);
        return json;
    }
}
