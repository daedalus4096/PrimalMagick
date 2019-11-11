package com.verdantartifice.primalmagic.common.blocks.trees;

import java.util.Random;

import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.common.worldgen.features.SunwoodTreeFeature;

import net.minecraft.block.trees.Tree;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

public class SunwoodTree extends Tree {
    @Override
    @Nullable
    protected AbstractTreeFeature<NoFeatureConfig> getTreeFeature(Random random) {
        return new SunwoodTreeFeature(NoFeatureConfig::deserialize, true);
    }
}
