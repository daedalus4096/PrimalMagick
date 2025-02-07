package com.verdantartifice.primalmagick.common.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.common.sources.Sources;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Collection of utility methods pertaining to JSON parsing.
 * 
 * @author Daedalus4096
 */
public class JsonUtils {
    /**
     * Parse the given JSON array into a list of its elements' string representations.
     * 
     * @param jsonArray the JSON array to be parsed
     * @return a list of string representations of the JSON array's elements
     */
    @Nonnull
    public static List<String> toStrings(@Nonnull JsonArray jsonArray) {
        List<String> retVal = new ArrayList<>();
        for (JsonElement element : jsonArray) {
            try {
                retVal.add(element.getAsString());
            } catch (Exception e) {}
        }
        return retVal;
    }
    
    /**
     * Parse the given JSON array into a list of resource locations, based on its elements' string
     * representations.
     * 
     * @param jsonArray the JSON array to be parsed
     * @return the list of deserialized resource locations 
     */
    @Nonnull
    public static List<ResourceLocation> toResourceLocations(@Nonnull JsonArray jsonArray) {
        List<ResourceLocation> retVal = new ArrayList<>();
        for (JsonElement element : jsonArray) {
            try {
                retVal.add(ResourceLocation.parse(element.getAsString()));
            } catch (Exception e) {}
        }
        return retVal;
    }
    
    /**
     * Parse the given JSON object into a source list.
     * 
     * @param jsonObject the JSON object to be parsed
     * @return a source list
     */
    @Nonnull
    public static SourceList toSourceList(@Nonnull JsonObject jsonObject) {
        SourceList.Builder retVal = SourceList.builder();
        for (Source source : Sources.getAllSorted()) {
            retVal.with(source, GsonHelper.getAsInt(jsonObject, source.getId().toString(), 0));
        }
        return retVal.build();
    }
}
