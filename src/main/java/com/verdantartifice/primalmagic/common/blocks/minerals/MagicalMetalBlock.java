package com.verdantartifice.primalmagic.common.blocks.minerals;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;

/**
 * Block definition for a magical metal storage block.  Can serve as a beacon base.
 * 
 * @author Daedalus4096
 */
public class MagicalMetalBlock extends Block {
    public MagicalMetalBlock(Block.Properties properties) {
        super(properties);
    }
    
    @Override
    public boolean isBeaconBase(BlockState state, IWorldReader world, BlockPos pos, BlockPos beacon) {
        return true;
    }
}
