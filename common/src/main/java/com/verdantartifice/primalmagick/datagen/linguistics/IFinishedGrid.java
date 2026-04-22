package com.verdantartifice.primalmagick.datagen.linguistics;

import com.google.gson.JsonObject;
import net.minecraft.resources.Identifier;

public interface IFinishedGrid {
    Identifier getId();
    
    void serialize(JsonObject json);

    default JsonObject getGridJson() {
        JsonObject json = new JsonObject();
        this.serialize(json);
        return json;
    }
}
