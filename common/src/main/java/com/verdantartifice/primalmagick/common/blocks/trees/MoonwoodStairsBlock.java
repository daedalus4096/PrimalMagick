package com.verdantartifice.primalmagick.common.blocks.trees;

import net.minecraft.world.level.block.state.BlockState;

/**
 * Block definition for moonwood stairs.  They are decorative blocks that fade out of existence during the day.
 * 
 * @author Daedalus4096
 */
public class MoonwoodStairsBlock extends AbstractPhasingStairsBlock implements IMoonwoodBlock {
    public MoonwoodStairsBlock(BlockState state, Properties properties) {
        super(state, properties);
    }
}
