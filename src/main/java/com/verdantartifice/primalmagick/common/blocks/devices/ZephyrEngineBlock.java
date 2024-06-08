package com.verdantartifice.primalmagick.common.blocks.devices;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.client.fx.particles.ParticleTypesPM;
import com.verdantartifice.primalmagick.common.sources.Sources;

import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Definition of a wind generator block that pushes entities away.
 *  
 * @author Daedalus4096
 */
public class ZephyrEngineBlock extends AbstractWindGeneratorBlock {
    public static final MapCodec<ZephyrEngineBlock> CODEC = simpleCodec(ZephyrEngineBlock::new);
    
    public ZephyrEngineBlock(Block.Properties properties) {
        super(properties);
    }
    
    @Override
    public Direction getWindDirection(BlockState state) {
        return state.getValue(FACING);
    }

    @Override
    public int getCoreColor() {
        return Sources.SKY.getColor();
    }

    @Override
    public ParticleOptions getParticleType() {
        return ParticleTypesPM.AIR_CURRENT.get();
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}
