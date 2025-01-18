package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.platform.services.IBlockStateService;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.block.state.BlockState;

public class BlockStateServiceForge implements IBlockStateService {
    @Override
    public float getExplosionResistance(BlockState state, BlockGetter level, BlockPos pos, Explosion explosion) {
        return state.getExplosionResistance(level, pos, explosion);
    }
}
