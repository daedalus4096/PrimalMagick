package com.verdantartifice.primalmagick.common.events;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.worldgen.features.ConfiguredStructureFeaturesPM;

import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Handlers for world-related events.
 * 
 * @author Daedalus4096
 */
@Mod.EventBusSubscriber(modid=PrimalMagick.MODID)
public class WorldEvents {
    private static final Logger LOGGER = LogManager.getLogger();

    @SubscribeEvent
    public static void onWorldLoad(WorldEvent.Load event) {
        if (event.getWorld() instanceof ServerLevel serverLevel) {
            ChunkGenerator chunkGenerator = serverLevel.getChunkSource().getGenerator();
            
            // Prevent spawning structures in vanilla superflat
            if (chunkGenerator instanceof FlatLevelSource && serverLevel.dimension().equals(Level.OVERWORLD)) {
                return;
            }
            
            // TODO Temporary until Forge API finds a better solution for adding structures to biomes
            StructureSettings worldStructureConfig = chunkGenerator.getSettings();
            HashMap<StructureFeature<?>, HashMultimap<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>>> structureToMultiMap = new HashMap<>();
            
            // Associate configured structures with the biomes they should spawn in
            for (Map.Entry<ResourceKey<Biome>, Biome> biomeEntry : serverLevel.registryAccess().ownedRegistryOrThrow(Registry.BIOME_REGISTRY).entrySet()) {
                Biome.BiomeCategory cat = biomeEntry.getValue().getBiomeCategory();
                
                // Add earth shrines to flatlands
                if (Biome.BiomeCategory.PLAINS.equals(cat) || Biome.BiomeCategory.SAVANNA.equals(cat)) {
                    associateBiomeToConfiguredStructure(structureToMultiMap, ConfiguredStructureFeaturesPM.CONFIGURED_EARTH_SHRINE, biomeEntry.getKey());
                }
                
                // Add sea shrines to wet biomes
                if (Biome.BiomeCategory.RIVER.equals(cat) || Biome.BiomeCategory.BEACH.equals(cat) || Biome.BiomeCategory.SWAMP.equals(cat) || Biome.BiomeCategory.ICY.equals(cat)) {
                    associateBiomeToConfiguredStructure(structureToMultiMap, ConfiguredStructureFeaturesPM.CONFIGURED_SEA_SHRINE, biomeEntry.getKey());
                }
                
                // Add sky shrines to mountains
                if (Biome.BiomeCategory.EXTREME_HILLS.equals(cat) || Biome.BiomeCategory.MOUNTAIN.equals(cat)) {
                    associateBiomeToConfiguredStructure(structureToMultiMap, ConfiguredStructureFeaturesPM.CONFIGURED_SKY_SHRINE, biomeEntry.getKey());
                }
                
                // Add sun shrines to deserts
                if (Biome.BiomeCategory.DESERT.equals(cat)) {
                    associateBiomeToConfiguredStructure(structureToMultiMap, ConfiguredStructureFeaturesPM.CONFIGURED_SUN_SHRINE, biomeEntry.getKey());
                }
                
                // Add moon shrines to forests
                if (Biome.BiomeCategory.FOREST.equals(cat)) {
                    associateBiomeToConfiguredStructure(structureToMultiMap, ConfiguredStructureFeaturesPM.CONFIGURED_MOON_SHRINE, biomeEntry.getKey());
                }
            }

            // Grab the map that holds what ConfigureStructures a structure has and what biomes it can spawn in, then add this mod's structures to it
            ImmutableMap.Builder<StructureFeature<?>, ImmutableMultimap<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>>> tempStructureToMultiMap = ImmutableMap.builder();
            worldStructureConfig.configuredStructures.entrySet().stream().filter(entry -> !structureToMultiMap.containsKey(entry.getKey())).forEach(tempStructureToMultiMap::put);
            structureToMultiMap.forEach((key, value) -> tempStructureToMultiMap.put(key, ImmutableMultimap.copyOf(value)));
            worldStructureConfig.configuredStructures = tempStructureToMultiMap.build();
        }
    }
    
    private static void associateBiomeToConfiguredStructure(HashMap<StructureFeature<?>, HashMultimap<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>>> structureToMultiMap,
            ConfiguredStructureFeature<?, ?> configuredStructureFeature, ResourceKey<Biome> biomeRegistryKey) {
        structureToMultiMap.putIfAbsent(configuredStructureFeature.feature, HashMultimap.create());
        HashMultimap<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>> configuredStructureToBiomeMultiMap = structureToMultiMap.get(configuredStructureFeature.feature);
        if (configuredStructureToBiomeMultiMap.containsValue(biomeRegistryKey)) {
            LOGGER.error("Skipping structure {} because another configured structure feature is already associated with biome {}", 
                    BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE.getId(configuredStructureFeature), biomeRegistryKey);
        } else {
            configuredStructureToBiomeMultiMap.put(configuredStructureFeature, biomeRegistryKey);
        }
    }
}
