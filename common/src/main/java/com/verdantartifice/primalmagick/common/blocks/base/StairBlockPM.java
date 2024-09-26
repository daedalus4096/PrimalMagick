package com.verdantartifice.primalmagick.common.blocks.base;

import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Extension of the vanilla stairs with a public constructor.
 *
 * @author Daedalus4096
 */
public class StairBlockPM extends StairBlock {
    public StairBlockPM(BlockState baseState, BlockBehaviour.Properties baseProperties) {
        super(baseState, baseProperties);
    }
}
