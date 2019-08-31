package com.verdantartifice.primalmagic.common.util;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import net.minecraft.util.ResourceLocation;

public class JsonUtils {
    public static List<ResourceLocation> toResourceLocations(JsonArray jsonArray) {
        List<ResourceLocation> retVal = new ArrayList<>();
        for (JsonElement element : jsonArray) {
            try {
                retVal.add(new ResourceLocation(element.getAsString()));
            } catch (Exception e) {}
        }
        return retVal;
    }
}
