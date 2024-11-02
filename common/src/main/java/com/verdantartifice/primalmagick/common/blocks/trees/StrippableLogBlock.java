package com.verdantartifice.primalmagick.common.blocks.trees;

import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

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
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (this.strippedVersion != null && player != null && Services.ITEM_ABILITIES.canAxeStrip(stack)) {
            // If the player right-clicks on the log with an axe, replace this block with its stripped version
            worldIn.playSound(player, pos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);
            if (!worldIn.isClientSide) {
                worldIn.setBlock(pos, this.getDefaultStrippedState(state), Block.UPDATE_ALL_IMMEDIATE);
                stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(handIn));
            }
            return ItemInteractionResult.SUCCESS;
        } else {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
    }
}
