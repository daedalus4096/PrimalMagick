package com.verdantartifice.primalmagick.client.tips;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

/**
 * Resource loader for client tip definitions.
 * 
 * @author Daedalus4096
 */
public class TipLoader extends SimpleJsonResourceReloadListener {
    protected static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
    private static final Logger LOGGER = LogManager.getLogger();
    
    private static TipLoader INSTANCE;
    
    protected TipLoader() {
        super(GSON, "tips");
    }
    
    public static TipLoader getInstance() {
        if (INSTANCE == null) {
            throw new IllegalStateException("Cannot retrieve TipLoader until resources are loaded at least once");
        } else {
            return INSTANCE;
        }
    }
    
    public static TipLoader getOrCreateInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TipLoader();
        }
        return INSTANCE;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> pObject, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
        // Load tip definitions explicitly defined in resource packs
        TipManager.clearTips();
        pObject.entrySet().forEach(entry -> {
            ResourceLocation location = entry.getKey();
            // Filter anything beginning with "_" as it's used for metadata.
            if (!location.getPath().startsWith("_")) {
                TipDefinition.CODEC.parse(JsonOps.INSTANCE, entry.getValue())
                        .resultOrPartial(LOGGER::error)
                        .ifPresent(tipDef -> TipManager.setTipDefinition(location, tipDef));
            }
        });

        LOGGER.info("Loaded {} tips", TipManager.getAllTipDefinitions().size());
    }
}
