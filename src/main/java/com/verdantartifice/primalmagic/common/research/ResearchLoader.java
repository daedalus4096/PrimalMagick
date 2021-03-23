package com.verdantartifice.primalmagic.common.research;

import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.verdantartifice.primalmagic.PrimalMagic;

import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid=PrimalMagic.MODID)
public class ResearchLoader extends JsonReloadListener {
    protected static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
    
    private static ResearchLoader INSTANCE;
    
    protected ResearchLoader() {
        super(GSON, "grimoire");
    }
    
    @SubscribeEvent
    public static void onResourceReload(AddReloadListenerEvent event) {
        INSTANCE = new ResearchLoader();
        event.addListener(INSTANCE);
    }
    
    public static ResearchLoader getInstance() {
        if (INSTANCE == null) {
            throw new IllegalStateException("Cannot retrieve ResearchLoader until resources are loaded at least once");
        } else {
            return INSTANCE;
        }
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> objectIn, IResourceManager resourceManagerIn, IProfiler profilerIn) {
        ResearchManager.clearCraftingReferences();
        ResearchDisciplines.clearAllResearch();
        for (Map.Entry<ResourceLocation, JsonElement> entry : objectIn.entrySet()) {
            ResourceLocation location = entry.getKey();
            if (location.getPath().startsWith("_")) {
                // Filter anything beginning with "_" as it's used for metadata.
                continue;
            }

            try {
                ResearchEntry researchEntry = ResearchEntry.parse(JSONUtils.getJsonObject(entry.getValue(), "top member"));
                ResearchDiscipline discipline = ResearchDisciplines.getDiscipline(researchEntry.getDisciplineKey());
                if (discipline == null || !discipline.addEntry(researchEntry)) {
                    PrimalMagic.LOGGER.error("Could not add invalid entry: {}", location);
                }
            } catch (Exception e) {
                PrimalMagic.LOGGER.error("Parsing error loading research entry {}", location, e);
            }
        }
        for (ResearchDiscipline discipline : ResearchDisciplines.getAllDisciplines()) {
            if (!discipline.getEntries().isEmpty()) {
                PrimalMagic.LOGGER.info("Loaded {} research entries for discipline {}", discipline.getEntries().size(), discipline.getKey().toLowerCase());
            }
        }
    }
}
