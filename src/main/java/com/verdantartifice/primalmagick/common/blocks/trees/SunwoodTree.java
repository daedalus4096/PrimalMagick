package com.verdantartifice.primalmagick.common.blocks.trees;

import java.util.HashMap;
import java.util.Map;

import com.verdantartifice.primalmagick.common.blockstates.properties.TimePhase;
import com.verdantartifice.primalmagick.common.worldgen.features.ConfiguredFeaturesPM;

import net.minecraft.Util;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

/**
 * Definition for a sunwood tree.  Used by sunwood saplings to spawn the sunwood tree worldgen feature.
 * They fade out of existence and become indestructable at night.
 * 
 * @author Daedalus4096
 */
public class SunwoodTree extends AbstractPhasingTree {
    @Override
    protected Map<TimePhase, ResourceKey<ConfiguredFeature<?, ?>>> getTreeFeaturesByPhase(RandomSource rand, boolean largeHive) {
        return Util.make(new HashMap<>(), (map) -> {
            map.put(TimePhase.FULL, ConfiguredFeaturesPM.TREE_SUNWOOD_FULL);
            map.put(TimePhase.WAXING, ConfiguredFeaturesPM.TREE_SUNWOOD_WAXING);
            map.put(TimePhase.WANING, ConfiguredFeaturesPM.TREE_SUNWOOD_WANING);
            map.put(TimePhase.FADED, ConfiguredFeaturesPM.TREE_SUNWOOD_FADED);
        });
    }

    @Override
    protected TimePhase getCurrentPhase(LevelAccessor world) {
        return TimePhase.getMoonPhase(world);
    }
}
