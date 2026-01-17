package com.verdantartifice.primalmagick.common.network.packets.data;

import com.verdantartifice.primalmagick.client.util.ClientUtils;
import com.verdantartifice.primalmagick.common.capabilities.PlayerWard;
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
 * Packet to sync ward capability data from the server to the client.
 * 
 * @author Daedalus4096
 */
public class SyncWardPacket implements IMessageToClient {
    public static final Identifier CHANNEL = ResourceUtils.loc("sync_ward");
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncWardPacket> STREAM_CODEC = StreamCodec.composite(
            PlayerWard.STREAM_CODEC, p -> p.packetWard,
            SyncWardPacket::new);

    protected final PlayerWard packetWard;

    public SyncWardPacket(PlayerWard ward) {
        this.packetWard = ward;
    }
    
    public static CustomPacketPayload.Type<CustomPacketPayload> type() {
        return new CustomPacketPayload.Type<>(CHANNEL);
    }

    public static void onMessage(PacketContext<SyncWardPacket> ctx) {
        Player player = Side.CLIENT.equals(ctx.side()) ? ClientUtils.getCurrentPlayer() : null;
        if (player != null) {
            Services.CAPABILITIES.ward(player).ifPresent(ward -> {
                if (ward instanceof PlayerWard playerWard) {
                    playerWard.copyFrom(ctx.message().packetWard);
                }
            });
        }
    }
}
