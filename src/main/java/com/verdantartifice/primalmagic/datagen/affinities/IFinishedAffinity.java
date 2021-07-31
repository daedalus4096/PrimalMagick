package com.verdantartifice.primalmagic.datagen.affinities;

import com.google.gson.JsonObject;
import com.verdantartifice.primalmagic.common.affinities.AffinityType;

import net.minecraft.resources.ResourceLocation;

public interface IFinishedAffinity {
    AffinityType getType();
    
    ResourceLocation getId();
    
    void serialize(JsonObject json);
    
    default JsonObject getAffinityJson() {
        JsonObject json = new JsonObject();
        json.addProperty("type", this.getType().getSerializedName());
        this.serialize(json);
        return json;
    }
}
