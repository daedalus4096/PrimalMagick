package com.verdantartifice.primalmagick.common.blocks.trees;

import java.util.HashMap;
import java.util.Map;

import com.verdantartifice.primalmagick.common.blockstates.properties.TimePhase;
import com.verdantartifice.primalmagick.common.worldgen.features.ConfiguredFeaturesPM;

import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;

/**
 * Definition for a sunwood tree.  Used by sunwood saplings to spawn the sunwood tree worldgen feature.
 * They fade out of existence and become indestructable at night.
 * 
 * @author Daedalus4096
 */
public class SunwoodTree extends AbstractPhasingTree {
    @Override
    protected Map<TimePhase, Holder<ConfiguredFeature<TreeConfiguration, ?>>> getTreeFeaturesByPhase(RandomSource rand, boolean largeHive) {
        return Util.make(new HashMap<>(), (map) -> {
            map.put(TimePhase.FULL, Holder.direct(ConfiguredFeaturesPM.TREE_SUNWOOD_FULL.get()));
            map.put(TimePhase.WAXING, Holder.direct(ConfiguredFeaturesPM.TREE_SUNWOOD_WAXING.get()));
            map.put(TimePhase.WANING, Holder.direct(ConfiguredFeaturesPM.TREE_SUNWOOD_WANING.get()));
            map.put(TimePhase.FADED, Holder.direct(ConfiguredFeaturesPM.TREE_SUNWOOD_FADED.get()));
        });
    }

    @Override
    protected TimePhase getCurrentPhase(LevelAccessor world) {
        return TimePhase.getMoonPhase(world);
    }
}
