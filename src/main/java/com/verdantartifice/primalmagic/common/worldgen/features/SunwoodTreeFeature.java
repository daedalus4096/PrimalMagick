package com.verdantartifice.primalmagic.common.worldgen.features;

import java.util.function.Function;

import com.mojang.datafixers.Dynamic;
import com.verdantartifice.primalmagic.common.blocks.BlocksPM;
import com.verdantartifice.primalmagic.common.blocks.trees.SunwoodLeavesBlock;
import com.verdantartifice.primalmagic.common.blocks.trees.SunwoodLogBlock;
import com.verdantartifice.primalmagic.common.blockstates.properties.TimePhase;

import net.minecraft.block.BlockState;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.common.IPlantable;

/**
 * Definition of a worldgen feature for a sunwood tree.
 * 
 * @author Daedalus4096
 */
public class SunwoodTreeFeature extends AbstractPhasingTreeFeature {
    protected static final BlockState LOG = BlocksPM.SUNWOOD_LOG.getDefaultState();
    protected static final BlockState LEAF = BlocksPM.SUNWOOD_LEAVES.getDefaultState();
    
    public SunwoodTreeFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> config, boolean doBlockNotify) {
        super(config, doBlockNotify);
    }

    @Override
    protected IPlantable getSapling() {
        return (IPlantable)BlocksPM.SUNWOOD_SAPLING;
    }

    @Override
    protected BlockState getLogState(IWorldGenerationReader world) {
        if (world instanceof IWorld) {
            return LOG.with(SunwoodLogBlock.PHASE, TimePhase.getSunPhase((IWorld)world));
        } else {
            return LOG;
        }
    }

    @Override
    protected BlockState getLeafState(IWorldGenerationReader world) {
        if (world instanceof IWorld) {
            return LEAF.with(SunwoodLeavesBlock.PHASE, TimePhase.getSunPhase((IWorld)world));
        } else {
            return LEAF;
        }
    }
}
