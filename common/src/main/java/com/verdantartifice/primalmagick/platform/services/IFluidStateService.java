package com.verdantartifice.primalmagick.platform.services;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.material.FluidState;

public interface IFluidStateService {
    float getExplosionResistance(FluidState state, BlockGetter level, BlockPos pos, Explosion explosion);
}
