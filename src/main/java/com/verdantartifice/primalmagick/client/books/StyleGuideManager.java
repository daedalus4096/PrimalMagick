package com.verdantartifice.primalmagick.client.books;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.resources.ResourceLocation;

/**
 * Container for mod style guides.
 * 
 * @author Daedalus4096
 */
public class StyleGuideManager {
    protected static final Map<ResourceLocation, StyleGuide> REGISTRY = new HashMap<>();
    private static final Logger LOGGER = LogManager.getLogger();
    
    public static void clearStyleGuides() {
        REGISTRY.clear();
    }

    public static void setStyleGuide(ResourceLocation id, StyleGuide guide) {
        if (REGISTRY.containsKey(id)) {
            LOGGER.warn("Style guide for language {} is already registered with {} entries", id.toString(), guide.size());
        } else {
            REGISTRY.put(id, guide);
        }
    }
    
    public static Optional<StyleGuide> getStyleGuide(ResourceLocation id) {
        return Optional.ofNullable(REGISTRY.get(id));
    }
    
    public static Collection<StyleGuide> getAllStyleGuides() {
        return Collections.unmodifiableCollection(REGISTRY.values());
    }
}
