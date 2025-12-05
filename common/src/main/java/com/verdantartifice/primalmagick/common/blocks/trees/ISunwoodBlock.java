package com.verdantartifice.primalmagick.common.blocks.trees;

import com.verdantartifice.primalmagick.common.blockstates.properties.TimePhase;
import net.minecraft.world.level.LevelTimeAccess;
import org.jetbrains.annotations.NotNull;

/**
 * Interface denoting a block that phases in during the day and out at night.
 *
 * @author Daedalus4096
 */
public interface ISunwoodBlock extends IPhasingBlock {
    @Override
    default TimePhase getCurrentPhase(@NotNull LevelTimeAccess level) {
        return TimePhase.getSunPhase(level);
    }
}
