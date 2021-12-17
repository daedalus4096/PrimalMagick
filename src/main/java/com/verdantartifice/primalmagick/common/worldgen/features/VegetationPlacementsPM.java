package com.verdantartifice.primalmagick.common.worldgen.features;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.blocks.BlocksPM;

import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

/**
 * Registration for mod wild tree worldgen placements.
 * 
 * @author Daedalus4096
 */
public class VegetationPlacementsPM {
    public static PlacedFeature TREES_WILD_SUNWOOD;
    public static PlacedFeature TREES_WILD_MOONWOOD;
    
    public static void setupTreePlacements() {
        TREES_WILD_SUNWOOD = registerTreePlacement("trees_wild_sunwood", TreeFeaturesPM.TREE_SUNWOOD_FULL.placed(VegetationPlacements.treePlacement(PlacementUtils.countExtra(0, 0.05f, 1), BlocksPM.SUNWOOD_SAPLING.get())));
        TREES_WILD_MOONWOOD = registerTreePlacement("trees_wild_moonwood", TreeFeaturesPM.TREE_MOONWOOD_FULL.placed(VegetationPlacements.treePlacement(PlacementUtils.countExtra(0, 0.05f, 1), BlocksPM.MOONWOOD_SAPLING.get())));
    }
    
    private static PlacedFeature registerTreePlacement(String key, PlacedFeature placedFeature) {
        return Registry.register(BuiltinRegistries.PLACED_FEATURE, new ResourceLocation(PrimalMagick.MODID, key), placedFeature);
    }
}
