package com.verdantartifice.primalmagick.common.worldgen.features;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.blocks.BlocksPM;

import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

/**
 * Registration for mod grown tree worldgen placements.
 * 
 * @author Daedalus4096
 */
public class TreePlacementsPM {
    public static PlacedFeature TREE_SUNWOOD_FULL_CHECKED;
    public static PlacedFeature TREE_SUNWOOD_WAXING_CHECKED;
    public static PlacedFeature TREE_SUNWOOD_WANING_CHECKED;
    public static PlacedFeature TREE_SUNWOOD_FADED_CHECKED;

    public static PlacedFeature TREE_MOONWOOD_FULL_CHECKED;
    public static PlacedFeature TREE_MOONWOOD_WAXING_CHECKED;
    public static PlacedFeature TREE_MOONWOOD_WANING_CHECKED;
    public static PlacedFeature TREE_MOONWOOD_FADED_CHECKED;
    
    public static PlacedFeature TREE_HALLOWOOD_CHECKED;
    
    public static void setupTreePlacements() {
        TREE_SUNWOOD_FULL_CHECKED = registerTreePlacement("tree_sunwood_full_checked", TreeFeaturesPM.TREE_SUNWOOD_FULL.filteredByBlockSurvival(BlocksPM.SUNWOOD_SAPLING.get()));
        TREE_SUNWOOD_WAXING_CHECKED = registerTreePlacement("tree_sunwood_waxing_checked", TreeFeaturesPM.TREE_SUNWOOD_WAXING.filteredByBlockSurvival(BlocksPM.SUNWOOD_SAPLING.get()));
        TREE_SUNWOOD_WANING_CHECKED = registerTreePlacement("tree_sunwood_waning_checked", TreeFeaturesPM.TREE_SUNWOOD_WANING.filteredByBlockSurvival(BlocksPM.SUNWOOD_SAPLING.get()));
        TREE_SUNWOOD_FADED_CHECKED = registerTreePlacement("tree_sunwood_faded_checked", TreeFeaturesPM.TREE_SUNWOOD_FADED.filteredByBlockSurvival(BlocksPM.SUNWOOD_SAPLING.get()));

        TREE_MOONWOOD_FULL_CHECKED = registerTreePlacement("tree_moonwood_full_checked", TreeFeaturesPM.TREE_MOONWOOD_FULL.filteredByBlockSurvival(BlocksPM.MOONWOOD_SAPLING.get()));
        TREE_MOONWOOD_WAXING_CHECKED = registerTreePlacement("tree_moonwood_waxing_checked", TreeFeaturesPM.TREE_MOONWOOD_WAXING.filteredByBlockSurvival(BlocksPM.MOONWOOD_SAPLING.get()));
        TREE_MOONWOOD_WANING_CHECKED = registerTreePlacement("tree_moonwood_waning_checked", TreeFeaturesPM.TREE_MOONWOOD_WANING.filteredByBlockSurvival(BlocksPM.MOONWOOD_SAPLING.get()));
        TREE_MOONWOOD_FADED_CHECKED = registerTreePlacement("tree_moonwood_faded_checked", TreeFeaturesPM.TREE_MOONWOOD_FADED.filteredByBlockSurvival(BlocksPM.MOONWOOD_SAPLING.get()));
        
        TREE_HALLOWOOD_CHECKED = registerTreePlacement("tree_hallowood_checked", TreeFeaturesPM.TREE_HALLOWOOD.filteredByBlockSurvival(BlocksPM.HALLOWOOD_SAPLING.get()));
    }
    
    private static PlacedFeature registerTreePlacement(String key, PlacedFeature placedFeature) {
        return Registry.register(BuiltinRegistries.PLACED_FEATURE, new ResourceLocation(PrimalMagick.MODID, key), placedFeature);
    }
}
