package com.verdantartifice.primalmagic.common.blocks.trees;

import java.util.Random;

import com.verdantartifice.primalmagic.common.worldgen.features.FeatureConfigsPM;

import net.minecraft.block.trees.Tree;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;

/**
 * Definition for a moonwood tree.  Used by moonwood saplings to spawn the moonwood tree worldgen feature.
 * They fade out of existence and become indestructable during the day.
 * 
 * @author Daedalus4096
 */
public class MoonwoodTree extends Tree {
    @Override
    protected ConfiguredFeature<TreeFeatureConfig, ?> getTreeFeature(Random randomIn, boolean hasFlowers) {
        return Feature.NORMAL_TREE.withConfiguration(FeatureConfigsPM.MOONWOOD_TREE_CONFIG);
    }
}
