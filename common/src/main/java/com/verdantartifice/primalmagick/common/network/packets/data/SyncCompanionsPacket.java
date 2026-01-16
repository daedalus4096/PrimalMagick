package com.verdantartifice.primalmagick.common.network.packets.data;

import com.verdantartifice.primalmagick.client.util.ClientUtils;
import com.verdantartifice.primalmagick.common.capabilities.PlayerCompanions;
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
 * Packet to sync companion capability data from the server to the client.
 * 
 * @author Daedalus4096
 */
public class SyncCompanionsPacket implements IMessageToClient {
    public static final Identifier CHANNEL = ResourceUtils.loc("sync_companions");
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncCompanionsPacket> STREAM_CODEC = StreamCodec.composite(
            PlayerCompanions.STREAM_CODEC, p -> p.packetCompanions,
            SyncCompanionsPacket::new);

    protected final PlayerCompanions packetCompanions;

    public SyncCompanionsPacket(PlayerCompanions companions) {
        this.packetCompanions = companions;
    }
    
    public static CustomPacketPayload.Type<CustomPacketPayload> type() {
        return new CustomPacketPayload.Type<>(CHANNEL);
    }

    public static void onMessage(PacketContext<SyncCompanionsPacket> ctx) {
        Player player = Side.CLIENT.equals(ctx.side()) ? ClientUtils.getCurrentPlayer() : null;
        if (player != null) {
            Services.CAPABILITIES.companions(player).ifPresent(companions -> {
                if (companions instanceof PlayerCompanions playerCompanions) {
                    playerCompanions.copyFrom(ctx.message().packetCompanions);
                }
            });
        }
    }
}
