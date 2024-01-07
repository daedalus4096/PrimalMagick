package com.verdantartifice.primalmagick.common.network.packets.fx;

import com.verdantartifice.primalmagick.client.fx.FxDispatcher;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.NetworkDirection;

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
    
    public static NetworkDirection direction() {
        return NetworkDirection.PLAY_TO_CLIENT;
    }
    
    public static void encode(TeleportArrivalPacket message, FriendlyByteBuf buf) {
        buf.writeDouble(message.x);
        buf.writeDouble(message.y);
        buf.writeDouble(message.z);
    }
    
    public static TeleportArrivalPacket decode(FriendlyByteBuf buf) {
        TeleportArrivalPacket message = new TeleportArrivalPacket();
        message.x = buf.readDouble();
        message.y = buf.readDouble();
        message.z = buf.readDouble();
        return message;
    }
    
    public static void onMessage(TeleportArrivalPacket message, CustomPayloadEvent.Context ctx) {
        FxDispatcher.INSTANCE.teleportArrival(message.x, message.y, message.z);
    }
}
