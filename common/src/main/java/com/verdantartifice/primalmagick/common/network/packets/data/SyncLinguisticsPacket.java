package com.verdantartifice.primalmagick.common.network.packets.data;

import com.verdantartifice.primalmagick.client.util.ClientUtils;
import com.verdantartifice.primalmagick.common.capabilities.PlayerLinguistics;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import com.verdantartifice.primalmagick.platform.Services;
import commonnetwork.networking.data.PacketContext;
import commonnetwork.networking.data.Side;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

/**
 * Packet to sync linguistics capability data from the server to the client.
 * 
 * @author Daedalus4096
 */
public class SyncLinguisticsPacket implements IMessageToClient {
    public static final Identifier CHANNEL = ResourceUtils.loc("sync_linguistics");
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncLinguisticsPacket> STREAM_CODEC = StreamCodec.composite(
            PlayerLinguistics.STREAM_CODEC, p -> p.packetLinguistics,
            SyncLinguisticsPacket::new);

    protected final PlayerLinguistics packetLinguistics;

    public SyncLinguisticsPacket(PlayerLinguistics linguistics) {
        this.packetLinguistics = linguistics;
    }
    
    public static CustomPacketPayload.Type<CustomPacketPayload> type() {
        return new CustomPacketPayload.Type<>(CHANNEL);
    }

    public static void onMessage(PacketContext<SyncLinguisticsPacket> ctx) {
        Player player = Side.CLIENT.equals(ctx.side()) ? ClientUtils.getCurrentPlayer() : null;
        if (player != null) {
            Services.CAPABILITIES.linguistics(player).ifPresent(linguistics -> {
                if (linguistics instanceof PlayerLinguistics playerLinguistics) {
                    playerLinguistics.copyFrom(ctx.message().packetLinguistics);
                }
            });
        }
    }
}
