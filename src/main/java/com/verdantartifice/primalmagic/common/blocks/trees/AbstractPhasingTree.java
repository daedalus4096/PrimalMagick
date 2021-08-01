package com.verdantartifice.primalmagic.common.blocks.trees;

import java.util.Map;
import java.util.Random;

import com.verdantartifice.primalmagic.common.blockstates.properties.TimePhase;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraftforge.common.util.Constants;

/**
 * Definition of a tree which chooses its associated feature based on the current time phase.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractPhasingTree extends AbstractTreeGrower {
    @Override
    protected ConfiguredFeature<TreeConfiguration, ?> getConfiguredFeature(Random randomIn, boolean largeHive) {
        // Unused; get the default fully-phased feature
        return this.getTreeFeaturesByPhase(randomIn, largeHive).get(TimePhase.FULL);
    }

    /**
     * Get all of the possible features for this tree, mapped by time phase
     */
    protected abstract Map<TimePhase, ConfiguredFeature<TreeConfiguration, ?>> getTreeFeaturesByPhase(Random rand, boolean largeHive);

    /**
     * Get the current time phase for this tree based on world time
     */
    protected abstract TimePhase getCurrentPhase(LevelAccessor world);

    @Override
    public boolean growTree(ServerLevel world, ChunkGenerator chunkGenerator, BlockPos pos, BlockState state, Random rand) {
        Map<TimePhase, ConfiguredFeature<TreeConfiguration, ?>> featureMap = this.getTreeFeaturesByPhase(rand, false);
        TimePhase currentPhase = this.getCurrentPhase(world);
        ConfiguredFeature<TreeConfiguration, ?> configuredFeature = featureMap.get(currentPhase);
        if (configuredFeature == null) {
            return false;
        } else {
            world.setBlock(pos, Blocks.AIR.defaultBlockState(), Constants.BlockFlags.NO_RERENDER);
            if (configuredFeature.place(world, chunkGenerator, rand, pos)) {
                return true;
            } else {
                world.setBlock(pos, state, Constants.BlockFlags.NO_RERENDER);
                return false;
            }
        }
    }
}
