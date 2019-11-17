package com.verdantartifice.primalmagic.common.blocks.trees;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.blockstates.properties.TimePhase;

import net.minecraft.block.Block;
import net.minecraft.world.IWorld;

public class MoonwoodSlabBlock extends AbstractPhasingSlabBlock {
    public MoonwoodSlabBlock(Block.Properties properties) {
        super(properties);
        this.setRegistryName(PrimalMagic.MODID, "moonwood_slab");
    }

    @Override
    protected TimePhase getCurrentPhase(IWorld world) {
        return TimePhase.getMoonPhase(world);
    }
}
