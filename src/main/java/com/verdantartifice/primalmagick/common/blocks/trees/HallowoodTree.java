package com.verdantartifice.primalmagick.common.blocks.trees;

import com.verdantartifice.primalmagick.common.worldgen.features.ConfiguredFeaturesPM;

import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

/**
 * Definition for a hallowood tree.  Used by hallowood saplings to spawn the hallowood tree worldgen feature.
 * 
 * @author Daedalus4096
 */
public class HallowoodTree extends AbstractTreeGrower {
    @Override
    protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource randomIn, boolean largeHive) {
        return ConfiguredFeaturesPM.TREE_HALLOWOOD;
    }
}
