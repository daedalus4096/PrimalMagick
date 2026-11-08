package com.verdantartifice.primalmagick.common.blocks.trees;

import net.minecraft.world.level.block.state.BlockBehaviour;

/**
 * Block definition for sunwood pillars.  They are decorative blocks that fade out of existence at night.
 * 
 * @author Daedalus4096
 */
public class SunwoodPillarBlock extends AbstractPhasingPillarBlock implements ISunwoodBlock {
    public SunwoodPillarBlock(BlockBehaviour.Properties props) {
        super(props);
    }
}
