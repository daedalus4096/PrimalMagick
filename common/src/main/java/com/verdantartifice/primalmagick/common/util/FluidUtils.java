package com.verdantartifice.primalmagick.common.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FlowingFluid;

public class FluidUtils {
    @SuppressWarnings("deprecation")
    public static boolean isInfiniteSource(Level level, BlockPos pos, FlowingFluid fluid) {
        int adjacentCount = 0;
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            if (level.getFluidState(pos.relative(direction)).isSourceOfType(fluid)) {
                adjacentCount++;
            }
        }
        if (fluid.canConvertToSource(level) && adjacentCount >= 2) {
            return level.getBlockState(pos.below()).isSolid() || level.getFluidState(pos.below()).isSourceOfType(fluid);
        }
        return false;
    }
}
