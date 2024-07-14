package com.verdantartifice.primalmagick.common.items.concoctions;

import com.verdantartifice.primalmagick.common.items.ItemsPM;

import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

/**
 * Definition of a bomb casing.  Used as a base item for making alchemical bombs.
 * 
 * @author Daedalus4096
 */
public class BombCasingItem extends AbstractConcoctionContainerItem {
    public static final CauldronInteraction FILL_BOMB = (BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack stack) -> {
        if (!level.isClientSide) {
            Item item = stack.getItem();
            player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, ItemsPM.ALCHEMICAL_BOMB.get().getDefaultInstance().copy()));
            player.awardStat(Stats.USE_CAULDRON);
            player.awardStat(Stats.ITEM_USED.get(item));
            LayeredCauldronBlock.lowerFillLevel(state, level, pos);
            level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.gameEvent(null, GameEvent.FLUID_PICKUP, pos);
        }
        return ItemInteractionResult.sidedSuccess(level.isClientSide);
    };
    
    public BombCasingItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    protected ItemStack getConcoctionContainerItem() {
        return ItemsPM.ALCHEMICAL_BOMB.get().getDefaultInstance().copy();
    }
}
