package com.verdantartifice.primalmagick.common.network.packets.fx;

import com.verdantartifice.primalmagick.client.fx.FxDispatcher;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraftforge.event.network.CustomPayloadEvent;

/**
 * Packet sent from the server to trigger a spell impact particle effect on the client.
 * 
 * @author Daedalus4096
 */
public class SpellImpactPacket implements IMessageToClient {
    public static final StreamCodec<RegistryFriendlyByteBuf, SpellImpactPacket> STREAM_CODEC = StreamCodec.ofMember(SpellImpactPacket::encode, SpellImpactPacket::decode);

    protected final double x;
    protected final double y;
    protected final double z;
    protected final int radius;
    protected final int color;
    
    public SpellImpactPacket(double x, double y, double z, int radius, int color) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.radius = radius;
        this.color = color;
    }
    
    public static void encode(SpellImpactPacket message, RegistryFriendlyByteBuf buf) {
        buf.writeDouble(message.x);
        buf.writeDouble(message.y);
        buf.writeDouble(message.z);
        buf.writeVarInt(message.radius);
        buf.writeVarInt(message.color);
    }
    
    public static SpellImpactPacket decode(RegistryFriendlyByteBuf buf) {
        return new SpellImpactPacket(buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readVarInt(), buf.readVarInt());
    }
    
    public static void onMessage(SpellImpactPacket message, CustomPayloadEvent.Context ctx) {
        FxDispatcher.INSTANCE.spellImpact(message.x, message.y, message.z, message.radius, message.color);
    }
}
