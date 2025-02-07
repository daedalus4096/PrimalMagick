package com.verdantartifice.primalmagick.common.network.packets.fx;

import com.verdantartifice.primalmagick.client.fx.FxDispatcher;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import commonnetwork.networking.data.PacketContext;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/**
 * Packet sent from the server to trigger a teleport arrival particle effect on the client.
 * 
 * @author Daedalus4096
 */
public class TeleportArrivalPacket implements IMessageToClient {
    public static final ResourceLocation CHANNEL = ResourceUtils.loc("teleport_arrival");
    public static final StreamCodec<RegistryFriendlyByteBuf, TeleportArrivalPacket> STREAM_CODEC = StreamCodec.ofMember(TeleportArrivalPacket::encode, TeleportArrivalPacket::decode);

    protected final double x;
    protected final double y;
    protected final double z;
    
    public TeleportArrivalPacket(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static CustomPacketPayload.Type<CustomPacketPayload> type() {
        return new CustomPacketPayload.Type<>(CHANNEL);
    }

    public static void encode(TeleportArrivalPacket message, RegistryFriendlyByteBuf buf) {
        buf.writeDouble(message.x);
        buf.writeDouble(message.y);
        buf.writeDouble(message.z);
    }
    
    public static TeleportArrivalPacket decode(RegistryFriendlyByteBuf buf) {
        return new TeleportArrivalPacket(buf.readDouble(), buf.readDouble(), buf.readDouble());
    }
    
    public static void onMessage(PacketContext<TeleportArrivalPacket> ctx) {
        TeleportArrivalPacket message = ctx.message();
        FxDispatcher.INSTANCE.teleportArrival(message.x, message.y, message.z);
    }
}
