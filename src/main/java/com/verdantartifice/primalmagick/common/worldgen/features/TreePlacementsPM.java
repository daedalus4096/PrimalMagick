package com.verdantartifice.primalmagick.common.worldgen.features;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.blocks.BlocksPM;

import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;

/**
 * Registration for mod grown tree worldgen placements.
 * 
 * @author Daedalus4096
 */
public class TreePlacementsPM {
    public static Holder<PlacedFeature> TREE_SUNWOOD_FULL_CHECKED;
    public static Holder<PlacedFeature> TREE_SUNWOOD_WAXING_CHECKED;
    public static Holder<PlacedFeature> TREE_SUNWOOD_WANING_CHECKED;
    public static Holder<PlacedFeature> TREE_SUNWOOD_FADED_CHECKED;

    public static Holder<PlacedFeature> TREE_MOONWOOD_FULL_CHECKED;
    public static Holder<PlacedFeature> TREE_MOONWOOD_WAXING_CHECKED;
    public static Holder<PlacedFeature> TREE_MOONWOOD_WANING_CHECKED;
    public static Holder<PlacedFeature> TREE_MOONWOOD_FADED_CHECKED;
    
    public static Holder<PlacedFeature> TREE_HALLOWOOD_CHECKED;
    
    public static void setupTreePlacements() {
        TREE_SUNWOOD_FULL_CHECKED = registerTreePlacement("tree_sunwood_full_checked", TreeFeaturesPM.TREE_SUNWOOD_FULL, PlacementUtils.filteredByBlockSurvival(BlocksPM.SUNWOOD_SAPLING.get()));
        TREE_SUNWOOD_WAXING_CHECKED = registerTreePlacement("tree_sunwood_waxing_checked", TreeFeaturesPM.TREE_SUNWOOD_WAXING, PlacementUtils.filteredByBlockSurvival(BlocksPM.SUNWOOD_SAPLING.get()));
        TREE_SUNWOOD_WANING_CHECKED = registerTreePlacement("tree_sunwood_waning_checked", TreeFeaturesPM.TREE_SUNWOOD_WANING, PlacementUtils.filteredByBlockSurvival(BlocksPM.SUNWOOD_SAPLING.get()));
        TREE_SUNWOOD_FADED_CHECKED = registerTreePlacement("tree_sunwood_faded_checked", TreeFeaturesPM.TREE_SUNWOOD_FADED, PlacementUtils.filteredByBlockSurvival(BlocksPM.SUNWOOD_SAPLING.get()));

        TREE_MOONWOOD_FULL_CHECKED = registerTreePlacement("tree_moonwood_full_checked", TreeFeaturesPM.TREE_MOONWOOD_FULL, PlacementUtils.filteredByBlockSurvival(BlocksPM.MOONWOOD_SAPLING.get()));
        TREE_MOONWOOD_WAXING_CHECKED = registerTreePlacement("tree_moonwood_waxing_checked", TreeFeaturesPM.TREE_MOONWOOD_WAXING, PlacementUtils.filteredByBlockSurvival(BlocksPM.MOONWOOD_SAPLING.get()));
        TREE_MOONWOOD_WANING_CHECKED = registerTreePlacement("tree_moonwood_waning_checked", TreeFeaturesPM.TREE_MOONWOOD_WANING, PlacementUtils.filteredByBlockSurvival(BlocksPM.MOONWOOD_SAPLING.get()));
        TREE_MOONWOOD_FADED_CHECKED = registerTreePlacement("tree_moonwood_faded_checked", TreeFeaturesPM.TREE_MOONWOOD_FADED, PlacementUtils.filteredByBlockSurvival(BlocksPM.MOONWOOD_SAPLING.get()));
        
        TREE_HALLOWOOD_CHECKED = registerTreePlacement("tree_hallowood_checked", TreeFeaturesPM.TREE_HALLOWOOD, PlacementUtils.filteredByBlockSurvival(BlocksPM.HALLOWOOD_SAPLING.get()));
    }
    
    private static Holder<PlacedFeature> registerTreePlacement(String key, ConfiguredFeature<?, ?> feature, PlacementModifier... placementModifiers) {
        return PlacementUtils.register(PrimalMagick.MODID + ":" + key, Holder.direct(feature), placementModifiers);
    }
}
