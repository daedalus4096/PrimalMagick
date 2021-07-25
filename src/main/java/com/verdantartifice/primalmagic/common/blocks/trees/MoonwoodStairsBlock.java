package com.verdantartifice.primalmagic.common.blocks.trees;

import java.util.function.Supplier;

import com.verdantartifice.primalmagic.common.blockstates.properties.TimePhase;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.LevelAccessor;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

/**
 * Block definition for moonwood stairs.  They are decorative blocks that fade out of existence and become indestructable during the day.
 * 
 * @author Daedalus4096
 */
public class MoonwoodStairsBlock extends AbstractPhasingStairsBlock {
    public MoonwoodStairsBlock(Supplier<BlockState> state, Properties properties) {
        super(state, properties);
    }

    @Override
    protected TimePhase getCurrentPhase(LevelAccessor world) {
        return TimePhase.getMoonPhase(world);
    }
}
