package com.verdantartifice.primalmagick.common.blocks.trees;

import com.verdantartifice.primalmagick.common.blockstates.properties.TimePhase;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Block definition for sunwood stairs.  They are decorative blocks that fade out of existence and become indestructable at night.
 * 
 * @author Daedalus4096
 */
public class SunwoodStairsBlock extends AbstractPhasingStairsBlock {
    public SunwoodStairsBlock(BlockState state, Properties properties) {
        super(state, properties);
    }

    @Override
    protected TimePhase getCurrentPhase(LevelAccessor world) {
        return TimePhase.getSunPhase(world);
    }
}
