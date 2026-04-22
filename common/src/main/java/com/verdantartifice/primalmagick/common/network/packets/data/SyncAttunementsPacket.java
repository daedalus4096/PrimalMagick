package com.verdantartifice.primalmagick.common.network.packets.data;

import com.verdantartifice.primalmagick.client.util.ClientUtils;
import com.verdantartifice.primalmagick.common.capabilities.PlayerAttunements;
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
 * Packet to sync attunements capability data from the server to the client.
 * 
 * @author Daedalus4096
 */
public class SyncAttunementsPacket implements IMessageToClient {
    public static final Identifier CHANNEL = ResourceUtils.loc("sync_attunements");
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncAttunementsPacket> STREAM_CODEC = StreamCodec.composite(
            PlayerAttunements.STREAM_CODEC, p -> p.packetAttunements,
            SyncAttunementsPacket::new);

    protected final PlayerAttunements packetAttunements;

    public SyncAttunementsPacket(PlayerAttunements attunements) {
        this.packetAttunements = attunements;
    }

    public static CustomPacketPayload.Type<CustomPacketPayload> type() {
        return new CustomPacketPayload.Type<>(CHANNEL);
    }

    public static void onMessage(PacketContext<SyncAttunementsPacket> ctx) {
        Player player = Side.CLIENT.equals(ctx.side()) ? ClientUtils.getCurrentPlayer() : null;
        if (player != null) {
            Services.CAPABILITIES.attunements(player).ifPresent(attunements -> {
                if (attunements instanceof PlayerAttunements playerAttunements) {
                    playerAttunements.copyFrom(ctx.message().packetAttunements);
                }
            });
        }
    }
}
