package com.verdantartifice.primalmagick.common.network.packets.fx;

import com.verdantartifice.primalmagick.client.fx.FxDispatcher;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.NetworkDirection;

/**
 * Packet sent from the server to trigger a spell impact particle effect on the client.
 * 
 * @author Daedalus4096
 */
public class SpellImpactPacket implements IMessageToClient {
    protected double x;
    protected double y;
    protected double z;
    protected int radius;
    protected int color;
    
    public SpellImpactPacket() {}
    
    public SpellImpactPacket(double x, double y, double z, int radius, int color) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.radius = radius;
        this.color = color;
    }
    
    public static NetworkDirection direction() {
        return NetworkDirection.PLAY_TO_CLIENT;
    }
    
    public static void encode(SpellImpactPacket message, FriendlyByteBuf buf) {
        buf.writeDouble(message.x);
        buf.writeDouble(message.y);
        buf.writeDouble(message.z);
        buf.writeInt(message.radius);
        buf.writeInt(message.color);
    }
    
    public static SpellImpactPacket decode(FriendlyByteBuf buf) {
        SpellImpactPacket message = new SpellImpactPacket();
        message.x = buf.readDouble();
        message.y = buf.readDouble();
        message.z = buf.readDouble();
        message.radius = buf.readInt();
        message.color = buf.readInt();
        return message;
    }
    
    public static void onMessage(SpellImpactPacket message, CustomPayloadEvent.Context ctx) {
        FxDispatcher.INSTANCE.spellImpact(message.x, message.y, message.z, message.radius, message.color);
    }
}
