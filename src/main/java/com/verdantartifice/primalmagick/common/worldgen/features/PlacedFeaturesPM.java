package com.verdantartifice.primalmagick.common.worldgen.features;

import java.util.List;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.blocks.BlocksPM;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.RarityFilter;

/**
 * Registration for placed versions of configured mod features (e.g. ores, vegetation, etc).
 * 
 * @author Daedalus4096
 */
public class PlacedFeaturesPM {
    public static final ResourceKey<PlacedFeature> ORE_MARBLE_RAW_UPPER = registerKey("ore_marble_raw_upper");
    public static final ResourceKey<PlacedFeature> ORE_MARBLE_RAW_LOWER = registerKey("ore_marble_raw_lower");
    public static final ResourceKey<PlacedFeature> ORE_ROCK_SALT_UPPER = registerKey("ore_rock_salt_upper");
    public static final ResourceKey<PlacedFeature> ORE_ROCK_SALT_LOWER = registerKey("ore_rock_salt_lower");
    public static final ResourceKey<PlacedFeature> ORE_QUARTZ_UPPER = registerKey("ore_quartz_upper");
    public static final ResourceKey<PlacedFeature> ORE_QUARTZ_LOWER = registerKey("ore_quartz_lower");
    
    public static final ResourceKey<PlacedFeature> TREE_SUNWOOD_FULL_CHECKED = registerKey("tree_sunwood_full_checked");
    public static final ResourceKey<PlacedFeature> TREE_SUNWOOD_WAXING_CHECKED = registerKey("tree_sunwood_waxing_checked");
    public static final ResourceKey<PlacedFeature> TREE_SUNWOOD_WANING_CHECKED = registerKey("tree_sunwood_waning_checked");
    public static final ResourceKey<PlacedFeature> TREE_SUNWOOD_FADED_CHECKED = registerKey("tree_sunwood_faded_checked");
    
    public static final ResourceKey<PlacedFeature> TREE_MOONWOOD_FULL_CHECKED = registerKey("tree_moonwood_full_checked");
    public static final ResourceKey<PlacedFeature> TREE_MOONWOOD_WAXING_CHECKED = registerKey("tree_moonwood_waxing_checked");
    public static final ResourceKey<PlacedFeature> TREE_MOONWOOD_WANING_CHECKED = registerKey("tree_moonwood_waning_checked");
    public static final ResourceKey<PlacedFeature> TREE_MOONWOOD_FADED_CHECKED = registerKey("tree_moonwood_faded_checked");
    
    public static final ResourceKey<PlacedFeature> TREE_HALLOWOOD_CHECKED = registerKey("tree_hallowood_checked");
    
    public static final ResourceKey<PlacedFeature> TREE_WILD_SUNWOOD = registerKey("tree_wild_sunwood");
    public static final ResourceKey<PlacedFeature> TREE_WILD_MOONWOOD = registerKey("tree_wild_moonwood");
    
