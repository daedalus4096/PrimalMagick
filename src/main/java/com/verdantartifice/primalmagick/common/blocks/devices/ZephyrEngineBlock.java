package com.verdantartifice.primalmagick.common.blocks.devices;

import com.verdantartifice.primalmagick.client.fx.particles.ParticleTypesPM;
import com.verdantartifice.primalmagick.common.sources.Source;

import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Definition of a wind generator block that pushes entities away.
 *  
 * @author Daedalus4096
 */
public class ZephyrEngineBlock extends AbstractWindGeneratorBlock {
    @Override
    public Direction getWindDirection(BlockState state) {
        return state.getValue(FACING);
    }

    @Override
    public int getCoreColor() {
        return Source.SKY.getColor();
    }

    @Override
    public ParticleOptions getParticleType() {
        return ParticleTypesPM.AIR_CURRENT.get();
    }
}
