package com.verdantartifice.primalmagick.common.blocks.trees;

import net.minecraft.world.level.block.state.BlockState;

/**
 * Block definition for sunwood stairs.  They are decorative blocks that fade out of existence at night.
 * 
 * @author Daedalus4096
 */
public class SunwoodStairsBlock extends AbstractPhasingStairsBlock implements ISunwoodBlock {
    public SunwoodStairsBlock(BlockState state, Properties properties) {
        super(state, properties);
    }
}
