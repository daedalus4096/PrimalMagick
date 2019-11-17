package com.verdantartifice.primalmagic.common.blocks.trees;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.blockstates.properties.TimePhase;

import net.minecraft.block.BlockState;
import net.minecraft.world.IWorld;

public class MoonwoodStairsBlock extends AbstractPhasingStairsBlock {
    public MoonwoodStairsBlock(BlockState state, Properties properties) {
        super(state, properties);
        this.setRegistryName(PrimalMagic.MODID, "moonwood_stairs");
    }

    @Override
    protected TimePhase getCurrentPhase(IWorld world) {
        return TimePhase.getMoonPhase(world);
    }
}
