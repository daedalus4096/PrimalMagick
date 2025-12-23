package com.verdantartifice.primalmagick.common.books.grids;

import com.verdantartifice.primalmagick.common.books.LinguisticsManager;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Resource reload listener for linguistics grid definition data.
 * 
 * @author Daedalus4096
 */
public class GridDefinitionLoader extends SimpleJsonResourceReloadListener<GridDefinition> {
    private static final Logger LOGGER = LogManager.getLogger();
    
    private static GridDefinitionLoader INSTANCE;
    
    protected GridDefinitionLoader() {
        super(GridDefinition.codec(), FileToIdConverter.json("linguistics_grids"));
    }

    public static GridDefinitionLoader getOrCreateInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GridDefinitionLoader();
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
    protected void apply(@NotNull Map<ResourceLocation, GridDefinition> pObject, @NotNull ResourceManager pResourceManager, @NotNull ProfilerFiller pProfiler) {
        LinguisticsManager.clearAllGridDefinitions();
        for (Map.Entry<ResourceLocation, GridDefinition> entry : pObject.entrySet()) {
            if (entry.getValue() == null || !LinguisticsManager.registerGridDefinition(entry.getKey(), entry.getValue())) {
                LOGGER.error("Failed to register linguistics grid definition {}", entry.getKey());
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
