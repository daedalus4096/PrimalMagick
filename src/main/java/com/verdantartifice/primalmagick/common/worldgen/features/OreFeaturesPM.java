package com.verdantartifice.primalmagick.common.worldgen.features;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.blocks.BlocksPM;

import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;

/**
 * Registration for mod ore worldgen features.
 * 
 * @author Daedalus4096
 */
public class OreFeaturesPM {
    public static ConfiguredFeature<?, ?> ORE_MARBLE_RAW;
    public static ConfiguredFeature<?, ?> ORE_ROCK_SALT;
    public static ConfiguredFeature<?, ?> ORE_QUARTZ;
    
    public static void setupOreFeatures() {
        ORE_MARBLE_RAW = registerOreFeature("ore_marble_raw", new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(OreFeatures.NATURAL_STONE, BlocksPM.MARBLE_RAW.get().defaultBlockState(), 33)));
        ORE_ROCK_SALT = registerOreFeature("ore_rock_salt", new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(OreFeatures.NATURAL_STONE, BlocksPM.ROCK_SALT_ORE.get().defaultBlockState(), 10)));
        ORE_QUARTZ = registerOreFeature("ore_quartz", new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(OreFeatures.NATURAL_STONE, BlocksPM.QUARTZ_ORE.get().defaultBlockState(), 3)));
    }
    
    private static <FC extends FeatureConfiguration> ConfiguredFeature<FC, ?> registerOreFeature(String key, ConfiguredFeature<FC, ?> configuredFeature) {
        return Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(PrimalMagick.MODID, key), configuredFeature);
    }
}
