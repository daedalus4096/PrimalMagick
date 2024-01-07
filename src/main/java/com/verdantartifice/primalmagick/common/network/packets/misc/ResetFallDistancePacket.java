package com.verdantartifice.primalmagick.common.network.packets.misc;

import com.verdantartifice.primalmagick.common.network.packets.IMessageToServer;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.NetworkDirection;

/**
 * Packet sent to reset the server-side fall distance of the sending player.
 * 
 * @author Daedalus4096
 */
public class ResetFallDistancePacket implements IMessageToServer {
    public ResetFallDistancePacket() {}
    
    public static NetworkDirection direction() {
        return NetworkDirection.PLAY_TO_SERVER;
    }
    
    public static void encode(ResetFallDistancePacket message, FriendlyByteBuf buf) {}
    
    public static ResetFallDistancePacket decode(FriendlyByteBuf buf) {
        return new ResetFallDistancePacket();
    }
    
    public static void onMessage(ResetFallDistancePacket message, CustomPayloadEvent.Context ctx) {
        ctx.getSender().fallDistance = 0.0F;
    }
}
