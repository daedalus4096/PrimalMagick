package com.verdantartifice.primalmagick.platform.services;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.block.state.BlockState;

public interface IBlockStateService {
    float getExplosionResistance(BlockState state, BlockGetter level, BlockPos pos, Explosion explosion);
}
