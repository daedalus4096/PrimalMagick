package com.verdantartifice.primalmagic.common.worldgen.features;

import java.util.Random;
import java.util.Set;

import com.verdantartifice.primalmagic.common.blocks.trees.AbstractPhasingLeavesBlock;
import com.verdantartifice.primalmagic.common.blockstates.properties.TimePhase;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;

public class PhasingBlobFoliagePlacer extends BlobFoliagePlacer {
    public PhasingBlobFoliagePlacer(int p_i225846_1_, int p_i225846_2_) {
        super(p_i225846_1_, p_i225846_2_);
    }
    
    @Override
    protected void func_227385_a_(IWorldGenerationReader world, Random rng, BlockPos pos, TreeFeatureConfig config, Set<BlockPos> posSet) {
        if (AbstractTreeFeature.isAirOrLeaves(world, pos) || AbstractTreeFeature.isTallPlants(world, pos) || AbstractTreeFeature.isWater(world, pos)) {
            BlockState leavesState = config.leavesProvider.func_225574_a_(rng, pos);
            if (world instanceof IWorld && leavesState.getBlock() instanceof AbstractPhasingLeavesBlock) {
                // Set the leaves' phase blockstate before placement
                TimePhase phase = ((AbstractPhasingLeavesBlock)leavesState.getBlock()).getCurrentPhase((IWorld)world);
                leavesState = leavesState.with(AbstractPhasingLeavesBlock.PHASE, phase);
            }
            world.setBlockState(pos, leavesState, 19);
            posSet.add(pos.toImmutable());
        }
    }
}
