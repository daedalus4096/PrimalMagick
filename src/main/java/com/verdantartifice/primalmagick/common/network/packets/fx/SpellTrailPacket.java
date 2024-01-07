package com.verdantartifice.primalmagick.common.network.packets.fx;

import com.verdantartifice.primalmagick.client.fx.FxDispatcher;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.NetworkDirection;

/**
 * Packet sent from the server to trigger a spell trail particle effect on the client.
 * 
 * @author Daedalus4096
 */
public class SpellTrailPacket implements IMessageToClient {
    protected double x;
    protected double y;
    protected double z;
    protected int color;
    
    public SpellTrailPacket() {}
    
    public SpellTrailPacket(double x, double y, double z, int color) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.color = color;
    }
    
    public SpellTrailPacket(Vec3 pos, int color) {
        this(pos.x, pos.y, pos.z, color);
    }
    
    public static NetworkDirection direction() {
        return NetworkDirection.PLAY_TO_CLIENT;
    }
    
    public static void encode(SpellTrailPacket message, FriendlyByteBuf buf) {
        buf.writeDouble(message.x);
        buf.writeDouble(message.y);
        buf.writeDouble(message.z);
        buf.writeInt(message.color);
    }
    
    public static SpellTrailPacket decode(FriendlyByteBuf buf) {
        SpellTrailPacket message = new SpellTrailPacket();
        message.x = buf.readDouble();
        message.y = buf.readDouble();
        message.z = buf.readDouble();
        message.color = buf.readInt();
        return message;
    }
    
    public static void onMessage(SpellTrailPacket message, CustomPayloadEvent.Context ctx) {
        FxDispatcher.INSTANCE.spellTrail(message.x, message.y, message.z, message.color);
    }
}
