package com.verdantartifice.primalmagic.common.blocks.trees;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.blockstates.properties.TimePhase;

import net.minecraft.block.BlockState;
import net.minecraft.world.IWorld;

/**
 * Block definition for sunwood stairs.  They are decorative blocks that fade out of existence and become indestructable at night.
 * 
 * @author Daedalus4096
 */
public class SunwoodStairsBlock extends AbstractPhasingStairsBlock {
    public SunwoodStairsBlock(BlockState state, Properties properties) {
        super(state, properties);
        this.setRegistryName(PrimalMagic.MODID, "sunwood_stairs");
    }

    @Override
    protected TimePhase getCurrentPhase(IWorld world) {
        return TimePhase.getSunPhase(world);
    }
}
