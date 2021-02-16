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
 * Definition for a moonwood tree.  Used by moonwood saplings to spawn the moonwood tree worldgen feature.
 * They fade out of existence and become indestructable during the day.
 * 
 * @author Daedalus4096
 */
public class MoonwoodTree extends AbstractPhasingTree {
    @Override
    protected Map<TimePhase, ConfiguredFeature<BaseTreeFeatureConfig, ?>> getTreeFeaturesByPhase(Random rand, boolean largeHive) {
        return Util.make(new HashMap<>(), (map) -> {
            map.put(TimePhase.FULL, FeaturesPM.TREE_MOONWOOD_FULL);
            map.put(TimePhase.WAXING, FeaturesPM.TREE_MOONWOOD_WAXING);
            map.put(TimePhase.WANING, FeaturesPM.TREE_MOONWOOD_WANING);
            map.put(TimePhase.FADED, FeaturesPM.TREE_MOONWOOD_FADED);
        });
    }

    @Override
    protected TimePhase getCurrentPhase(IWorld world) {
        return TimePhase.getMoonPhase(world);
    }
}
