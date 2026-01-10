package com.verdantartifice.primalmagick.common.blocks.trees;

import com.verdantartifice.primalmagick.common.blockstates.properties.TimePhase;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.NotNull;

/**
 * Base interface for a block that phases in and out based on the time of day.
 *
 * @author Daedalus4096
 */
public interface IPhasingBlock {
    EnumProperty<TimePhase> PHASE = EnumProperty.create("phase", TimePhase.class);

    /**
     * Get the current phase of the block based on the current game time.
     *
     * @param level the game world
     * @param pos the position of the block
     * @return the block's current phase
     */
    TimePhase getCurrentPhase(@NotNull LevelReader level, @NotNull BlockPos pos);

    /**
     * Update the time phase of the given block state based on the given level's current time, if the given level
     * supports phasing. If it does not, instead return the given block state.
     *
     * @param state the block state to be updated
     * @param level the game world
     * @param pos the position of the block
     * @return the new block state
     */
    default BlockState updateBlockPhase(@NotNull BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos) {
        TimePhase newPhase = this.getCurrentPhase(level, pos);
        if (newPhase != state.getValue(PHASE)) {
            state = state.setValue(PHASE, newPhase);
        }
        return state;
    }
}
