package com.verdantartifice.primalmagick.common.items.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Definition of a debug item that forces a random tick for the block on which it's used.
 * 
 * @author Daedalus4096
 */
public class TickStickItem extends Item {
    public TickStickItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Level level = pContext.getLevel();
        if (!level.isClientSide && level instanceof ServerLevel serverLevel) {
            BlockPos pos = pContext.getClickedPos();
            BlockState state = level.getBlockState(pos);
            Player player = pContext.getPlayer();
            if (player.canUseGameMasterBlocks() && state.isRandomlyTicking()) {
                state.randomTick(serverLevel, pos, serverLevel.random);
                return InteractionResult.sidedSuccess(level.isClientSide);
            } else {
                return InteractionResult.FAIL;
            }
        } else {
            return super.useOn(pContext);
        }
    }

    @Override
    public boolean isFoil(ItemStack pStack) {
        return true;
    }
}
