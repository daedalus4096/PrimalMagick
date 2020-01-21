package com.verdantartifice.primalmagic.common.network.packets.fx;

import java.util.function.Supplier;

import com.verdantartifice.primalmagic.client.fx.FxDispatcher;
import com.verdantartifice.primalmagic.common.network.packets.IMessageToClient;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

/**
 * Packet sent from the server to trigger a teleport arrival particle effect on the client.
 * 
 * @author Daedalus4096
 */
public class TeleportArrivalPacket implements IMessageToClient {
    protected double x;
    protected double y;
    protected double z;
    
    public TeleportArrivalPacket() {}
    
    public TeleportArrivalPacket(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public static void encode(TeleportArrivalPacket message, PacketBuffer buf) {
        buf.writeDouble(message.x);
        buf.writeDouble(message.y);
        buf.writeDouble(message.z);
    }
    
    public static TeleportArrivalPacket decode(PacketBuffer buf) {
        TeleportArrivalPacket message = new TeleportArrivalPacket();
        message.x = buf.readDouble();
        message.y = buf.readDouble();
        message.z = buf.readDouble();
        return message;
    }
    
    public static class Handler {
        public static void onMessage(TeleportArrivalPacket message, Supplier<NetworkEvent.Context> ctx) {
            // Enqueue the handler work on the main game thread
            ctx.get().enqueueWork(() -> {
                FxDispatcher.INSTANCE.teleportArrival(message.x, message.y, message.z);
            });
            
            // Mark the packet as handled so we don't get warning log spam
            ctx.get().setPacketHandled(true);
        }
    }
}
