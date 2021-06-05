package com.verdantartifice.primalmagic.common.blocks.trees;

import java.util.Random;

import com.verdantartifice.primalmagic.common.worldgen.features.FeaturesPM;

import net.minecraft.block.trees.Tree;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;

/**
 * Definition for a hallowood tree.  Used by hallowood saplings to spawn the hallowood tree worldgen feature.
 * 
 * @author Daedalus4096
 */
public class HallowoodTree extends Tree {
    @Override
    protected ConfiguredFeature<BaseTreeFeatureConfig, ?> getTreeFeature(Random randomIn, boolean largeHive) {
        return FeaturesPM.TREE_HALLOWOOD;
    }
}
