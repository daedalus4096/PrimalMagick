package com.verdantartifice.primalmagic.common.network.packets.fx;

import java.util.function.Supplier;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagic.client.fx.FxDispatcher;
import com.verdantartifice.primalmagic.common.network.packets.IMessageToClient;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

public class ManaSparklePacket implements IMessageToClient {
    protected double x1;
    protected double y1;
    protected double z1;
    protected double x2;
    protected double y2;
    protected double z2;
    protected int maxAge;
    protected int color;

    public ManaSparklePacket() {}
    
    public ManaSparklePacket(double x1, double y1, double z1, double x2, double y2, double z2, int maxAge, int color) {
        this.x1 = x1;
        this.y1 = y1;
        this.z1 = z1;
        this.x2 = x2;
        this.y2 = y2;
        this.z2 = z2;
        this.maxAge = maxAge;
        this.color = color;
    }
    
    public ManaSparklePacket(@Nonnull BlockPos source, double targetX, double targetY, double targetZ, int maxAge, int color) {
        this(source.getX() + 0.5D, source.getY() + 0.5D, source.getZ() + 0.5D, targetX, targetY, targetZ, maxAge, color);
    }
    
    public static void encode(ManaSparklePacket message, PacketBuffer buf) {
        buf.writeDouble(message.x1);
        buf.writeDouble(message.y1);
        buf.writeDouble(message.z1);
        buf.writeDouble(message.x2);
        buf.writeDouble(message.y2);
        buf.writeDouble(message.z2);
        buf.writeVarInt(message.maxAge);
        buf.writeVarInt(message.color);
    }
    
    public static ManaSparklePacket decode(PacketBuffer buf) {
        ManaSparklePacket message = new ManaSparklePacket();
        message.x1 = buf.readDouble();
        message.y1 = buf.readDouble();
        message.z1 = buf.readDouble();
        message.x2 = buf.readDouble();
        message.y2 = buf.readDouble();
        message.z2 = buf.readDouble();
        message.maxAge = buf.readVarInt();
        message.color = buf.readVarInt();
        return message;
    }
    
    public static class Handler {
        public static void onMessage(ManaSparklePacket message, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                FxDispatcher.INSTANCE.manaSparkle(message.x1, message.y1, message.z1, message.x2, message.y2, message.z2, message.maxAge, message.color);
            });
        }
    }
}
