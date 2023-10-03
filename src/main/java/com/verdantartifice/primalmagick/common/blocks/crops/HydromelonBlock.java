package com.verdantartifice.primalmagick.common.blocks.crops;

import com.verdantartifice.primalmagick.common.blocks.BlocksPM;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AttachedStemBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.block.StemGrownBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

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
        // TODO Auto-generated method stub
        return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }
}
