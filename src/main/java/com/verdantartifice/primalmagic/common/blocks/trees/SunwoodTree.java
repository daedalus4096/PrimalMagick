package com.verdantartifice.primalmagic.common.blocks.trees;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.verdantartifice.primalmagic.common.blockstates.properties.TimePhase;
import com.verdantartifice.primalmagic.common.worldgen.features.FeaturesPM;

import net.minecraft.util.Util;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;

/**
 * Definition for a sunwood tree.  Used by sunwood saplings to spawn the sunwood tree worldgen feature.
 * They fade out of existence and become indestructable at night.
 * 
 * @author Daedalus4096
 */
public class SunwoodTree extends AbstractPhasingTree {
    protected static final Map<TimePhase, ConfiguredFeature<BaseTreeFeatureConfig, ?>> FEATURES = Util.make(new HashMap<>(), (map) -> {
        map.put(TimePhase.FULL, FeaturesPM.TREE_SUNWOOD_FULL);
        map.put(TimePhase.WAXING, FeaturesPM.TREE_SUNWOOD_WAXING);
        map.put(TimePhase.WANING, FeaturesPM.TREE_SUNWOOD_WANING);
        map.put(TimePhase.FADED, FeaturesPM.TREE_SUNWOOD_FADED);
    });

    @Override
    protected Map<TimePhase, ConfiguredFeature<BaseTreeFeatureConfig, ?>> getTreeFeaturesByPhase(Random rand, boolean largeHive) {
        return FEATURES;
    }

    @Override
    protected TimePhase getCurrentPhase(IWorld world) {
        return TimePhase.getMoonPhase(world);
    }
}
