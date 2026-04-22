package com.verdantartifice.primalmagick.client.books;

import net.minecraft.resources.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Container for mod style guides.
 * 
 * @author Daedalus4096
 */
public class StyleGuideManager {
    protected static final Map<Identifier, StyleGuide> REGISTRY = new HashMap<>();
    private static final Logger LOGGER = LogManager.getLogger();
    
    public static void clearStyleGuides() {
        REGISTRY.clear();
    }

    public static void setStyleGuide(Identifier id, StyleGuide guide) {
        if (REGISTRY.containsKey(id)) {
            LOGGER.warn("Style guide for language {} is already registered with {} entries", id.toString(), guide.size());
        } else {
            REGISTRY.put(id, guide);
        }
    }
    
    public static Optional<StyleGuide> getStyleGuide(Identifier id) {
        return Optional.ofNullable(REGISTRY.get(id));
    }
    
    public static Collection<StyleGuide> getAllStyleGuides() {
        return Collections.unmodifiableCollection(REGISTRY.values());
    }
}
