package com.verdantartifice.primalmagick.common.util;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.verdantartifice.primalmagick.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.SourceList;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;

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
                retVal.add(new ResourceLocation(element.getAsString()));
            } catch (Exception e) {}
        }
        return retVal;
    }
    
    /**
     * Parse the given JSON array into a list of simple research keys, based on its elements' string
     * representations.
     * 
     * @param jsonArray the JSON array to be parsed
     * @return the list of deserialized simple research keys
     */
    @Nonnull
    public static List<SimpleResearchKey> toSimpleResearchKeys(@Nonnull JsonArray jsonArray) {
        List<SimpleResearchKey> retVal = new ArrayList<>();
        for (JsonElement element : jsonArray) {
            SimpleResearchKey key = SimpleResearchKey.parse(element.getAsString());
            if (key != null) {
                retVal.add(key);
            }
        }
        return retVal;
    }
    
    /**
     * Parse the given JSON array into a list of objects.  The type of each object depends on the
     * string representation of each element of the JSON array.  If the string starts with "tag:",
     * then the object will be a resource location for the named tag.  Otherwise, the method will
     * attempt to parse the string into an itemstack and add that to the return list instead.  If
     * no valid, non-empty item stack could be parsed, then no entry will be added to the return
     * list.
     * 
     * @param jsonArray the JSON array to be parsed
     * @return a list of resource locations and itemstacks
     */
    @Nonnull
    public static List<Object> toOres(@Nonnull JsonArray jsonArray) {
        List<Object> retVal = new ArrayList<>();
        for (JsonElement element : jsonArray) {
            try {
                String str = element.getAsString();
                str = str.replace("'", "\"");
                if (str.startsWith("tag:")) {
                    // If the string starts with a "tag:" prefix, create a resource location from the suffix
                    String[] tokens = str.split(":", 2);
                    if (tokens.length > 1 && !tokens[1].isEmpty()) {
                        retVal.add(new ResourceLocation(tokens[1]));
                    }
                } else {
                    // Otherwise, attempt to parse the string into an itemstack
                    ItemStack stack = ItemUtils.parseItemStack(str);
                    if (stack != null && !stack.isEmpty()) {
                        retVal.add(stack);
                    }
                }
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
        for (Source source : Source.SORTED_SOURCES) {
            retVal.with(source, GsonHelper.getAsInt(jsonObject, source.getTag(), 0));
        }
        return retVal.build();
    }
}
