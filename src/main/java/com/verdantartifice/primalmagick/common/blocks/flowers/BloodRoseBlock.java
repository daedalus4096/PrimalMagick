package com.verdantartifice.primalmagick.common.blocks.flowers;

import com.verdantartifice.primalmagick.client.fx.FxDispatcher;
import com.verdantartifice.primalmagick.common.damagesource.DamageSourcesPM;
import com.verdantartifice.primalmagick.common.tags.EntityTypeTagsPM;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TallFlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

/**
 * Block definition for a blood rose.  Like a regular rose bush, but does damage like a cactus and drips.
 * 
 * @author Daedalus4096
 */
public class BloodRoseBlock extends TallFlowerBlock {
    public BloodRoseBlock(Block.Properties properties) {
        super(properties);
    }

    @Override
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
        if (pRandom.nextInt(10) == 0) {
            Vec3 offset = pState.getOffset(pLevel, pPos);
            double x = pPos.getX() + (0.1D + 0.8D * pRandom.nextDouble()) + offset.x();
            double y = pPos.getY() + (0.1D + 0.8D * pRandom.nextDouble());
            double z = pPos.getZ() + (0.1D + 0.8D * pRandom.nextDouble()) + offset.z();
            FxDispatcher.INSTANCE.bloodDrop(x, y, z);
        }
    }

    @Override
    public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
        // Don't destroy items and don't harm anything in the immunity tag
        if (pEntity.getType() != EntityType.ITEM && !pEntity.getType().is(EntityTypeTagsPM.BLOOD_ROSE_IMMUNE)) {
            pEntity.hurt(DamageSourcesPM.bloodRose(pLevel), 1F);
        }
    }
}
