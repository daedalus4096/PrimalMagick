package com.verdantartifice.primalmagic.common.blocks.trees;

import java.util.Map;
import java.util.Random;

import com.verdantartifice.primalmagic.common.blockstates.properties.TimePhase;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.trees.Tree;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;

/**
 * Definition of a tree which chooses its associated feature based on the current time phase.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractPhasingTree extends Tree {
    @Override
    protected ConfiguredFeature<BaseTreeFeatureConfig, ?> getTreeFeature(Random randomIn, boolean largeHive) {
        // Unused; get the default fully-phased feature
        return this.getTreeFeaturesByPhase(randomIn, largeHive).get(TimePhase.FULL);
    }

    /**
     * Get all of the possible features for this tree, mapped by time phase
     */
    protected abstract Map<TimePhase, ConfiguredFeature<BaseTreeFeatureConfig, ?>> getTreeFeaturesByPhase(Random rand, boolean largeHive);

    /**
     * Get the current time phase for this tree based on world time
     */
    protected abstract TimePhase getCurrentPhase(IWorld world);

    @Override
    public boolean attemptGrowTree(ServerWorld world, ChunkGenerator chunkGenerator, BlockPos pos, BlockState state, Random rand) {
        Map<TimePhase, ConfiguredFeature<BaseTreeFeatureConfig, ?>> featureMap = this.getTreeFeaturesByPhase(rand, false);
        TimePhase currentPhase = this.getCurrentPhase(world);
        ConfiguredFeature<BaseTreeFeatureConfig, ?> configuredFeature = featureMap.get(currentPhase);
        if (configuredFeature == null) {
            return false;
        } else {
            world.setBlockState(pos, Blocks.AIR.getDefaultState(), Constants.BlockFlags.NO_RERENDER);
            configuredFeature.config.forcePlacement();
            if (configuredFeature.generate(world, chunkGenerator, rand, pos)) {
                return true;
            } else {
                world.setBlockState(pos, state, Constants.BlockFlags.NO_RERENDER);
                return false;
            }
        }
    }
}
