package com.verdantartifice.primalmagic.common.blocks.trees;

import java.util.Random;

import com.verdantartifice.primalmagic.common.worldgen.features.MoonwoodTreeFeature;

import net.minecraft.block.trees.Tree;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

/**
 * Definition for a moonwood tree.  Used by moonwood saplings to spawn the moonwood tree worldgen feature.
 * They fade out of existence and become indestructable during the day.
 * 
 * @author Daedalus4096
 */
public class MoonwoodTree extends Tree {
    @Override
    protected AbstractTreeFeature<NoFeatureConfig> getTreeFeature(Random random) {
        return new MoonwoodTreeFeature(NoFeatureConfig::deserialize, true);
    }
}
