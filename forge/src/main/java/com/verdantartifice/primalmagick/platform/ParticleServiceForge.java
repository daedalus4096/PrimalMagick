package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.platform.services.IParticleService;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.level.block.state.BlockState;

public class ParticleServiceForge implements IParticleService {
    @Override
    public BlockParticleOption makeBlockParticleOptionWithPos(ParticleType<BlockParticleOption> type, BlockState state, BlockPos pos) {
        return new BlockParticleOption(type, state).setPos(pos);
    }
}
