package com.verdantartifice.primalmagick.client.books;

import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Resource loader for client style guides.
 * 
 * @author Daedalus4096
 */
public class StyleGuideLoader extends SimpleJsonResourceReloadListener<StyleGuide> {
    private static final Logger LOGGER = LogManager.getLogger();
    
    private static StyleGuideLoader INSTANCE;
    
    protected StyleGuideLoader() {
        super(StyleGuide.CODEC, FileToIdConverter.json("style_guides"));
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
    protected void apply(@NotNull Map<Identifier, StyleGuide> pObject, @NotNull ResourceManager pResourceManager, @NotNull ProfilerFiller pProfiler) {
        // Load style guides explicitly defined in resource packs
        StyleGuideManager.clearStyleGuides();
        pObject.forEach((location, guide) -> {
            if (guide == null) {
                LOGGER.error("Failed to load style guide {}", location);
            } else {
                StyleGuideManager.setStyleGuide(location, guide);
            }
        });

        LOGGER.info("Loaded {} style guides", StyleGuideManager.getAllStyleGuides().size());
    }

}
