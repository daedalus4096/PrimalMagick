package com.verdantartifice.primalmagic.common.network.packets.fx;

import java.util.function.Supplier;

import com.verdantartifice.primalmagic.client.fx.FxDispatcher;
import com.verdantartifice.primalmagic.common.network.packets.IMessageToClient;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.fml.network.NetworkEvent;

/**
 * Packet sent from the server to trigger a potion explosion particle effect on the client.
 * 
 * @author Daedalus4096
 */
public class PotionExplosionPacket implements IMessageToClient {
    protected double x;
    protected double y;
    protected double z;
    protected int color;
    
    public PotionExplosionPacket() {}
    
    public PotionExplosionPacket(double x, double y, double z, int color) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.color = color;
    }
    
    public PotionExplosionPacket(Vector3d vec, int color) {
        this(vec.x, vec.y, vec.z, color);
    }
    
    public static void encode(PotionExplosionPacket message, PacketBuffer buf) {
        buf.writeDouble(message.x);
        buf.writeDouble(message.y);
        buf.writeDouble(message.z);
        buf.writeVarInt(message.color);
    }
    
    public static PotionExplosionPacket decode(PacketBuffer buf) {
        PotionExplosionPacket message = new PotionExplosionPacket();
        message.x = buf.readDouble();
        message.y = buf.readDouble();
        message.z = buf.readDouble();
        message.color = buf.readVarInt();
        return message;
    }
    
    public static class Handler {
        public static void onMessage(PotionExplosionPacket message, Supplier<NetworkEvent.Context> ctx) {
            // Enqueue the handler work on the main game thread
            ctx.get().enqueueWork(() -> {
                FxDispatcher.INSTANCE.potionExplosion(message.x, message.y, message.z, message.color);
            });
            
            // Mark the packet as handled so we don't get warning log spam
            ctx.get().setPacketHandled(true);
        }
    }
}
