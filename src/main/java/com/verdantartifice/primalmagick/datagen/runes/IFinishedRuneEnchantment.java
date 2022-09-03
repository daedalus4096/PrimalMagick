package com.verdantartifice.primalmagick.datagen.runes;

import com.google.gson.JsonObject;

import net.minecraft.resources.ResourceLocation;

public interface IFinishedRuneEnchantment {
    ResourceLocation getId();
    
    void serialize(JsonObject json);

    default JsonObject getEnchantmentJson() {
        JsonObject json = new JsonObject();
        this.serialize(json);
        return json;
    }
}
