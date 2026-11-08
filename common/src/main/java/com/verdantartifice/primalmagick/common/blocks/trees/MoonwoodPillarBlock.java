package com.verdantartifice.primalmagick.common.blocks.trees;

import net.minecraft.world.level.block.state.BlockBehaviour;

/**
 * Block definition for moonwood pillars.  They are decorative blocks that fade out of existence during the day.
 * 
 * @author Daedalus4096
 */
public class MoonwoodPillarBlock extends AbstractPhasingPillarBlock implements IMoonwoodBlock {
    public MoonwoodPillarBlock(BlockBehaviour.Properties props) {
        super(props);
    }
}
