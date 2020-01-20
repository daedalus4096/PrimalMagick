package com.verdantartifice.primalmagic.common.blocks.trees;

import java.util.Random;

import com.verdantartifice.primalmagic.common.worldgen.features.SunwoodTreeFeature;

import net.minecraft.block.trees.Tree;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

/**
 * Definition for a sunwood tree.  Used by sunwood saplings to spawn the sunwood tree worldgen feature.
 * They fade out of existence and become indestructable at night.
 * 
 * @author Daedalus4096
 */
public class SunwoodTree extends Tree {
    @Override
    protected AbstractTreeFeature<NoFeatureConfig> getTreeFeature(Random random) {
        return new SunwoodTreeFeature(NoFeatureConfig::deserialize, true);
    }
}
