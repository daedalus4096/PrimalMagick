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
import org.jetbrains.annotations.NotNull;

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
    protected boolean mayPlaceOn(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos) {
        return super.mayPlaceOn(pState, pLevel, pPos) || pState.is(BlockTags.SOUL_FIRE_BASE_BLOCKS);
    }

    @Override
    public void animateTick(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull RandomSource pRandom) {
        if (pRandom.nextInt(5) == 0 && pState.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.UPPER) {
            Vec3 offset = pState.getOffset(pPos);
            double x = pPos.getX() + (0.3D + 0.4D * pRandom.nextDouble()) + offset.x();
            double y = pPos.getY() + (0.3D + 0.4D * pRandom.nextDouble());
            double z = pPos.getZ() + (0.3D + 0.4D * pRandom.nextDouble()) + offset.z();
            pLevel.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0D, 0.0D, 0.0D);
            pLevel.addParticle(ParticleTypesPM.INFERNAL_FLAME.get(), x, y, z, 0.0D, 0.0D, 0.0D);
        }
    }
}
