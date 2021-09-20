package com.verdantartifice.primalmagic.common.research;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.verdantartifice.primalmagic.PrimalMagic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid=PrimalMagic.MODID)
public class ResearchLoader extends SimpleJsonResourceReloadListener {
    protected static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
    private static final Logger LOGGER = LogManager.getLogger();

    private static ResearchLoader INSTANCE;
    
    protected ResearchLoader() {
        super(GSON, "grimoire");
    }
    
    @SubscribeEvent
    public static void onResourceReload(AddReloadListenerEvent event) {
        event.addListener(createInstance());
    }
    
    public static ResearchLoader createInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ResearchLoader();
        }
        return INSTANCE;
    }
    
    public static ResearchLoader getInstance() {
        if (INSTANCE == null) {
            throw new IllegalStateException("Cannot retrieve ResearchLoader until resources are loaded at least once");
        } else {
            return INSTANCE;
        }
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> objectIn, ResourceManager resourceManagerIn, ProfilerFiller profilerIn) {
        ResearchManager.clearCraftingReferences();
        ResearchManager.clearRecipeMap();
        ResearchDisciplines.clearAllResearch();
        for (Map.Entry<ResourceLocation, JsonElement> entry : objectIn.entrySet()) {
            ResourceLocation location = entry.getKey();
            if (location.getPath().startsWith("_")) {
                // Filter anything beginning with "_" as it's used for metadata.
                continue;
            }

            try {
                ResearchEntry researchEntry = ResearchEntry.parse(GsonHelper.convertToJsonObject(entry.getValue(), "top member"));
                ResearchDiscipline discipline = ResearchDisciplines.getDiscipline(researchEntry.getDisciplineKey());
                if (discipline == null || !discipline.addEntry(researchEntry)) {
                    LOGGER.error("Could not add invalid entry: {}", location);
                } else {
                    // Assemble derived data structures
                    researchEntry.getStages().forEach(s -> s.getRecipes().forEach(r -> ResearchManager.addRecipeMapping(r, researchEntry)));
                    researchEntry.getAddenda().forEach(a -> a.getRecipes().forEach(r -> ResearchManager.addRecipeMapping(r, researchEntry)));
                }
            } catch (Exception e) {
                LOGGER.error("Parsing error loading research entry {}", location, e);
            }
        }
        for (ResearchDiscipline discipline : ResearchDisciplines.getAllDisciplines()) {
            if (!discipline.getEntries().isEmpty()) {
                LOGGER.info("Loaded {} research entries for discipline {}", discipline.getEntries().size(), discipline.getKey().toLowerCase());
            }
        }
    }
    
    public void replaceResearch(List<ResearchEntry> entries) {
        ResearchManager.clearCraftingReferences();
        ResearchManager.clearRecipeMap();
        ResearchDisciplines.clearAllResearch();
        for (ResearchEntry researchEntry : entries) {
            ResearchDiscipline discipline = ResearchDisciplines.getDiscipline(researchEntry.getDisciplineKey());
            if (discipline == null || !discipline.addEntry(researchEntry)) {
                LOGGER.error("Could not update invalid research entry");
            } else {
                // Assemble derived data structures
                researchEntry.getStages().forEach(s -> {
                    s.getCraftReference().forEach(c -> ResearchManager.addCraftingReference(c));
                    s.getRecipes().forEach(r -> ResearchManager.addRecipeMapping(r, researchEntry));
                });
                researchEntry.getAddenda().forEach(a -> a.getRecipes().forEach(r -> ResearchManager.addRecipeMapping(r, researchEntry)));
            }
        }
        for (ResearchDiscipline discipline : ResearchDisciplines.getAllDisciplines()) {
            if (!discipline.getEntries().isEmpty()) {
                LOGGER.info("Updated {} research entries for discipline {}", discipline.getEntries().size(), discipline.getKey().toLowerCase());
            }
        }
    }
}
