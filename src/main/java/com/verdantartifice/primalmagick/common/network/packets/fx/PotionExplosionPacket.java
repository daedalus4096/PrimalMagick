package com.verdantartifice.primalmagick.common.network.packets.fx;

import com.verdantartifice.primalmagick.client.fx.FxDispatcher;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.NetworkDirection;

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
    protected boolean isInstant;
    
    public PotionExplosionPacket() {}
    
    public PotionExplosionPacket(double x, double y, double z, int color, boolean isInstant) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.color = color;
        this.isInstant = isInstant;
    }
    
    public PotionExplosionPacket(Vec3 vec, int color, boolean isInstant) {
        this(vec.x, vec.y, vec.z, color, isInstant);
    }
    
    public static NetworkDirection direction() {
        return NetworkDirection.PLAY_TO_CLIENT;
    }
    
    public static void encode(PotionExplosionPacket message, FriendlyByteBuf buf) {
        buf.writeDouble(message.x);
        buf.writeDouble(message.y);
        buf.writeDouble(message.z);
        buf.writeVarInt(message.color);
        buf.writeBoolean(message.isInstant);
    }
    
    public static PotionExplosionPacket decode(FriendlyByteBuf buf) {
        PotionExplosionPacket message = new PotionExplosionPacket();
        message.x = buf.readDouble();
        message.y = buf.readDouble();
        message.z = buf.readDouble();
        message.color = buf.readVarInt();
        message.isInstant = buf.readBoolean();
        return message;
    }
    
    public static void onMessage(PotionExplosionPacket message, CustomPayloadEvent.Context ctx) {
        FxDispatcher.INSTANCE.potionExplosion(message.x, message.y, message.z, message.color, message.isInstant);
    }
}
