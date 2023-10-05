package com.verdantartifice.primalmagick.common.blocks.flowers;

import com.verdantartifice.primalmagick.common.damagesource.DamageSourcesPM;
import com.verdantartifice.primalmagick.common.tags.EntityTypeTagsPM;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TallFlowerBlock;
import net.minecraft.world.level.block.state.BlockState;

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
        // TODO Auto-generated method stub
        super.animateTick(pState, pLevel, pPos, pRandom);
    }

    @Override
    public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
        if (!pEntity.getType().is(EntityTypeTagsPM.BLOOD_ROSE_IMMUNE)) {
            pEntity.hurt(DamageSourcesPM.bloodRose(pLevel), 1F);
        }
    }
}
