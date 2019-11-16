package com.verdantartifice.primalmagic.common.blocks.trees;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.blockstates.properties.TimePhase;

import net.minecraft.block.Block;
import net.minecraft.world.IWorld;

public class SunwoodSlabBlock extends AbstractPhasingSlabBlock {
    public SunwoodSlabBlock(Block.Properties properties) {
        super(properties);
        this.setRegistryName(PrimalMagic.MODID, "sunwood_slab");
    }

    @Override
    protected TimePhase getCurrentPhase(IWorld world) {
        return TimePhase.getSunPhase(world);
    }
}
