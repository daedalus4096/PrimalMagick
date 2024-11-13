package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.platform.services.ILevelService;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class LevelServiceNeoforge implements ILevelService {
    @Override
    public boolean isAreaLoaded(Level level, BlockPos pos, int range) {
        return level.isAreaLoaded(pos, range);
    }
}
