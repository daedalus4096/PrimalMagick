package com.verdantartifice.primalmagic.common.blocks.trees;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.blockstates.properties.TimePhase;

import net.minecraft.block.Block;
import net.minecraft.world.IWorld;

/**
 * Block definition for moonwood slabs.  They are decorative blocks that fade out of existence and become indestructable during the day.
 * 
 * @author Daedalus4096
 */
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
