package com.verdantartifice.primalmagick.platform.services;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public interface ILevelService {
    boolean isAreaLoaded(Level level, BlockPos pos, int range);
}
