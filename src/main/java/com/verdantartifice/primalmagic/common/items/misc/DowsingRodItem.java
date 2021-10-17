package com.verdantartifice.primalmagic.common.items.misc;

import com.verdantartifice.primalmagic.common.tiles.rituals.RitualAltarTileEntity;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * Definition of an item for checking ritual altar and prop status.
 * 
 * @author Daedalus4096
 */
public class DowsingRodItem extends Item {
    public DowsingRodItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (!level.isClientSide) {
            ItemStack stack = context.getItemInHand();
            BlockPos targetPos = context.getClickedPos();
            BlockEntity blockEntity = level.getBlockEntity(targetPos);
            Player player = context.getPlayer();
            if (blockEntity instanceof RitualAltarTileEntity altarEntity) {
                player.sendMessage(new TranslatableComponent("event.primalmagic.dowsing_rod.altar_stability", altarEntity.calculateStabilityDelta()), Util.NIL_UUID);
                if (!player.getAbilities().instabuild) {
                    stack.hurtAndBreak(1, player, p -> {
                        p.broadcastBreakEvent(EquipmentSlot.MAINHAND);
                    });
                }
                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.PASS;
            }
        } else {
            return InteractionResult.PASS;
        }
    }
}
