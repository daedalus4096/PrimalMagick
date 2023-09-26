package com.verdantartifice.primalmagick.client.books;

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
 * Resource loader for client style guides.
 * 
 * @author Daedalus4096
 */
public class StyleGuideLoader extends SimpleJsonResourceReloadListener {
    protected static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
    private static final Logger LOGGER = LogManager.getLogger();
    
    private static StyleGuideLoader INSTANCE;
    
    protected StyleGuideLoader() {
        super(GSON, "style_guides");
    }
    
    public static StyleGuideLoader getInstance() {
        if (INSTANCE == null) {
            throw new IllegalStateException("Cannot retrieve StyleGuideLoader until resources are loaded at least once");
        } else {
            return INSTANCE;
        }
    }
    
    public static StyleGuideLoader getOrCreateInstance() {
        if (INSTANCE == null) {
            INSTANCE = new StyleGuideLoader();
        }
        return INSTANCE;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> pObject, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
        // Load style guides explicitly defined in resource packs
        StyleGuideManager.clearStyleGuides();
        pObject.entrySet().forEach(entry -> {
            ResourceLocation location = entry.getKey();
            // Filter anything beginning with "_" as it's used for metadata.
            if (!location.getPath().startsWith("_")) {
                StyleGuide.CODEC.parse(JsonOps.INSTANCE, entry.getValue())
                        .resultOrPartial(LOGGER::error)
                        .ifPresent(styleGuide -> StyleGuideManager.setStyleGuide(location, styleGuide));
            }
        });

        LOGGER.info("Loaded {} style guides", StyleGuideManager.getAllStyleGuides().size());
    }

}
