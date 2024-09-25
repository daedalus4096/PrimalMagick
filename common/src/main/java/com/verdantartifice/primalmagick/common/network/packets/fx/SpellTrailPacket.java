package com.verdantartifice.primalmagick.common.network.packets.fx;

import com.verdantartifice.primalmagick.client.fx.FxDispatcher;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.network.CustomPayloadEvent;

/**
 * Packet sent from the server to trigger a spell trail particle effect on the client.
 * 
 * @author Daedalus4096
 */
public class SpellTrailPacket implements IMessageToClient {
    public static final StreamCodec<RegistryFriendlyByteBuf, SpellTrailPacket> STREAM_CODEC = StreamCodec.ofMember(SpellTrailPacket::encode, SpellTrailPacket::decode);

    protected final double x;
    protected final double y;
    protected final double z;
    protected final int color;
    
    public SpellTrailPacket(double x, double y, double z, int color) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.color = color;
    }
    
    public SpellTrailPacket(Vec3 pos, int color) {
        this(pos.x, pos.y, pos.z, color);
    }
    
    public static void encode(SpellTrailPacket message, RegistryFriendlyByteBuf buf) {
        buf.writeDouble(message.x);
        buf.writeDouble(message.y);
        buf.writeDouble(message.z);
        buf.writeVarInt(message.color);
    }
    
    public static SpellTrailPacket decode(RegistryFriendlyByteBuf buf) {
        return new SpellTrailPacket(buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readVarInt());
    }
    
    public static void onMessage(SpellTrailPacket message, CustomPayloadEvent.Context ctx) {
        FxDispatcher.INSTANCE.spellTrail(message.x, message.y, message.z, message.color);
    }
}
