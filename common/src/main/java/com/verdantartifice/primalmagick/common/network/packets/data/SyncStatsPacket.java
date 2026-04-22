package com.verdantartifice.primalmagick.common.network.packets.data;

import com.verdantartifice.primalmagick.client.util.ClientUtils;
import com.verdantartifice.primalmagick.common.capabilities.PlayerStats;
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
 * Packet to sync statistics capability data from the server to the client.
 * 
 * @author Daedalus4096
 */
public class SyncStatsPacket implements IMessageToClient {
    public static final Identifier CHANNEL = ResourceUtils.loc("sync_stats");
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncStatsPacket> STREAM_CODEC = StreamCodec.composite(
            PlayerStats.STREAM_CODEC, p -> p.packetStats,
            SyncStatsPacket::new);

    protected final PlayerStats packetStats;

    public SyncStatsPacket(PlayerStats stats) {
        this.packetStats = stats;
    }

    public static CustomPacketPayload.Type<CustomPacketPayload> type() {
        return new CustomPacketPayload.Type<>(CHANNEL);
    }

    public static void onMessage(PacketContext<SyncStatsPacket> ctx) {
        Player player = Side.CLIENT.equals(ctx.side()) ? ClientUtils.getCurrentPlayer() : null;
        if (player != null) {
            Services.CAPABILITIES.stats(player).ifPresent(stats -> {
                if (stats instanceof PlayerStats playerStats) {
                    playerStats.copyFrom(ctx.message().packetStats);
                }
            });
        }
    }
}
