package com.verdantartifice.primalmagick.datagen.linguistics;

import com.google.gson.JsonObject;

import net.minecraft.resources.ResourceLocation;

public interface IFinishedGrid {
    ResourceLocation getId();
    
    void serialize(JsonObject json);

    default JsonObject getGridJson() {
        JsonObject json = new JsonObject();
        this.serialize(json);
        return json;
    }
}
