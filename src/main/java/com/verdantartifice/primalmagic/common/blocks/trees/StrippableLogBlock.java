package com.verdantartifice.primalmagic.common.blocks.trees;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.Constants;

/**
 * Definition for a log block that can be stripped with an axe.
 * 
 * @author Daedalus4096
 */
public class StrippableLogBlock extends RotatedPillarBlock {
    protected final Block strippedVersion;

    public StrippableLogBlock(Block stripped, Block.Properties properties) {
        super(properties);
        this.strippedVersion = stripped;
    }
    
    protected BlockState getDefaultStrippedState(BlockState originalState) {
        return this.strippedVersion.defaultBlockState().setValue(RotatedPillarBlock.AXIS, originalState.getValue(RotatedPillarBlock.AXIS));
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (this.strippedVersion != null && player != null && player.getItemInHand(handIn).getItem() instanceof AxeItem) {
            // If the player right-clicks on the log with an axe, replace this block with its stripped version
            worldIn.playSound(player, pos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);
            if (!worldIn.isClientSide) {
                worldIn.setBlock(pos, this.getDefaultStrippedState(state), Constants.BlockFlags.DEFAULT_AND_RERENDER);
                player.getItemInHand(handIn).hurtAndBreak(1, player, (p) -> {
                    p.broadcastBreakEvent(handIn);
                });
            }
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.PASS;
        }
    }
}
