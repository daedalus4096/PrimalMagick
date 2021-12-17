package com.verdantartifice.primalmagick.common.blocks.trees;

import java.util.Random;

import com.verdantartifice.primalmagick.common.worldgen.features.TreeFeaturesPM;

import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;

/**
 * Definition for a hallowood tree.  Used by hallowood saplings to spawn the hallowood tree worldgen feature.
 * 
 * @author Daedalus4096
 */
public class HallowoodTree extends AbstractTreeGrower {
    @Override
    protected ConfiguredFeature<TreeConfiguration, ?> getConfiguredFeature(Random randomIn, boolean largeHive) {
        return TreeFeaturesPM.TREE_HALLOWOOD;
    }
}
