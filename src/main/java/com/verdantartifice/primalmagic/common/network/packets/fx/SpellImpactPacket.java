package com.verdantartifice.primalmagic.common.network.packets.fx;

import java.util.function.Supplier;

import com.verdantartifice.primalmagic.client.fx.FxDispatcher;
import com.verdantartifice.primalmagic.common.network.packets.IMessageToClient;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class SpellImpactPacket implements IMessageToClient {
    protected double x;
    protected double y;
    protected double z;
    protected int color;
    
    public SpellImpactPacket() {}
    
    public SpellImpactPacket(double x, double y, double z, int color) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.color = color;
    }
    
    public static void encode(SpellImpactPacket message, PacketBuffer buf) {
        buf.writeDouble(message.x);
        buf.writeDouble(message.y);
        buf.writeDouble(message.z);
        buf.writeInt(message.color);
    }
    
    public static SpellImpactPacket decode(PacketBuffer buf) {
        SpellImpactPacket message = new SpellImpactPacket();
        message.x = buf.readDouble();
        message.y = buf.readDouble();
        message.z = buf.readDouble();
        message.color = buf.readInt();
        return message;
    }
    
    public static class Handler {
        public static void onMessage(SpellImpactPacket message, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                FxDispatcher.INSTANCE.spellImpact(message.x, message.y, message.z, message.color);
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
