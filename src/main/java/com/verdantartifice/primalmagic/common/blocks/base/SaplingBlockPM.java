package com.verdantartifice.primalmagic.common.blocks.base;

import net.minecraft.block.Block;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.trees.Tree;

/**
 * Extension of the vanilla sapling with a public constructor.
 * 
 * @author Daedalus4096
 */
public class SaplingBlockPM extends SaplingBlock {
    public SaplingBlockPM(Tree tree, Block.Properties properties) {
        // Super constructor is protected, so expose it
        super(tree, properties);
    }
}
