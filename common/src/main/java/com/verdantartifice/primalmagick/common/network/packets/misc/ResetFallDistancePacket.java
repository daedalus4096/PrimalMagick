package com.verdantartifice.primalmagick.common.network.packets.misc;

import com.verdantartifice.primalmagick.common.network.packets.IMessageToServer;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import commonnetwork.networking.data.PacketContext;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/**
 * Packet sent to reset the server-side fall distance of the sending player.
 * 
 * @author Daedalus4096
 */
public class ResetFallDistancePacket implements IMessageToServer {
    public static final ResourceLocation CHANNEL = ResourceUtils.loc("reset_fall_distance");
    public static final StreamCodec<RegistryFriendlyByteBuf, ResetFallDistancePacket> STREAM_CODEC = StreamCodec.ofMember(ResetFallDistancePacket::encode, ResetFallDistancePacket::decode);

    public ResetFallDistancePacket() {}

    public static CustomPacketPayload.Type<CustomPacketPayload> type() {
        return new CustomPacketPayload.Type<>(CHANNEL);
    }

    public static void encode(ResetFallDistancePacket message, RegistryFriendlyByteBuf buf) {}
    
    public static ResetFallDistancePacket decode(RegistryFriendlyByteBuf buf) {
        return new ResetFallDistancePacket();
    }
    
    public static void onMessage(PacketContext<ResetFallDistancePacket> ctx) {
        ctx.sender().fallDistance = 0.0F;
    }
}
