package com.verdantartifice.primalmagick.common.blocks.trees;

import net.minecraft.world.level.block.Block;

/**
 * Block definition for moonwood slabs.  They are decorative blocks that fade out of existence during the day.
 * 
 * @author Daedalus4096
 */
public class MoonwoodSlabBlock extends AbstractPhasingSlabBlock implements IMoonwoodBlock {
    public MoonwoodSlabBlock(Block.Properties properties) {
        super(properties);
    }
}
