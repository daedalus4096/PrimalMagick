package com.verdantartifice.primalmagick.common.items.misc;

import java.util.Optional;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundLevelEventPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * Definition of an item that teleports the player back to their spawn point.
 * 
 * @author Daedalus4096
 */
public class RecallStoneItem extends Item {
    public RecallStoneItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide && level instanceof ServerLevel serverLevel && player instanceof ServerPlayer serverPlayer) {
            if (serverLevel.dimension().equals(serverPlayer.getRespawnDimension())) {
                // If the player's respawn point is in this dimension, teleport them to it
                BlockPos respawnBlockPos = serverPlayer.getRespawnPosition();
                float angle = serverPlayer.getRespawnAngle();
                boolean forced = serverPlayer.isRespawnForced();
                Optional<Vec3> respawnVecOpt;
                if (serverLevel != null && respawnBlockPos != null) {
                    // Pass the last value as true to simulate game win and prevent consuming respawn anchor charges
                    respawnVecOpt = Player.findRespawnPositionAndUseSpawnBlock(serverLevel, respawnBlockPos, angle, forced, true);
                } else {
                    respawnVecOpt = Optional.empty();
                }
                
                respawnVecOpt.ifPresentOrElse(respawnVec -> {
                    serverPlayer.teleportTo(serverLevel, respawnVec.x, respawnVec.y, respawnVec.z, angle, 0F);
                    serverPlayer.connection.send(new ClientboundLevelEventPacket(1032, BlockPos.ZERO, 0, false));   // Play the portal travel sound
                    if (!player.getAbilities().instabuild) {
                        player.getItemInHand(hand).shrink(1);
                    }
                }, () -> {
                    player.displayClientMessage(Component.translatable("event.primalmagick.recall_stone.failure").withStyle(ChatFormatting.RED), true);
                });
            } else {
                player.displayClientMessage(Component.translatable("event.primalmagick.recall_stone.cannot_cross_dimensions").withStyle(ChatFormatting.RED), true);
            }
        }
        return super.use(level, player, hand);
    }
}
