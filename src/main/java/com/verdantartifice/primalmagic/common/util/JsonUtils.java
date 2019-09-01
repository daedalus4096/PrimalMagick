package com.verdantartifice.primalmagic.common.util;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class JsonUtils {
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
    
    @Nonnull
    public static List<Object> toOres(@Nonnull JsonArray jsonArray) {
        List<Object> retVal = new ArrayList<>();
        for (JsonElement element : jsonArray) {
            try {
                String str = element.getAsString();
                str = str.replace("'", "\"");
                if (str.startsWith("tag:")) {
                    String[] tokens = str.split(":", 2);
                    if (tokens.length > 1 && !tokens[1].isEmpty()) {
                        retVal.add(new ResourceLocation(tokens[1]));
                    }
                } else {
                    ItemStack stack = ItemUtils.parseItemStack(str);
                    if (stack != null && !stack.isEmpty()) {
                        retVal.add(stack);
                    }
                }
            } catch (Exception e) {}
        }
        return retVal;
    }
}
