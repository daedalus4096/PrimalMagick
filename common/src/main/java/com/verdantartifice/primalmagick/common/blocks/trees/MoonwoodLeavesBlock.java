package com.verdantartifice.primalmagick.common.blocks.trees;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

/**
 * Block definition for moonwood leaves.  They are decorative blocks that fade out of existence during the day.
 * 
 * @author Daedalus4096
 */
public class MoonwoodLeavesBlock extends AbstractPhasingLeavesBlock implements IMoonwoodBlock {
    public MoonwoodLeavesBlock(BlockBehaviour.Properties props) {
        super(props);
    }
}
