package com.verdantartifice.primalmagick.datagen.linguistics;

import java.util.OptionalInt;

import com.google.gson.JsonObject;

import net.minecraft.resources.ResourceLocation;

public interface IFinishedGridNodeReward {
    void serialize(JsonObject json);

    default JsonObject getRewardJson() {
        JsonObject json = new JsonObject();
        this.serialize(json);
        return json;
    }
    
    default OptionalInt getComprehensionPoints(ResourceLocation bookLanguageId) {
        return OptionalInt.empty();
    }
}
