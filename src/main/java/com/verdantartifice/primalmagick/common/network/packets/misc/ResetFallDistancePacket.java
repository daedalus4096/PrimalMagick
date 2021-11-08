package com.verdantartifice.primalmagick.common.network.packets.misc;

import java.util.function.Supplier;

import com.verdantartifice.primalmagick.common.network.packets.IMessageToServer;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

/**
 * Packet sent to reset the server-side fall distance of the sending player.
 * 
 * @author Daedalus4096
 */
public class ResetFallDistancePacket implements IMessageToServer {
    public ResetFallDistancePacket() {}
    
    public static void encode(ResetFallDistancePacket message, FriendlyByteBuf buf) {}
    
    public static ResetFallDistancePacket decode(FriendlyByteBuf buf) {
        return new ResetFallDistancePacket();
    }
    
    public static class Handler {
        public static void onMessage(ResetFallDistancePacket message, Supplier<NetworkEvent.Context> ctx) {
            // Enqueue the handler work on the main game thread
            ctx.get().enqueueWork(() -> {
                ctx.get().getSender().fallDistance = 0.0F;
            });
            
            // Mark the packet as handled so we don't get warning log spam
            ctx.get().setPacketHandled(true);
        }
    }
}
