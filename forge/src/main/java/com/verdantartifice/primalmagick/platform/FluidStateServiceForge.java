package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.platform.services.IFluidStateService;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.material.FluidState;

public class FluidStateServiceForge implements IFluidStateService {
    @Override
    public float getExplosionResistance(FluidState state, BlockGetter level, BlockPos pos, Explosion explosion) {
        return state.getExplosionResistance(level, pos, explosion);
    }
}
