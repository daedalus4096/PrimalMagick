package com.verdantartifice.primalmagick.common.blocks.trees;

import com.verdantartifice.primalmagick.common.blockstates.properties.TimePhase;
import net.minecraft.world.level.LevelTimeAccess;
import org.jetbrains.annotations.NotNull;

/**
 * Interface denoting a block that phases in at night and out during the day.
 *
 * @author Daedalus4096
 */
public interface IMoonwoodBlock extends IPhasingBlock {
    @Override
    default TimePhase getCurrentPhase(@NotNull LevelTimeAccess level) {
        return TimePhase.getMoonPhase(level);
    }
}
