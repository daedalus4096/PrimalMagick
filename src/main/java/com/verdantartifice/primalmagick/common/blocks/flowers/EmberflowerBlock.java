package com.verdantartifice.primalmagick.common.blocks.flowers;

import com.verdantartifice.primalmagick.client.fx.particles.ParticleTypesPM;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TallFlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.Vec3;

/**
 * Block definition for an emberflower.  Like a sunflower, but it emits light and flame particles.
 * 
 * @author Daedalus4096
 */
public class EmberflowerBlock extends TallFlowerBlock {
    public EmberflowerBlock(Block.Properties properties) {
        super(properties);
    }

    @Override
    protected boolean mayPlaceOn(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return super.mayPlaceOn(pState, pLevel, pPos) || pState.is(BlockTags.SOUL_FIRE_BASE_BLOCKS);
    }

    @Override
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
        if (pRandom.nextInt(5) == 0 && pState.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.UPPER) {
            Vec3 offset = pState.getOffset(pLevel, pPos);
            double x = pPos.getX() + (0.3D + 0.4D * pRandom.nextDouble()) + offset.x();
            double y = pPos.getY() + (0.3D + 0.4D * pRandom.nextDouble());
            double z = pPos.getZ() + (0.3D + 0.4D * pRandom.nextDouble()) + offset.z();
            pLevel.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0D, 0.0D, 0.0D);
            pLevel.addParticle(ParticleTypesPM.INFERNAL_FLAME.get(), x, y, z, 0.0D, 0.0D, 0.0D);
        }
    }
}
