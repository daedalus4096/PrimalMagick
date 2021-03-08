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
        
        // Define vanilla item affinities
        ItemAffinityBuilder.itemAffinity(Items.STONE).set(Source.EARTH, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.GRANITE).base(Items.STONE).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.POLISHED_GRANITE).base(Items.GRANITE).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.DIORITE).base(Items.STONE).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.POLISHED_DIORITE).base(Items.DIORITE).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.ANDESITE).base(Items.STONE).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.POLISHED_ANDESITE).base(Items.ANDESITE).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.GRASS_BLOCK).base(Items.DIRT).add(Source.SUN, 2).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.DIRT).set(Source.EARTH, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.COARSE_DIRT).base(Items.DIRT).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.PODZOL).base(Items.DIRT).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.CRIMSON_NYLIUM).base(Items.NETHERRACK).add(Source.MOON, 2).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.WARPED_NYLIUM).base(Items.NETHERRACK).add(Source.MOON, 2).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.COBBLESTONE).set(Source.EARTH, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.OAK_PLANKS).set(Source.EARTH, 2).set(Source.SUN, 2).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.SPRUCE_PLANKS).base(Items.OAK_PLANKS).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.BIRCH_PLANKS).base(Items.OAK_PLANKS).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.JUNGLE_PLANKS).base(Items.OAK_PLANKS).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.ACACIA_PLANKS).base(Items.OAK_PLANKS).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.DARK_OAK_PLANKS).base(Items.OAK_PLANKS).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.CRIMSON_PLANKS).base(Items.OAK_PLANKS).add(Source.MOON, 2).add(Source.INFERNAL, 2).remove(Source.SUN, 2).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.WARPED_PLANKS).base(Items.CRIMSON_PLANKS).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.OAK_SAPLING).set(Source.EARTH, 10).set(Source.SUN, 10).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.SPRUCE_SAPLING).base(Items.OAK_SAPLING).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.BIRCH_SAPLING).base(Items.OAK_SAPLING).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.JUNGLE_SAPLING).base(Items.OAK_SAPLING).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.ACACIA_SAPLING).base(Items.OAK_SAPLING).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.DARK_OAK_SAPLING).base(Items.OAK_SAPLING).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.BEDROCK).set(Source.EARTH, 20).set(Source.VOID, 20).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.SAND).set(Source.EARTH, 5).set(Source.SUN, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.RED_SAND).base(Items.SAND).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.GRAVEL).set(Source.EARTH, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.GOLD_ORE).base(Items.STONE).add(Source.EARTH, 10).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.IRON_ORE).base(Items.STONE).add(Source.EARTH, 10).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.COAL_ORE).base(Items.STONE).add(Source.EARTH, 5).add(Source.INFERNAL, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.NETHER_GOLD_ORE).base(Items.NETHERRACK).add(Source.EARTH, 10).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.OAK_LOG).set(Source.EARTH, 10).set(Source.SUN, 10).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.SPRUCE_LOG).base(Items.OAK_LOG).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.BIRCH_LOG).base(Items.OAK_LOG).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.JUNGLE_LOG).base(Items.OAK_LOG).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.ACACIA_LOG).base(Items.OAK_LOG).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.DARK_OAK_LOG).base(Items.OAK_LOG).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.CRIMSON_STEM).base(Items.OAK_LOG).add(Source.MOON, 10).add(Source.INFERNAL, 10).remove(Source.SUN, 10).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.WARPED_STEM).base(Items.CRIMSON_STEM).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.STRIPPED_OAK_LOG).base(Items.OAK_LOG).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.STRIPPED_SPRUCE_LOG).base(Items.SPRUCE_LOG).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.STRIPPED_BIRCH_LOG).base(Items.BIRCH_LOG).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.STRIPPED_JUNGLE_LOG).base(Items.JUNGLE_LOG).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.STRIPPED_ACACIA_LOG).base(Items.ACACIA_LOG).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.STRIPPED_DARK_OAK_LOG).base(Items.DARK_OAK_LOG).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.STRIPPED_CRIMSON_STEM).base(Items.CRIMSON_STEM).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.STRIPPED_WARPED_STEM).base(Items.WARPED_STEM).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.STRIPPED_OAK_WOOD).base(Items.OAK_WOOD).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.STRIPPED_SPRUCE_WOOD).base(Items.SPRUCE_WOOD).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.STRIPPED_BIRCH_WOOD).base(Items.BIRCH_WOOD).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.STRIPPED_JUNGLE_WOOD).base(Items.JUNGLE_WOOD).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.STRIPPED_ACACIA_WOOD).base(Items.ACACIA_WOOD).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.STRIPPED_DARK_OAK_WOOD).base(Items.DARK_OAK_WOOD).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.STRIPPED_CRIMSON_HYPHAE).base(Items.CRIMSON_HYPHAE).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.STRIPPED_WARPED_HYPHAE).base(Items.WARPED_HYPHAE).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.OAK_WOOD).base(Items.OAK_LOG).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.SPRUCE_WOOD).base(Items.SPRUCE_LOG).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.BIRCH_WOOD).base(Items.BIRCH_LOG).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.JUNGLE_WOOD).base(Items.JUNGLE_LOG).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.ACACIA_WOOD).base(Items.ACACIA_LOG).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.DARK_OAK_WOOD).base(Items.DARK_OAK_LOG).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.CRIMSON_HYPHAE).base(Items.CRIMSON_STEM).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.WARPED_HYPHAE).base(Items.WARPED_STEM).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.OAK_LEAVES).set(Source.EARTH, 5).set(Source.SKY, 5).set(Source.SUN, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.SPRUCE_LEAVES).base(Items.OAK_LEAVES).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.BIRCH_LEAVES).base(Items.OAK_LEAVES).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.JUNGLE_LEAVES).base(Items.OAK_LEAVES).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.ACACIA_LEAVES).base(Items.OAK_LEAVES).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.DARK_OAK_LEAVES).base(Items.OAK_LEAVES).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.SPONGE).set(Source.SEA, 10).set(Source.VOID, 10).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.WET_SPONGE).base(Items.SPONGE).add(Source.SEA, 10).remove(Source.VOID, 10).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.LAPIS_ORE).base(Items.STONE).add(Source.EARTH, 10).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.SANDSTONE).base(Items.STONE).add(Source.SUN, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.COBWEB).set(Source.BLOOD, 2).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.GRASS).set(Source.EARTH, 2).set(Source.SUN, 2).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.FERN).base(Items.GRASS).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.DEAD_BUSH).base(Items.GRASS).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.SEAGRASS).base(Items.GRASS).add(Source.SEA, 2).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.SEA_PICKLE).set(Source.EARTH, 5).set(Source.SEA, 5).set(Source.SUN, 10).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.WHITE_WOOL).set(Source.BLOOD, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.ORANGE_WOOL).base(Items.WHITE_WOOL).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.MAGENTA_WOOL).base(Items.WHITE_WOOL).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.LIGHT_BLUE_WOOL).base(Items.WHITE_WOOL).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.YELLOW_WOOL).base(Items.WHITE_WOOL).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.LIME_WOOL).base(Items.WHITE_WOOL).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.PINK_WOOL).base(Items.WHITE_WOOL).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.GRAY_WOOL).base(Items.WHITE_WOOL).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.LIGHT_GRAY_WOOL).base(Items.WHITE_WOOL).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.CYAN_WOOL).base(Items.WHITE_WOOL).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.PURPLE_WOOL).base(Items.WHITE_WOOL).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.BLUE_WOOL).base(Items.WHITE_WOOL).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.BROWN_WOOL).base(Items.WHITE_WOOL).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.GREEN_WOOL).base(Items.WHITE_WOOL).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.RED_WOOL).base(Items.WHITE_WOOL).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.BLACK_WOOL).base(Items.WHITE_WOOL).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.DANDELION).set(Source.EARTH, 5).set(Source.SUN, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.POPPY).base(Items.DANDELION).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.BLUE_ORCHID).base(Items.DANDELION).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.ALLIUM).base(Items.DANDELION).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.AZURE_BLUET).base(Items.DANDELION).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.RED_TULIP).base(Items.DANDELION).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.ORANGE_TULIP).base(Items.DANDELION).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.WHITE_TULIP).base(Items.DANDELION).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.PINK_TULIP).base(Items.DANDELION).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.OXEYE_DAISY).base(Items.DANDELION).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.CORNFLOWER).base(Items.DANDELION).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.LILY_OF_THE_VALLEY).base(Items.DANDELION).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.WITHER_ROSE).base(Items.DANDELION).add(Source.INFERNAL, 5).remove(Source.SUN, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.BROWN_MUSHROOM).set(Source.EARTH, 5).set(Source.MOON, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.RED_MUSHROOM).base(Items.BROWN_MUSHROOM).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.CRIMSON_FUNGUS).base(Items.BROWN_MUSHROOM).add(Source.INFERNAL, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.WARPED_FUNGUS).base(Items.CRIMSON_FUNGUS).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.CRIMSON_ROOTS).set(Source.EARTH, 2).set(Source.MOON, 2).set(Source.INFERNAL, 2).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.WARPED_ROOTS).base(Items.CRIMSON_ROOTS).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.NETHER_SPROUTS).base(Items.CRIMSON_ROOTS).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.WEEPING_VINES).base(Items.VINE).add(Source.MOON, 5).add(Source.INFERNAL, 5).remove(Source.SUN, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.TWISTING_VINES).base(Items.WEEPING_VINES).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.SUGAR_CANE).set(Source.EARTH, 5).set(Source.SKY, 5).set(Source.SUN, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.KELP).set(Source.EARTH, 5).set(Source.SEA, 5).set(Source.SUN, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.BAMBOO).set(Source.EARTH, 5).set(Source.SKY, 5).set(Source.SUN, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.OAK_SLAB).set(Source.EARTH, 1).set(Source.SUN, 1).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.SPRUCE_SLAB).base(Items.OAK_SLAB).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.BIRCH_SLAB).base(Items.OAK_SLAB).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.JUNGLE_SLAB).base(Items.OAK_SLAB).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.ACACIA_SLAB).base(Items.OAK_SLAB).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.DARK_OAK_SLAB).base(Items.OAK_SLAB).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.CRIMSON_SLAB).base(Items.OAK_SLAB).add(Source.MOON, 1).add(Source.INFERNAL, 1).remove(Source.SUN, 1).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.WARPED_SLAB).base(Items.CRIMSON_SLAB).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.PETRIFIED_OAK_SLAB).base(Items.OAK_SLAB).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.MOSSY_COBBLESTONE).base(Items.COBBLESTONE).add(Source.MOON, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.OBSIDIAN).set(Source.EARTH, 5).set(Source.INFERNAL, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.TORCH).set(Source.EARTH, 2).set(Source.SUN, 10).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.CHORUS_PLANT).set(Source.EARTH, 5).set(Source.MOON, 5).set(Source.VOID, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.CHORUS_FLOWER).base(Items.CHORUS_PLANT).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.PURPUR_BLOCK).set(Source.EARTH, 5).set(Source.VOID, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.SPAWNER).set(Source.BLOOD, 20).set(Source.INFERNAL, 10).set(Source.VOID, 10).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.DIAMOND_ORE).base(Items.STONE).add(Source.EARTH, 20).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.FARMLAND).base(Items.DIRT).add(Source.SEA, 2).add(Source.SUN, 5).build(consumer);
        // TODO Append 5 infernal to Furnace
        ItemAffinityBuilder.itemAffinity(Items.REDSTONE_ORE).base(Items.STONE).add(Source.EARTH, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.SNOW).set(Source.SEA, 2).set(Source.SKY, 2).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.ICE).set(Source.SEA, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.SNOW_BLOCK).set(Source.SEA, 5).set(Source.SKY, 5).build(consumer);
        ItemAffinityBuilder.itemAffinity(Items.CACTUS).set(Source.EARTH, 5).set(Source.SUN, 5).build(consumer);
        
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
