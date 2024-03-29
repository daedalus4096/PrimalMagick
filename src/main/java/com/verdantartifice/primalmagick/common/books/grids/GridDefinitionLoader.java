package com.verdantartifice.primalmagick.common.books.grids;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.books.LinguisticsManager;
import com.verdantartifice.primalmagick.common.books.grids.rewards.AttunementReward;
import com.verdantartifice.primalmagick.common.books.grids.rewards.ComprehensionReward;
import com.verdantartifice.primalmagick.common.books.grids.rewards.EmptyReward;
import com.verdantartifice.primalmagick.common.books.grids.rewards.KnowledgeReward;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Resource reload listener for linguistics grid definition data.
 * 
 * @author Daedalus4096
 */
@Mod.EventBusSubscriber(modid=PrimalMagick.MODID)
public class GridDefinitionLoader extends SimpleJsonResourceReloadListener {
    protected static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
    private static final Logger LOGGER = LogManager.getLogger();
    
    private static GridDefinitionLoader INSTANCE;
    
    protected GridDefinitionLoader() {
        super(GSON, "linguistics_grids");
    }

    @SubscribeEvent
    public static void onResourceReload(AddReloadListenerEvent event) {
        event.addListener(createInstance());
    }
    
    public static GridDefinitionLoader createInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GridDefinitionLoader();
            
            // Initialize reward serialization mappings while we're at it, as the classloader requires it to happen somewhere
            EmptyReward.init();
            AttunementReward.init();
            ComprehensionReward.init();
            KnowledgeReward.init();
        }
        return INSTANCE;
    }
    
    public static GridDefinitionLoader getInstance() {
        if (INSTANCE == null) {
            throw new IllegalStateException("Cannot retrieve GridDefinitionLoader until resources are loaded at least once");
        } else {
            return INSTANCE;
        }
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> pObject, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
        LinguisticsManager.clearAllGridDefinitions();
        for (Map.Entry<ResourceLocation, JsonElement> entry : pObject.entrySet()) {
            ResourceLocation location = entry.getKey();
            if (location.getPath().startsWith("_")) {
                // Filter anything beginning with "_" as it's used for metadata.
                continue;
            }

            try {
                // Instantiate grid definition from serializer, then attempt to register it
                GridDefinition gridDef = GridDefinition.SERIALIZER.read(location, GsonHelper.convertToJsonObject(entry.getValue(), "top member"));
                if (gridDef == null || !LinguisticsManager.registerGridDefinition(location, gridDef)) {
                    LOGGER.error("Failed to register linguistics grid definition {}", location);
                }
            } catch (Exception e) {
                LOGGER.error("Parsing error loading linguistics grid definition {}", location, e);
            }
        }
        LOGGER.info("Loaded {} linguistics grid definitions", LinguisticsManager.getAllGridDefinitions().size());
    }

    public void replaceGridDefinitions(Map<ResourceLocation, GridDefinition> gridDefinitions) {
        LinguisticsManager.clearAllGridDefinitions();
        for (Map.Entry<ResourceLocation, GridDefinition> entry : gridDefinitions.entrySet()) {
            if (entry.getValue() == null || !LinguisticsManager.registerGridDefinition(entry.getKey(), entry.getValue())) {
                LOGGER.error("Failed to update linguistics grid {}", entry.getKey());
            }
        }
        LOGGER.info("Updated {} linguistics grid definitions", LinguisticsManager.getAllGridDefinitions().size());
    }
}
