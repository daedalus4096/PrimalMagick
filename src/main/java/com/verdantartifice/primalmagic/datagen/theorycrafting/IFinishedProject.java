package com.verdantartifice.primalmagic.datagen.theorycrafting;

import com.google.gson.JsonObject;

import net.minecraft.resources.ResourceLocation;

public interface IFinishedProject {
    ResourceLocation getId();
    
    void serialize(JsonObject json);

    default JsonObject getProjectJson() {
        JsonObject json = new JsonObject();
        this.serialize(json);
        return json;
    }
}
