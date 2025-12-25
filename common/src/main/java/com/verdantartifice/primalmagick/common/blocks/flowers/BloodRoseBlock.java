package com.verdantartifice.primalmagick.common.blocks.flowers;

import com.verdantartifice.primalmagick.client.fx.FxDispatcher;
import com.verdantartifice.primalmagick.common.damagesource.DamageSourcesPM;
import com.verdantartifice.primalmagick.common.tags.EntityTypeTagsPM;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TallFlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

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
    public void animateTick(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull RandomSource pRandom) {
        if (pRandom.nextInt(10) == 0) {
            Vec3 offset = pState.getOffset(pPos);
            double x = pPos.getX() + (0.1D + 0.8D * pRandom.nextDouble()) + offset.x();
            double y = pPos.getY() + (0.1D + 0.8D * pRandom.nextDouble());
            double z = pPos.getZ() + (0.1D + 0.8D * pRandom.nextDouble()) + offset.z();
            FxDispatcher.INSTANCE.bloodDrop(x, y, z);
        }
    }

    @Override
    public void entityInside(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos,
                             @NotNull Entity pEntity, @NotNull InsideBlockEffectApplier applier, boolean intersects) {
        // Don't destroy items and don't harm anything in the immunity tag
        if ( pLevel instanceof ServerLevel serverLevel &&
             pEntity.getType() != EntityType.ITEM &&
             !pEntity.getType().is(EntityTypeTagsPM.BLOOD_ROSE_IMMUNE) ) {
            pEntity.hurtServer(serverLevel, DamageSourcesPM.bloodRose(serverLevel.registryAccess()), 1F);
        }
    }
}
