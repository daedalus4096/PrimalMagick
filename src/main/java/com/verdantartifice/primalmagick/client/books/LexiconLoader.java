package com.verdantartifice.primalmagick.client.books;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;

/**
 * Resource loader for client lexicons.  Reads datapack-provided JSON files as well as parses lang assets
 * for book languages.
 * 
 * @author Daedalus4096
 */
public class LexiconLoader extends SimpleJsonResourceReloadListener {
    protected static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
    private static final Logger LOGGER = LogManager.getLogger();
    
    private static LexiconLoader INSTANCE;
    
    protected LexiconLoader() {
        super(GSON, "lexicons");
    }
    
    public static LexiconLoader getInstance() {
        if (INSTANCE == null) {
            throw new IllegalStateException("Cannot retrieve LexiconLoader until resources are loaded at least once");
        } else {
            return INSTANCE;
        }
    }
    
    public static LexiconLoader getOrCreateInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LexiconLoader();
        }
        return INSTANCE;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> pObject, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
        LexiconManager.clear();
        
        // Load lexicons explicitly defined in resource packs
        pObject.entrySet().forEach(entry -> {
            ResourceLocation location = entry.getKey();
            // Filter anything beginning with "_" as it's used for metadata.
            if (!location.getPath().startsWith("_")) {
                try {
                    LexiconManager.register(location, Lexicon.parse(GsonHelper.convertToJsonObject(entry.getValue(), "top member")));
                } catch (Exception e) {
                    LOGGER.error("Parsing error loading lexicon {}", location, e);
                }
            }
        });
        
        // TODO Create lexicons from registered book languages
        
        LOGGER.info("Loaded {} lexicons", LexiconManager.getAllLexicons().size());
    }

}
