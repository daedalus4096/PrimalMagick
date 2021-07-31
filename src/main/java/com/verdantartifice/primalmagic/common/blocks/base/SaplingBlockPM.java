package com.verdantartifice.primalmagic.common.blocks.base;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;

/**
 * Extension of the vanilla sapling with a public constructor.
 * 
 * @author Daedalus4096
 */
public class SaplingBlockPM extends SaplingBlock {
    public SaplingBlockPM(AbstractTreeGrower tree, Block.Properties properties) {
        // Super constructor is protected, so expose it
        super(tree, properties);
    }
}
