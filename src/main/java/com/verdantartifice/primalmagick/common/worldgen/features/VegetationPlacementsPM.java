package com.verdantartifice.primalmagick.common.worldgen.features;

import java.util.List;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.blocks.BlocksPM;

import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;

/**
 * Registration for mod wild tree worldgen placements.
 * 
 * @author Daedalus4096
 */
public class VegetationPlacementsPM {
    public static Holder<PlacedFeature> TREES_WILD_SUNWOOD;
    public static Holder<PlacedFeature> TREES_WILD_MOONWOOD;
    
    public static void setupTreePlacements() {
        TREES_WILD_SUNWOOD = registerTreePlacement("trees_wild_sunwood", TreeFeaturesPM.TREE_SUNWOOD_FULL, VegetationPlacements.treePlacement(PlacementUtils.countExtra(0, 0.05f, 1), BlocksPM.SUNWOOD_SAPLING.get()));
        TREES_WILD_MOONWOOD = registerTreePlacement("trees_wild_moonwood", TreeFeaturesPM.TREE_MOONWOOD_FULL, VegetationPlacements.treePlacement(PlacementUtils.countExtra(0, 0.05f, 1), BlocksPM.MOONWOOD_SAPLING.get()));
    }
    
    private static Holder<PlacedFeature> registerTreePlacement(String key, ConfiguredFeature<?, ?> feature, List<PlacementModifier> placementModifiers) {
        return PlacementUtils.register(PrimalMagick.MODID + ":" + key, Holder.direct(feature), placementModifiers);
    }
}
