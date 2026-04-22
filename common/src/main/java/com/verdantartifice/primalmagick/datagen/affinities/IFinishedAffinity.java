package com.verdantartifice.primalmagick.datagen.affinities;

import com.google.gson.JsonObject;
import com.verdantartifice.primalmagick.common.affinities.AffinityType;
import net.minecraft.resources.Identifier;

public interface IFinishedAffinity {
    AffinityType<?> getType();
    
    Identifier getId();
    
    void serialize(JsonObject json);
    
    default JsonObject getAffinityJson() {
        JsonObject json = new JsonObject();
        json.addProperty("type", this.getType().id().toString());
        this.serialize(json);
        return json;
    }
}
