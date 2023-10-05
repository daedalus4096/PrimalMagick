package com.verdantartifice.primalmagick.common.blocks.flowers;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TallFlowerBlock;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Block definition for an emberflower.  Like a sunflower, but it emits light and flame particles.
 * 
 * @author Daedalus4096
 */
public class EmberflowerBlock extends TallFlowerBlock {
    public EmberflowerBlock(Block.Properties properties) {
        super(properties);
    }

    @Override
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
        // TODO Auto-generated method stub
        super.animateTick(pState, pLevel, pPos, pRandom);
    }
}
