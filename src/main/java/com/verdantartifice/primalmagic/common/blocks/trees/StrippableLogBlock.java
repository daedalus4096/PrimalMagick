package com.verdantartifice.primalmagic.common.blocks.trees;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
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
        return this.strippedVersion.getDefaultState().with(RotatedPillarBlock.AXIS, originalState.get(RotatedPillarBlock.AXIS));
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (this.strippedVersion != null && player != null && player.getHeldItem(handIn).getItem() instanceof AxeItem) {
            // If the player right-clicks on the log with an axe, replace this block with its stripped version
            worldIn.playSound(player, pos, SoundEvents.ITEM_AXE_STRIP, SoundCategory.BLOCKS, 1.0F, 1.0F);
            if (!worldIn.isRemote) {
                worldIn.setBlockState(pos, this.getDefaultStrippedState(state), Constants.BlockFlags.DEFAULT_AND_RERENDER);
                player.getHeldItem(handIn).damageItem(1, player, (p) -> {
                    p.sendBreakAnimation(handIn);
                });
            }
            return ActionResultType.SUCCESS;
        } else {
            return ActionResultType.PASS;
        }
    }
}
