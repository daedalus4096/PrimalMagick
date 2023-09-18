package com.verdantartifice.primalmagick.client.tips;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.resources.ResourceLocation;

/**
 * Container for mod tip definitions.
 * 
 * @author Daedalus4096
 */
public class TipManager {
    protected static final Map<ResourceLocation, TipDefinition> REGISTRY = new HashMap<>();
    private static final Logger LOGGER = LogManager.getLogger();

    public static void setTipDefinition(ResourceLocation id, TipDefinition tip) {
        if (REGISTRY.containsKey(id)) {
            LOGGER.error("Tip definition {} is already registered", id.toString());
        } else {
            REGISTRY.put(id, tip);
        }
    }
    
    public static Optional<TipDefinition> getTipDefinition(ResourceLocation id) {
        return Optional.ofNullable(REGISTRY.get(id));
    }
    
    public static Collection<TipDefinition> getAllTipDefinitions() {
        return Collections.unmodifiableCollection(REGISTRY.values());
    }
}
