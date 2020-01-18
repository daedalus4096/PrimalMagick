package com.verdantartifice.primalmagic.common.blocks.base;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;

/**
 * Extension of the vanilla stairs block with a public constructor.
 * 
 * @author Daedalus4096
 */
public class StairsBlockPM extends StairsBlock {
    public StairsBlockPM(BlockState state, Block.Properties properties) {
        // Super constructor is protected, so expose it
        super(state, properties);
    }
}
