package com.verdantartifice.primalmagick.common.network.packets.fx;

import com.verdantartifice.primalmagick.client.fx.FxDispatcher;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.network.CustomPayloadEvent;

/**
 * Packet sent from the server to trigger a potion explosion particle effect on the client.
 * 
 * @author Daedalus4096
 */
public class PotionExplosionPacket implements IMessageToClient {
    public static final StreamCodec<RegistryFriendlyByteBuf, PotionExplosionPacket> STREAM_CODEC = StreamCodec.ofMember(PotionExplosionPacket::encode, PotionExplosionPacket::decode);

    protected final double x;
    protected final double y;
    protected final double z;
    protected final int color;
    protected final boolean isInstant;
    
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
    
    public static void encode(PotionExplosionPacket message, RegistryFriendlyByteBuf buf) {
        buf.writeDouble(message.x);
        buf.writeDouble(message.y);
        buf.writeDouble(message.z);
        buf.writeVarInt(message.color);
        buf.writeBoolean(message.isInstant);
    }
    
    public static PotionExplosionPacket decode(RegistryFriendlyByteBuf buf) {
        return new PotionExplosionPacket(buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readVarInt(), buf.readBoolean());
    }
    
    public static void onMessage(PotionExplosionPacket message, CustomPayloadEvent.Context ctx) {
        FxDispatcher.INSTANCE.potionExplosion(message.x, message.y, message.z, message.color, message.isInstant);
    }
}
