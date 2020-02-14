package com.verdantartifice.primalmagic.common.worldgen.features;

import java.util.Random;
import java.util.Set;
import java.util.function.Function;

import com.mojang.datafixers.Dynamic;
import com.verdantartifice.primalmagic.common.blocks.trees.AbstractPhasingLeavesBlock;
import com.verdantartifice.primalmagic.common.blocks.trees.AbstractPhasingLogBlock;
import com.verdantartifice.primalmagic.common.blockstates.properties.TimePhase;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.TreeFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;

public class PhasingTreeFeature extends TreeFeature {
    public PhasingTreeFeature(Function<Dynamic<?>, ? extends TreeFeatureConfig> deserializer) {
        super(deserializer);
    }
    
    @Override
    protected boolean func_227216_a_(IWorldGenerationReader world, Random rng, BlockPos pos, Set<BlockPos> posSet, MutableBoundingBox boundingBox, BaseTreeFeatureConfig config) {
        if (!isAirOrLeaves(world, pos) && !isTallPlants(world, pos) && !isWater(world, pos)) {
            return false;
        } else {
            BlockState logState = config.trunkProvider.func_225574_a_(rng, pos);
            if (world instanceof IWorld && logState.getBlock() instanceof AbstractPhasingLogBlock) {
                // Set the log's phase blockstate before placement
                TimePhase phase = ((AbstractPhasingLogBlock)logState.getBlock()).getCurrentPhase((IWorld)world);
                logState = logState.with(AbstractPhasingLogBlock.PHASE, phase);
            }
            this.func_227217_a_(world, pos, logState, boundingBox);
            posSet.add(pos.toImmutable());
            return true;
        }
    }
    
    @Override
    protected boolean func_227219_b_(IWorldGenerationReader world, Random rng, BlockPos pos, Set<BlockPos> posSet, MutableBoundingBox boundingBox, BaseTreeFeatureConfig config) {
        if (!isAirOrLeaves(world, pos) && !isTallPlants(world, pos) && !isWater(world, pos)) {
            return false;
        } else {
            BlockState leavesState = config.leavesProvider.func_225574_a_(rng, pos);
            if (world instanceof IWorld && leavesState.getBlock() instanceof AbstractPhasingLeavesBlock) {
                // Set the leaves' phase blockstate before placement
                TimePhase phase = ((AbstractPhasingLeavesBlock)leavesState.getBlock()).getCurrentPhase((IWorld)world);
                leavesState = leavesState.with(AbstractPhasingLeavesBlock.PHASE, phase);
            }
            this.func_227217_a_(world, pos, leavesState, boundingBox);
            posSet.add(pos.toImmutable());
            return true;
        }
    }
}
