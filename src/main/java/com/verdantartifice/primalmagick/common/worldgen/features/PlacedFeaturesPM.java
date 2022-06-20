package com.verdantartifice.primalmagick.common.worldgen.features;

import java.util.List;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.blocks.BlocksPM;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

/**
 * Registration for placed versions of configured mod features (e.g. ores, vegetation, etc).
 * 
 * @author Daedalus4096
 */
public class PlacedFeaturesPM {
    private static final DeferredRegister<PlacedFeature> PLACED_FEATURES = DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, PrimalMagick.MODID);
    
    public static void init() {
        PLACED_FEATURES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    // Register placed ore features
    public static final RegistryObject<PlacedFeature> ORE_MARBLE_RAW_UPPER = PLACED_FEATURES.register("ore_marble_raw_upper", () -> new PlacedFeature(Holder.direct(ConfiguredFeaturesPM.ORE_MARBLE_RAW.get()), rareOrePlacement(6, HeightRangePlacement.uniform(VerticalAnchor.absolute(64), VerticalAnchor.absolute(128)))));
    public static final RegistryObject<PlacedFeature> ORE_MARBLE_RAW_LOWER = PLACED_FEATURES.register("ore_marble_raw_lower", () -> new PlacedFeature(Holder.direct(ConfiguredFeaturesPM.ORE_MARBLE_RAW.get()), commonOrePlacement(2, HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(60)))));
    public static final RegistryObject<PlacedFeature> ORE_ROCK_SALT_UPPER = PLACED_FEATURES.register("ore_rock_salt_upper", () -> new PlacedFeature(Holder.direct(ConfiguredFeaturesPM.ORE_ROCK_SALT.get()), rareOrePlacement(6, HeightRangePlacement.uniform(VerticalAnchor.absolute(64), VerticalAnchor.absolute(128)))));
    public static final RegistryObject<PlacedFeature> ORE_ROCK_SALT_LOWER = PLACED_FEATURES.register("ore_rock_salt_lower", () -> new PlacedFeature(Holder.direct(ConfiguredFeaturesPM.ORE_ROCK_SALT.get()), commonOrePlacement(2, HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(60)))));
    public static final RegistryObject<PlacedFeature> ORE_QUARTZ = PLACED_FEATURES.register("ore_quartz", () -> new PlacedFeature(Holder.direct(ConfiguredFeaturesPM.ORE_QUARTZ.get()), commonOrePlacement(10, HeightRangePlacement.uniform(VerticalAnchor.absolute(64), VerticalAnchor.absolute(128)))));
    public static final RegistryObject<PlacedFeature> ORE_QUARTZ_LOWER = PLACED_FEATURES.register("ore_quartz_lower", () -> new PlacedFeature(Holder.direct(ConfiguredFeaturesPM.ORE_QUARTZ.get()), commonOrePlacement(20, HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(60)))));
    
    // Register placed tree features (cultivated)
    public static final RegistryObject<PlacedFeature> TREE_SUNWOOD_FULL_CHECKED = PLACED_FEATURES.register("tree_sunwood_full_checked", () -> new PlacedFeature(Holder.direct(ConfiguredFeaturesPM.TREE_SUNWOOD_FULL.get()), List.of(PlacementUtils.filteredByBlockSurvival(BlocksPM.SUNWOOD_SAPLING.get()))));
    public static final RegistryObject<PlacedFeature> TREE_SUNWOOD_WAXING_CHECKED = PLACED_FEATURES.register("tree_sunwood_waxing_checked", () -> new PlacedFeature(Holder.direct(ConfiguredFeaturesPM.TREE_SUNWOOD_WAXING.get()), List.of(PlacementUtils.filteredByBlockSurvival(BlocksPM.SUNWOOD_SAPLING.get()))));
    public static final RegistryObject<PlacedFeature> TREE_SUNWOOD_WANING_CHECKED = PLACED_FEATURES.register("tree_sunwood_waning_checked", () -> new PlacedFeature(Holder.direct(ConfiguredFeaturesPM.TREE_SUNWOOD_WANING.get()), List.of(PlacementUtils.filteredByBlockSurvival(BlocksPM.SUNWOOD_SAPLING.get()))));
    public static final RegistryObject<PlacedFeature> TREE_SUNWOOD_FADED_CHECKED = PLACED_FEATURES.register("tree_sunwood_faded_checked", () -> new PlacedFeature(Holder.direct(ConfiguredFeaturesPM.TREE_SUNWOOD_FADED.get()), List.of(PlacementUtils.filteredByBlockSurvival(BlocksPM.SUNWOOD_SAPLING.get()))));
    
    public static final RegistryObject<PlacedFeature> TREE_MOONWOOD_FULL_CHECKED = PLACED_FEATURES.register("tree_moonwood_full_checked", () -> new PlacedFeature(Holder.direct(ConfiguredFeaturesPM.TREE_MOONWOOD_FULL.get()), List.of(PlacementUtils.filteredByBlockSurvival(BlocksPM.MOONWOOD_SAPLING.get()))));
    public static final RegistryObject<PlacedFeature> TREE_MOONWOOD_WAXING_CHECKED = PLACED_FEATURES.register("tree_moonwood_waxing_checked", () -> new PlacedFeature(Holder.direct(ConfiguredFeaturesPM.TREE_MOONWOOD_WAXING.get()), List.of(PlacementUtils.filteredByBlockSurvival(BlocksPM.MOONWOOD_SAPLING.get()))));
    public static final RegistryObject<PlacedFeature> TREE_MOONWOOD_WANING_CHECKED = PLACED_FEATURES.register("tree_moonwood_waning_checked", () -> new PlacedFeature(Holder.direct(ConfiguredFeaturesPM.TREE_MOONWOOD_WANING.get()), List.of(PlacementUtils.filteredByBlockSurvival(BlocksPM.MOONWOOD_SAPLING.get()))));
    public static final RegistryObject<PlacedFeature> TREE_MOONWOOD_FADED_CHECKED = PLACED_FEATURES.register("tree_moonwood_faded_checked", () -> new PlacedFeature(Holder.direct(ConfiguredFeaturesPM.TREE_MOONWOOD_FADED.get()), List.of(PlacementUtils.filteredByBlockSurvival(BlocksPM.MOONWOOD_SAPLING.get()))));
    
    public static final RegistryObject<PlacedFeature> TREE_HALLOWOOD_CHECKED = PLACED_FEATURES.register("tree_hallowood_checked", () -> new PlacedFeature(Holder.direct(ConfiguredFeaturesPM.TREE_HALLOWOOD.get()), List.of(PlacementUtils.filteredByBlockSurvival(BlocksPM.HALLOWOOD_SAPLING.get()))));

    // Register placed tree features (wild)
    public static final RegistryObject<PlacedFeature> TREE_WILD_SUNWOOD = PLACED_FEATURES.register("trees_wild_sunwood", () -> new PlacedFeature(Holder.direct(ConfiguredFeaturesPM.TREE_SUNWOOD_FULL.get()), VegetationPlacements.treePlacement(PlacementUtils.countExtra(0, 0.05f, 1), BlocksPM.SUNWOOD_SAPLING.get())));
    public static final RegistryObject<PlacedFeature> TREE_WILD_MOONWOOD = PLACED_FEATURES.register("trees_wild_moonwood", () -> new PlacedFeature(Holder.direct(ConfiguredFeaturesPM.TREE_MOONWOOD_FULL.get()), VegetationPlacements.treePlacement(PlacementUtils.countExtra(0, 0.05f, 1), BlocksPM.MOONWOOD_SAPLING.get())));
    
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
