package com.verdantartifice.primalmagick.common.worldgen.features;

import java.util.List;

import com.verdantartifice.primalmagick.PrimalMagick;

import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.placement.PlacementUtils;
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
 * Registration for mod ore worldgen placements.
 * 
 * @author Daedalus4096
 */
public class OrePlacementsPM {
    public static Holder<PlacedFeature> ORE_MARBLE_RAW_UPPER;
    public static Holder<PlacedFeature> ORE_MARBLE_RAW_LOWER;
    public static Holder<PlacedFeature> ORE_ROCK_SALT_UPPER;
    public static Holder<PlacedFeature> ORE_ROCK_SALT_LOWER;
    public static Holder<PlacedFeature> ORE_QUARTZ;
    
    public static void setupOrePlacements() {
        ORE_MARBLE_RAW_UPPER = registerOrePlacement("ore_marble_raw_upper", OreFeaturesPM.ORE_MARBLE_RAW, rareOrePlacement(6, HeightRangePlacement.uniform(VerticalAnchor.absolute(64), VerticalAnchor.absolute(128))));
        ORE_MARBLE_RAW_LOWER = registerOrePlacement("ore_marble_raw_lower", OreFeaturesPM.ORE_MARBLE_RAW, commonOrePlacement(2, HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(60))));
        ORE_ROCK_SALT_UPPER = registerOrePlacement("ore_rock_salt_upper", OreFeaturesPM.ORE_ROCK_SALT, rareOrePlacement(6, HeightRangePlacement.uniform(VerticalAnchor.absolute(64), VerticalAnchor.absolute(128))));
        ORE_ROCK_SALT_LOWER = registerOrePlacement("ore_rock_salt_lower", OreFeaturesPM.ORE_ROCK_SALT, commonOrePlacement(2, HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(60))));
        ORE_QUARTZ = registerOrePlacement("ore_quartz", OreFeaturesPM.ORE_QUARTZ, rareOrePlacement(6, HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(128))));
    }
    
    private static Holder<PlacedFeature> registerOrePlacement(String key, ConfiguredFeature<?, ?> feature, List<PlacementModifier> placementModifiers) {
        return PlacementUtils.register(PrimalMagick.MODID + ":" + key, Holder.direct(feature), placementModifiers);
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
