package com.verdantartifice.primalmagick.common.blocks.crops;

import com.verdantartifice.primalmagick.common.blocks.BlocksPM;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AttachedStemBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.block.StemGrownBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.ToolActions;

/**
 * Block definition for a hydromelon crop.  Hydromelons are similar to melons and pumpkins in that
 * they are grown from a stem.  Unlike regular melons, they do not split into slices when broken.
 * If stripped by an axe, the melon disintegrates into a source block of water.
 * 
 * @author Daedalus4096
 */
public class HydromelonBlock extends StemGrownBlock {
    public HydromelonBlock(Block.Properties properties) {
        super(properties);
    }

    @Override
    public StemBlock getStem() {
        return BlocksPM.HYRDOMELON_STEM.get();
    }

    @Override
    public AttachedStemBlock getAttachedStem() {
        return BlocksPM.ATTACHED_HYDROMELON_STEM.get();
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pPlayer != null && pPlayer.getItemInHand(pHand).canPerformAction(ToolActions.AXE_STRIP)) {
            // If the player right-clicks on the hydromelon with an axe, replace this block with water (or vapor if in the Nether)
            boolean shouldVaporize = pLevel.dimensionType().ultraWarm();
            RandomSource rng = pPlayer.getRandom();
            pLevel.playSound(pPlayer, pPos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);
            if (shouldVaporize) {
                pLevel.playSound(pPlayer, pPos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F + (pLevel.random.nextFloat() - pLevel.random.nextFloat()) * 0.8F);
                for (int index = 0; index < 8; index++) {
                    pLevel.addParticle(ParticleTypes.LARGE_SMOKE, pPos.getX() + rng.nextDouble(), pPos.getY() + rng.nextDouble(), pPos.getZ() + rng.nextDouble(), 0.0D, 0.0D, 0.0D);
                }
            }
            if (!pLevel.isClientSide) {
                if (shouldVaporize) {
                    pLevel.removeBlock(pPos, false);
                } else {
                    pLevel.setBlock(pPos, Blocks.WATER.defaultBlockState(), Block.UPDATE_ALL_IMMEDIATE);
                }
                pPlayer.getItemInHand(pHand).hurtAndBreak(1, pPlayer, (p) -> {
                    p.broadcastBreakEvent(pHand);
                });
            }
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.PASS;
        }
    }
}
