package com.verdantartifice.primalmagick.common.items.misc;

import com.verdantartifice.primalmagick.common.advancements.criterion.CriteriaTriggersPM;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundLevelEventPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.TeleportTransition;
import org.jetbrains.annotations.NotNull;

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
    @NotNull
    public InteractionResult use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        if (!level.isClientSide() && level instanceof ServerLevel serverLevel && player instanceof ServerPlayer serverPlayer) {
            if (serverLevel.dimension().equals(ServerPlayer.RespawnConfig.getDimensionOrDefault(serverPlayer.getRespawnConfig()))) {
                // If the player's respawn point is in this dimension, teleport them to it
                TeleportTransition respawn = serverPlayer.findRespawnPositionAndUseSpawnBlock(true, TeleportTransition.DO_NOTHING);
                serverPlayer.teleportTo(serverLevel, respawn.position().x(), respawn.position().y(), respawn.position().z(), respawn.relatives(), respawn.yRot(), respawn.xRot(), true);
                serverPlayer.connection.send(new ClientboundLevelEventPacket(1032, BlockPos.ZERO, 0, false));   // Play the portal travel sound
                if (!player.hasInfiniteMaterials()) {
                    player.getItemInHand(hand).shrink(1);
                }
                CriteriaTriggersPM.RECALL_STONE.get().trigger(serverPlayer, serverLevel.dimension());
            } else {
                player.displayClientMessage(Component.translatable("event.primalmagick.recall_stone.cannot_cross_dimensions").withStyle(ChatFormatting.RED), true);
            }
        }
        return super.use(level, player, hand);
    }
}
