package com.verdantartifice.primalmagick.common.runes;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.verdantartifice.primalmagick.PrimalMagick;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid=PrimalMagick.MODID)
public class RuneEnchantmentDefinitionLoader extends SimpleJsonResourceReloadListener {
    protected static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
    private static final Logger LOGGER = LogManager.getLogger();
    
    private static RuneEnchantmentDefinitionLoader INSTANCE;
    
    protected RuneEnchantmentDefinitionLoader() {
        super(GSON, "rune_enchantments");
    }

    @SubscribeEvent
    public static void onResourceReload(AddReloadListenerEvent event) {
        event.addListener(createInstance());
    }
    
    public static RuneEnchantmentDefinitionLoader createInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RuneEnchantmentDefinitionLoader();
        }
        return INSTANCE;
    }
    
    public static RuneEnchantmentDefinitionLoader getInstance() {
        if (INSTANCE == null) {
            throw new IllegalStateException("Cannot retrieve RuneEnchantmentDefinitionLoader until resources are loaded at least once");
        } else {
            return INSTANCE;
        }
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> pObject, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
        RuneManager.clearAllRuneEnchantments();
        for (Map.Entry<ResourceLocation, JsonElement> entry : pObject.entrySet()) {
            ResourceLocation location = entry.getKey();
            if (location.getPath().startsWith("_")) {
                // Filter anything beginning with "_" as it's used for metadata.
                continue;
            }

            try {
                RuneEnchantmentDefinition def = RuneManager.DEFINITION_SERIALIZER.read(location, GsonHelper.convertToJsonObject(entry.getValue(), "top member"));
                if (def == null || !RuneManager.registerRuneEnchantment(def)) {
                    LOGGER.error("Failed loading rune enchantment definition {}", location);
                }
            } catch (Exception e) {
                LOGGER.error("Parsing failure loading rune enchantment definition {}", location, e);
            }
        }
        LOGGER.info("Loaded {} rune enchantment definitions", RuneManager.getAllDefinitions().size());
    }

    public void replaceRuneEnchantments(Map<ResourceLocation, RuneEnchantmentDefinition> definitions) {
        RuneManager.clearAllRuneEnchantments();
        for (Map.Entry<ResourceLocation, RuneEnchantmentDefinition> entry : definitions.entrySet()) {
            if (entry.getValue() == null || !RuneManager.registerRuneEnchantment(entry.getValue())) {
                LOGGER.error("Failed to update rune enchantment definition {}", entry.getKey());
            }
        }
        LOGGER.info("Updated {} rune enchantment definitions", RuneManager.getAllDefinitions().size());
    }
}
