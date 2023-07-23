package com.verdantartifice.primalmagick.common.blocks.base;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Extension of the vanilla sapling with a public constructor.
 * 
 * @author Daedalus4096
 */
public class SaplingBlockPM extends SaplingBlock {
    protected final TagKey<Block> mayPlaceOn;
    
    public SaplingBlockPM(AbstractTreeGrower tree, TagKey<Block> mayPlaceOn, Block.Properties properties) {
        super(tree, properties);
        this.mayPlaceOn = mayPlaceOn;
    }

    @Override
    protected boolean mayPlaceOn(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return pState.is(this.mayPlaceOn);
    }
}
