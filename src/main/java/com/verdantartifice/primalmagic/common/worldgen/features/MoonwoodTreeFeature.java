package com.verdantartifice.primalmagic.common.worldgen.features;

import java.util.function.Function;

import com.mojang.datafixers.Dynamic;
import com.verdantartifice.primalmagic.common.blocks.BlocksPM;
import com.verdantartifice.primalmagic.common.blocks.trees.MoonwoodLeavesBlock;
import com.verdantartifice.primalmagic.common.blocks.trees.MoonwoodLogBlock;
import com.verdantartifice.primalmagic.common.blockstates.properties.TimePhase;

import net.minecraft.block.BlockState;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.common.IPlantable;

/**
 * Definition of a worldgen feature for a moonwood tree.
 * 
 * @author Daedalus4096
 */
public class MoonwoodTreeFeature extends AbstractPhasingTreeFeature {
    protected static final BlockState LOG = BlocksPM.MOONWOOD_LOG.getDefaultState();
    protected static final BlockState LEAF = BlocksPM.MOONWOOD_LEAVES.getDefaultState();
    
    public MoonwoodTreeFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> config, boolean doBlockNotify) {
        super(config, doBlockNotify);
    }

    @Override
    protected IPlantable getSapling() {
        return (IPlantable)BlocksPM.MOONWOOD_SAPLING;
    }

    @Override
    protected BlockState getLogState(IWorldGenerationReader world) {
        if (world instanceof IWorld) {
            return LOG.with(MoonwoodLogBlock.PHASE, TimePhase.getMoonPhase((IWorld)world));
        } else {
            return LOG;
        }
    }

    @Override
    protected BlockState getLeafState(IWorldGenerationReader world) {
        if (world instanceof IWorld) {
            return LEAF.with(MoonwoodLeavesBlock.PHASE, TimePhase.getMoonPhase((IWorld)world));
        } else {
            return LEAF;
        }
    }
}
