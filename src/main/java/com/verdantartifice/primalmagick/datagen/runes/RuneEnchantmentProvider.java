package com.verdantartifice.primalmagick.datagen.runes;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonObject;
import com.verdantartifice.primalmagick.common.enchantments.EnchantmentsPM;
import com.verdantartifice.primalmagick.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagick.common.runes.Rune;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantments;

public class RuneEnchantmentProvider implements DataProvider {
    private static final Logger LOGGER = LogManager.getLogger();
    protected final DataGenerator generator;

    public RuneEnchantmentProvider(DataGenerator generator) {
        this.generator = generator;
    }

    @Override
    public void run(CachedOutput cache) throws IOException {
        Path path = this.generator.getOutputFolder();
        Map<ResourceLocation, IFinishedRuneEnchantment> map = new HashMap<>();
        this.registerEnchantments((enchantment) -> {
            if (map.put(enchantment.getId(), enchantment) != null) {
                LOGGER.debug("Duplicate rune enchantment definition in data generation: {}", enchantment.getId().toString());
            }
        });
        for (Map.Entry<ResourceLocation, IFinishedRuneEnchantment> entry : map.entrySet()) {
            this.saveProject(cache, entry.getValue().getEnchantmentJson(), path.resolve("data/" + entry.getKey().getNamespace() + "/rune_enchantments/" + entry.getKey().getPath() + ".json"));
        }
    }
    
    private void saveProject(CachedOutput cache, JsonObject json, Path path) {
        try {
            DataProvider.saveStable(cache, json, path);
        } catch (IOException e) {
            LOGGER.error("Couldn't save rune enchantment definition {}", path, e);
        }
    }
    
    protected void registerEnchantments(Consumer<IFinishedRuneEnchantment> consumer) {
        // Register rune combinations for vanilla enchantments
        RuneEnchantmentBuilder.enchantment(Enchantments.ALL_DAMAGE_PROTECTION).verb(Rune.PROTECT).noun(Rune.SELF).source(Rune.EARTH).build(consumer);
        
        // Register rune combinations for mod enchantments
        RuneEnchantmentBuilder.enchantment(EnchantmentsPM.REVERBERATION.get()).verb(Rune.PROJECT).noun(Rune.AREA).source(Rune.EARTH)
            .requiredResearch(CompoundResearchKey.from(true, "MASTER_RUNEWORKING", "PRIMAL_SHOVEL", "RUNE_PROJECT", "RUNE_AREA", "RUNE_EARTH")).build(consumer);
    }

    @Override
    public String getName() {
        return "Primal Magick Rune Enchantment Definitions";
    }
}
