package com.verdantartifice.primalmagic.datagen;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.items.ItemsPM;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.sources.SourceList;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Items;
import net.minecraft.potion.Potions;
import net.minecraft.util.ResourceLocation;

public class AffinityProvider implements IDataProvider {
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
    protected final DataGenerator generator;
    
    public AffinityProvider(DataGenerator generator) {
        this.generator = generator;
    }

    @Override
    public void act(DirectoryCache cache) throws IOException {
        Path path = this.generator.getOutputFolder();
        Map<ResourceLocation, IFinishedAffinity> map = new HashMap<>();
        this.registerAffinities((affinity) -> {
            if (map.put(affinity.getId(), affinity) != null) {
                PrimalMagic.LOGGER.debug("Duplicate affinity in data generation: " + affinity.getId().toString());
            }
        });
        for (Map.Entry<ResourceLocation, IFinishedAffinity> entry : map.entrySet()) {
            IFinishedAffinity affinity = entry.getValue();
            this.saveAffinity(cache, affinity.getAffinityJson(), path.resolve("data/" + entry.getKey().getNamespace() + "/affinities/" + entry.getKey().getPath() + ".json"));
        }
    }
    
    private void saveAffinity(DirectoryCache cache, JsonObject json, Path path) {
        try {
            String jsonStr = GSON.toJson((JsonElement)json);
            String hash = HASH_FUNCTION.hashUnencodedChars(jsonStr).toString();
            if (!Objects.equals(cache.getPreviousHash(path), hash) || !Files.exists(path)) {
                Files.createDirectories(path.getParent());
                try (BufferedWriter writer = Files.newBufferedWriter(path)) {
                    writer.write(jsonStr);
                }
            }
            cache.recordHash(path, hash);
        } catch (IOException e) {
            PrimalMagic.LOGGER.error("Couldn't save affinity {}", path, e);
        }
    }
    
    protected void registerAffinities(Consumer<IFinishedAffinity> consumer) {
        SourceList auraUnit = new SourceList().add(Source.EARTH, 1).add(Source.SEA, 1).add(Source.SKY, 1).add(Source.SUN, 1).add(Source.MOON, 1);
        
        // Define vanilla affinities
        ItemAffinityBuilder.itemAffinity(Items.POTION).set(Source.SEA, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.ENCHANTED_BOOK).set(auraUnit.copy().multiply(5)).build(consumer);
        
        // Define mod affinities
        ItemAffinityBuilder.itemAffinity(ItemsPM.MARBLE_RAW.get()).set(Source.EARTH, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(ItemsPM.MARBLE_ENCHANTED.get()).base(ItemsPM.MARBLE_RAW.get()).add(auraUnit.copy()).build(consumer);
        
        // Define potion bonuses
        PotionBonusAffinityBuilder.potionBonusAffinity(Potions.NIGHT_VISION).bonus(Source.SUN, 2).build(consumer);
        
        // Define enchantment bonuses
        EnchantmentBonusAffinityBuilder.enchantmentBonusAffinity(Enchantments.PROTECTION).multiplier(Source.EARTH).build(consumer);
    }

    @Override
    public String getName() {
        return "Primal Magic Affinities";
    }
}
