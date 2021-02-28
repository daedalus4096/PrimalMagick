package com.verdantartifice.primalmagic.common.affinities;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.verdantartifice.primalmagic.PrimalMagic;

import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

public class AffinityController extends JsonReloadListener {
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
    private static final Map<AffinityType, IAffinitySerializer<?>> SERIALIZERS = new ImmutableMap.Builder<AffinityType, IAffinitySerializer<?>>()
            .put(AffinityType.ITEM, ItemAffinity.SERIALIZER)
            .build();
    
    private Map<AffinityType, Map<ResourceLocation, IAffinity>> affinities = ImmutableMap.of();

    public AffinityController() {
        super(GSON, "affinities");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> objectIn, IResourceManager resourceManagerIn, IProfiler profilerIn) {
        Map<AffinityType, ImmutableMap.Builder<ResourceLocation, IAffinity>> map = new HashMap<>();
        
        for (Map.Entry<ResourceLocation, JsonElement> entry : objectIn.entrySet()) {
            ResourceLocation location = entry.getKey();
            if (location.getPath().startsWith("_")) {
                // Filter anything beginning with "_" as it's used for metadata.
                continue;
            }

            try {
                IAffinity aff = this.deserializeAffinity(location, JSONUtils.getJsonObject(entry.getValue(), "top member"));
                if (aff == null) {
                    PrimalMagic.LOGGER.info("Skipping loading affinity {} as its serializer returned null", location);
                    continue;
                }
                map.computeIfAbsent(aff.getType(), (affinityType) -> {
                    return ImmutableMap.builder();
                }).put(location, aff);
            } catch (IllegalArgumentException | JsonParseException e) {
                PrimalMagic.LOGGER.error("Parsing error loading affinity {}", location, e);
            }
        }
        
        this.affinities = map.entrySet().stream().collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, (affinityEntry) -> {
            return affinityEntry.getValue().build();
        }));
        PrimalMagic.LOGGER.info("Loaded {} affinity definitions", map.size());
    }
    
    protected IAffinity deserializeAffinity(ResourceLocation id, JsonObject json) {
        String s = JSONUtils.getString(json, "type");
        AffinityType type = AffinityType.parse(s);
        IAffinitySerializer<?> serializer = SERIALIZERS.get(type);
        if (serializer == null) {
            throw new JsonSyntaxException("Invalid or unsupported affinity type '" + s + "'");
        } else {
            return serializer.read(id, json);
        }
    }
}
