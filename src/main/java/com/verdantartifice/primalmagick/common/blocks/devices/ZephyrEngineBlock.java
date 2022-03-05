package com.verdantartifice.primalmagick.common.blocks.devices;

import com.verdantartifice.primalmagick.common.sources.Source;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Definition of a wind generator block that pushes entities away.
 *  
 * @author Daedalus4096
 */
public class ZephyrEngineBlock extends AbstractWindGeneratorBlock {
    @Override
    protected Direction getWindDirection(BlockState state) {
        return state.getValue(FACING);
    }

    @Override
    protected int getParticleColor() {
        return Source.SKY.getColor();
    }
}
