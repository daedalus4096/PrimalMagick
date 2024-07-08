package com.verdantartifice.primalmagick.common.network.packets.misc;

import com.verdantartifice.primalmagick.common.network.packets.IMessageToServer;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraftforge.event.network.CustomPayloadEvent;

/**
 * Packet sent to reset the server-side fall distance of the sending player.
 * 
 * @author Daedalus4096
 */
public class ResetFallDistancePacket implements IMessageToServer {
    public static final StreamCodec<RegistryFriendlyByteBuf, ResetFallDistancePacket> STREAM_CODEC = StreamCodec.ofMember(ResetFallDistancePacket::encode, ResetFallDistancePacket::decode);

    public ResetFallDistancePacket() {}
    
    public static void encode(ResetFallDistancePacket message, RegistryFriendlyByteBuf buf) {}
    
    public static ResetFallDistancePacket decode(RegistryFriendlyByteBuf buf) {
        return new ResetFallDistancePacket();
    }
    
    public static void onMessage(ResetFallDistancePacket message, CustomPayloadEvent.Context ctx) {
        ctx.getSender().fallDistance = 0.0F;
    }
}
