package com.verdantartifice.primalmagick.common.blocks.trees;

import net.minecraft.world.level.block.Block;

/**
 * Block definition for sunwood slabs.  They are decorative blocks that fade out of existence at night.
 * 
 * @author Daedalus4096
 */
public class SunwoodSlabBlock extends AbstractPhasingSlabBlock implements ISunwoodBlock {
    public SunwoodSlabBlock(Block.Properties properties) {
        super(properties);
    }
}
