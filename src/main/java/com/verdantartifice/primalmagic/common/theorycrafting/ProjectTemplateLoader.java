package com.verdantartifice.primalmagic.common.theorycrafting;

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
public class ProjectTemplateLoader extends SimpleJsonResourceReloadListener {
    protected static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
    private static final Logger LOGGER = LogManager.getLogger();
    
    private static ProjectTemplateLoader INSTANCE;
    
    protected ProjectTemplateLoader() {
        super(GSON, "theorycrafting");
    }

    @SubscribeEvent
    public static void onResourceReload(AddReloadListenerEvent event) {
        event.addListener(createInstance());
    }
    
    public static ProjectTemplateLoader createInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ProjectTemplateLoader();
        }
        return INSTANCE;
    }
    
    public static ProjectTemplateLoader getInstance() {
        if (INSTANCE == null) {
            throw new IllegalStateException("Cannot retrieve ProjectTemplateLoader until resources are loaded at least once");
        } else {
            return INSTANCE;
        }
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> objectIn, ResourceManager resourceManagerIn, ProfilerFiller profilerIn) {
        TheorycraftManager.clearAllTemplates();
        for (Map.Entry<ResourceLocation, JsonElement> entry : objectIn.entrySet()) {
            ResourceLocation location = entry.getKey();
            if (location.getPath().startsWith("_")) {
                // Filter anything beginning with "_" as it's used for metadata.
                continue;
            }

            try {
                ProjectTemplate template = TheorycraftManager.TEMPLATE_SERIALIZER.read(location, GsonHelper.convertToJsonObject(entry.getValue(), "top member"));
                if (template == null || !TheorycraftManager.registerTemplate(location, template)) {
                    LOGGER.error("Failed to register theorycrafting project template {}", location);
                }
            } catch (Exception e) {
                LOGGER.error("Parsing error loading theorycrafting project template {}", location, e);
            }
        }
        LOGGER.info("Loaded {} theorycrafting project templates", TheorycraftManager.getAllTemplates().size());
    }
    
    public void replaceTemplates(Map<ResourceLocation, ProjectTemplate> templates) {
        TheorycraftManager.clearAllTemplates();
        for (Map.Entry<ResourceLocation, ProjectTemplate> entry : templates.entrySet()) {
            if (entry.getValue() == null || !TheorycraftManager.registerTemplate(entry.getKey(), entry.getValue())) {
                LOGGER.error("Failed to update theorycrafting project template {}", entry.getKey());
            }
        }
        LOGGER.info("Updated {} theorycrafting project templates", TheorycraftManager.getAllTemplates().size());
    }
}
