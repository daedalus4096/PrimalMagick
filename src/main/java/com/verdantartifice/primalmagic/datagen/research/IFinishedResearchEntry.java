package com.verdantartifice.primalmagic.datagen.research;

import com.google.gson.JsonObject;

import net.minecraft.util.ResourceLocation;

public interface IFinishedResearchEntry {
    ResourceLocation getId();
    
    void serialize(JsonObject json);
    
    default JsonObject getEntryJson() {
        JsonObject json = new JsonObject();
        this.serialize(json);
        return json;
    }
}
