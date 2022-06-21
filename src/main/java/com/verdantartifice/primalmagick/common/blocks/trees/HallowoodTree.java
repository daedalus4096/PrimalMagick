package com.verdantartifice.primalmagick.common.blocks.trees;

import com.verdantartifice.primalmagick.common.worldgen.features.ConfiguredFeaturesPM;

import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
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
    protected Holder<ConfiguredFeature<TreeConfiguration, ?>> getConfiguredFeature(RandomSource randomIn, boolean largeHive) {
        return Holder.direct(ConfiguredFeaturesPM.TREE_HALLOWOOD.get());
    }
}
