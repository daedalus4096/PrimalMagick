package com.verdantartifice.primalmagic.common.network.packets.fx;

import java.util.function.Supplier;

import com.verdantartifice.primalmagic.client.fx.FxDispatcher;
import com.verdantartifice.primalmagic.common.network.packets.IMessageToClient;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

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
    
    public static void encode(SpellTrailPacket message, PacketBuffer buf) {
        buf.writeDouble(message.x);
        buf.writeDouble(message.y);
        buf.writeDouble(message.z);
        buf.writeInt(message.color);
    }
    
    public static SpellTrailPacket decode(PacketBuffer buf) {
        SpellTrailPacket message = new SpellTrailPacket();
        message.x = buf.readDouble();
        message.y = buf.readDouble();
        message.z = buf.readDouble();
        message.color = buf.readInt();
        return message;
    }
    
    public static class Handler {
        public static void onMessage(SpellTrailPacket message, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                FxDispatcher.INSTANCE.spellTrail(message.x, message.y, message.z, message.color);
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
