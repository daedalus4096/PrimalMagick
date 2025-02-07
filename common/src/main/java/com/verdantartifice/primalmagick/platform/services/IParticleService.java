package com.verdantartifice.primalmagick.platform.services;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.level.block.state.BlockState;

public interface IParticleService {
    BlockParticleOption makeBlockParticleOptionWithPos(ParticleType<BlockParticleOption> type, BlockState state, BlockPos pos);
}
