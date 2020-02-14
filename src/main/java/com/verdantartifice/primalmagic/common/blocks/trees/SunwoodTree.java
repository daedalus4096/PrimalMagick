package com.verdantartifice.primalmagic.common.blocks.trees;

import java.util.Random;

import com.verdantartifice.primalmagic.common.worldgen.features.FeatureConfigsPM;
import com.verdantartifice.primalmagic.common.worldgen.features.FeaturesPM;

import net.minecraft.block.trees.Tree;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;

/**
 * Definition for a sunwood tree.  Used by sunwood saplings to spawn the sunwood tree worldgen feature.
 * They fade out of existence and become indestructable at night.
 * 
 * @author Daedalus4096
 */
public class SunwoodTree extends Tree {
    @Override
    protected ConfiguredFeature<TreeFeatureConfig, ?> getTreeFeature(Random randomIn, boolean hasFlowers) {
        return FeaturesPM.PHASING_TREE.withConfiguration(FeatureConfigsPM.SUNWOOD_TREE_CONFIG);
    }
}
