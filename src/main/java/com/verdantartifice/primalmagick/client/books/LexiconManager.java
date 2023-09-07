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
 * Container for mod lexicons.
 * 
 * @author Daedalus4096
 */
public class LexiconManager {
    protected static final Map<ResourceLocation, Lexicon> REGISTRY = new HashMap<>();
    private static final Logger LOGGER = LogManager.getLogger();
    
    public static void clear() {
        REGISTRY.clear();
    }
    
    public static void register(ResourceLocation id, Lexicon lexicon) {
        if (REGISTRY.containsKey(id)) {
            throw new IllegalStateException("Duplicate lexicon for " + id.toString() + " is not allowed!");
        } else {
            REGISTRY.put(id, lexicon);
            LOGGER.info("Registered {} lexicon with {} entries", id.toString(), lexicon.size());
        }
    }
    
    public static Optional<Lexicon> getLexicon(ResourceLocation id) {
        return Optional.ofNullable(REGISTRY.get(id));
    }
    
    public static Collection<Lexicon> getAllLexicons() {
        return Collections.unmodifiableCollection(REGISTRY.values());
    }
}