    public static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, PrimalMagick.resource(name));
    }
    
    public static void bootstrap(BootstapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> features = context.lookup(Registries.CONFIGURED_FEATURE);
        
        // Register placed ore features
        context.register(ORE_MARBLE_RAW_UPPER, new PlacedFeature(features.getOrThrow(ConfiguredFeaturesPM.ORE_MARBLE_RAW), rareOrePlacement(6, HeightRangePlacement.uniform(VerticalAnchor.absolute(64), VerticalAnchor.absolute(128)))));
        context.register(ORE_MARBLE_RAW_LOWER, new PlacedFeature(features.getOrThrow(ConfiguredFeaturesPM.ORE_MARBLE_RAW), commonOrePlacement(2, HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(60)))));
        context.register(ORE_ROCK_SALT_UPPER, new PlacedFeature(features.getOrThrow(ConfiguredFeaturesPM.ORE_ROCK_SALT), rareOrePlacement(6, HeightRangePlacement.uniform(VerticalAnchor.absolute(64), VerticalAnchor.absolute(128)))));
        context.register(ORE_ROCK_SALT_LOWER, new PlacedFeature(features.getOrThrow(ConfiguredFeaturesPM.ORE_ROCK_SALT), commonOrePlacement(2, HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(60)))));
        context.register(ORE_QUARTZ_UPPER, new PlacedFeature(features.getOrThrow(ConfiguredFeaturesPM.ORE_QUARTZ), commonOrePlacement(10, HeightRangePlacement.uniform(VerticalAnchor.absolute(64), VerticalAnchor.absolute(128)))));
        context.register(ORE_QUARTZ_LOWER, new PlacedFeature(features.getOrThrow(ConfiguredFeaturesPM.ORE_QUARTZ), commonOrePlacement(20, HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(60)))));

        // Register placed tree features (cultivated)
        context.register(TREE_SUNWOOD_FULL_CHECKED, new PlacedFeature(features.getOrThrow(ConfiguredFeaturesPM.TREE_SUNWOOD_FULL), List.of(PlacementUtils.filteredByBlockSurvival(BlocksPM.SUNWOOD_SAPLING.get()))));
        context.register(TREE_SUNWOOD_WAXING_CHECKED, new PlacedFeature(features.getOrThrow(ConfiguredFeaturesPM.TREE_SUNWOOD_WAXING), List.of(PlacementUtils.filteredByBlockSurvival(BlocksPM.SUNWOOD_SAPLING.get()))));
        context.register(TREE_SUNWOOD_WANING_CHECKED, new PlacedFeature(features.getOrThrow(ConfiguredFeaturesPM.TREE_SUNWOOD_WANING), List.of(PlacementUtils.filteredByBlockSurvival(BlocksPM.SUNWOOD_SAPLING.get()))));
        context.register(TREE_SUNWOOD_FADED_CHECKED, new PlacedFeature(features.getOrThrow(ConfiguredFeaturesPM.TREE_SUNWOOD_FADED), List.of(PlacementUtils.filteredByBlockSurvival(BlocksPM.SUNWOOD_SAPLING.get()))));
        
        context.register(TREE_MOONWOOD_FULL_CHECKED, new PlacedFeature(features.getOrThrow(ConfiguredFeaturesPM.TREE_MOONWOOD_FULL), List.of(PlacementUtils.filteredByBlockSurvival(BlocksPM.MOONWOOD_SAPLING.get()))));
        context.register(TREE_MOONWOOD_WAXING_CHECKED, new PlacedFeature(features.getOrThrow(ConfiguredFeaturesPM.TREE_MOONWOOD_WAXING), List.of(PlacementUtils.filteredByBlockSurvival(BlocksPM.MOONWOOD_SAPLING.get()))));
        context.register(TREE_MOONWOOD_WANING_CHECKED, new PlacedFeature(features.getOrThrow(ConfiguredFeaturesPM.TREE_MOONWOOD_WANING), List.of(PlacementUtils.filteredByBlockSurvival(BlocksPM.MOONWOOD_SAPLING.get()))));
        context.register(TREE_MOONWOOD_FADED_CHECKED, new PlacedFeature(features.getOrThrow(ConfiguredFeaturesPM.TREE_MOONWOOD_FADED), List.of(PlacementUtils.filteredByBlockSurvival(BlocksPM.MOONWOOD_SAPLING.get()))));
        
        context.register(TREE_HALLOWOOD_CHECKED, new PlacedFeature(features.getOrThrow(ConfiguredFeaturesPM.TREE_HALLOWOOD), List.of(PlacementUtils.filteredByBlockSurvival(BlocksPM.HALLOWOOD_SAPLING.get()))));

        // Register placed tree features (wild)
        context.register(TREE_WILD_SUNWOOD, new PlacedFeature(features.getOrThrow(ConfiguredFeaturesPM.TREE_SUNWOOD_FULL), VegetationPlacements.treePlacement(PlacementUtils.countExtra(0, 0.05f, 1), BlocksPM.SUNWOOD_SAPLING.get())));
        context.register(TREE_WILD_MOONWOOD, new PlacedFeature(features.getOrThrow(ConfiguredFeaturesPM.TREE_MOONWOOD_FULL), VegetationPlacements.treePlacement(PlacementUtils.countExtra(0, 0.05f, 1), BlocksPM.MOONWOOD_SAPLING.get())));
    }

    private static List<PlacementModifier> orePlacement(PlacementModifier frequencyModifier, PlacementModifier positionModifier) {
        return List.of(frequencyModifier, InSquarePlacement.spread(), positionModifier, BiomeFilter.biome());
    }

    private static List<PlacementModifier> commonOrePlacement(int count, PlacementModifier positionModifier) {
        return orePlacement(CountPlacement.of(count), positionModifier);
    }

    private static List<PlacementModifier> rareOrePlacement(int chance, PlacementModifier positionModifier) {
        return orePlacement(RarityFilter.onAverageOnceEvery(chance), positionModifier);
    }
}
