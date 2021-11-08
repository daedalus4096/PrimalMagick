package com.verdantartifice.primalmagick.common.blocks.trees;

import com.verdantartifice.primalmagick.common.blockstates.properties.TimePhase;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;

/**
 * Block definition for moonwood slabs.  They are decorative blocks that fade out of existence and become indestructable during the day.
 * 
 * @author Daedalus4096
 */
public class MoonwoodSlabBlock extends AbstractPhasingSlabBlock {
    public MoonwoodSlabBlock(Block.Properties properties) {
        super(properties);
    }

    @Override
    protected TimePhase getCurrentPhase(LevelAccessor world) {
        return TimePhase.getMoonPhase(world);
    }
}
