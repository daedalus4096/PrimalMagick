package com.verdantartifice.primalmagick.common.worldgen.features;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.config.Config;

import net.minecraft.data.BuiltinRegistries;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Deferred registry for mod worldgen features.
 * 
 * @author Daedalus4096
 */
public class StructureFeaturesPM {
    private static final DeferredRegister<StructureFeature<?>> STRUCTURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, PrimalMagick.MODID);
    
    public static void init() {
        STRUCTURES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
    
    public static final RegistryObject<StructureFeature<ShrineConfig>> SHRINE = STRUCTURES.register("shrine", () -> new ShrineStructure(ShrineConfig.CODEC));
    
    public static void setupStructures() {
        int shrineAvgDist = Config.SHRINE_AVERAGE_DISTANCE_CHUNKS.get();
        int shrineMinDist = Math.min(Config.SHRINE_MINIMUM_DISTANCE_CHUNKS.get(), shrineAvgDist - 1);
        setupSpacingAndLand(
                SHRINE.get(), 
                new StructureFeatureConfiguration(
                        shrineAvgDist,  // Average distance apart in chunks between spawn attempts 
                        shrineMinDist,  // Minimum distance apart in chunks between spawn attempts
                        11893192),      // Seed modifier
                true);
    }
    
    private static <S extends StructureFeature<?>> void setupSpacingAndLand(S structure, StructureFeatureConfiguration settings, boolean transformSurroundingLand) {
        StructureFeature.STRUCTURES_REGISTRY.put(structure.getRegistryName().toString(), structure);
        
        // Modify surrounding land to conform to the bottom of the structure, if necessary
        if (transformSurroundingLand) {
            StructureFeature.NOISE_AFFECTING_FEATURES = ImmutableList.<StructureFeature<?>>builder()
                    .addAll(StructureFeature.NOISE_AFFECTING_FEATURES)
                    .add(structure)
                    .build();
        }
        
        // Add default structure spacing
        StructureSettings.DEFAULTS = ImmutableMap.<StructureFeature<?>, StructureFeatureConfiguration>builder()
                .putAll(StructureSettings.DEFAULTS)
                .put(structure, settings)
                .build();
        
        // Add the structure to the noise settings registry just in case another mod needs it before the world is made
        BuiltinRegistries.NOISE_GENERATOR_SETTINGS.entrySet().forEach(noiseSettings -> {
            Map<StructureFeature<?>, StructureFeatureConfiguration> structureMap = noiseSettings.getValue().structureSettings().structureConfig;
            if (structureMap instanceof ImmutableMap) {
                Map<StructureFeature<?>, StructureFeatureConfiguration> tempMap = new HashMap<>(structureMap);
                tempMap.put(structure, settings);
                noiseSettings.getValue().structureSettings().structureConfig = tempMap;
            } else {
                structureMap.put(structure, settings);
            }
        });
    }
}
