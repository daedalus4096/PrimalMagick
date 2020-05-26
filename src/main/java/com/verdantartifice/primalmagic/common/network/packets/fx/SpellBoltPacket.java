package com.verdantartifice.primalmagic.common.network.packets.fx;

import java.util.function.Supplier;

import com.verdantartifice.primalmagic.client.fx.FxDispatcher;
import com.verdantartifice.primalmagic.common.network.packets.IMessageToClient;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.network.NetworkEvent;

/**
 * Packet sent from the server to trigger a spell bolt particle effect on the client.
 * 
 * @author Daedalus4096
 */
public class SpellBoltPacket implements IMessageToClient {
    protected double x1;
    protected double y1;
    protected double z1;
    protected double x2;
    protected double y2;
    protected double z2;
    protected int color;

    public SpellBoltPacket() {}
    
    public SpellBoltPacket(double x1, double y1, double z1, double x2, double y2, double z2, int color) {
        this.x1 = x1;
        this.y1 = y1;
        this.z1 = z1;
        this.x2 = x2;
        this.y2 = y2;
        this.z2 = z2;
        this.color = color;
    }

    public SpellBoltPacket(Vec3d source, Vec3d target, int color) {
        this(source.x, source.y, source.z, target.x, target.y, target.z, color);
    }
    
    public SpellBoltPacket(BlockPos source, BlockPos target, int color) {
        this(source.getX() + 0.5D, source.getY() + 0.5D, source.getZ() + 0.5D, target.getX() + 0.5D, target.getY() + 0.5D, target.getZ() + 0.5D, color);
    }
    
    public static void encode(SpellBoltPacket message, PacketBuffer buf) {
        buf.writeDouble(message.x1);
        buf.writeDouble(message.y1);
        buf.writeDouble(message.z1);
        buf.writeDouble(message.x2);
        buf.writeDouble(message.y2);
        buf.writeDouble(message.z2);
        buf.writeVarInt(message.color);
    }
    
    public static SpellBoltPacket decode(PacketBuffer buf) {
        SpellBoltPacket message = new SpellBoltPacket();
        message.x1 = buf.readDouble();
        message.y1 = buf.readDouble();
        message.z1 = buf.readDouble();
        message.x2 = buf.readDouble();
        message.y2 = buf.readDouble();
        message.z2 = buf.readDouble();
        message.color = buf.readVarInt();
        return message;
    }
    
    public static class Handler {
        public static void onMessage(SpellBoltPacket message, Supplier<NetworkEvent.Context> ctx) {
            // Enqueue the handler work on the main game thread
            ctx.get().enqueueWork(() -> {
                FxDispatcher.INSTANCE.spellBolt(message.x1, message.y1, message.z1, message.x2, message.y2, message.z2, message.color);
            });
            
            // Mark the packet as handled so we don't get warning log spam
            ctx.get().setPacketHandled(true);
        }
    }
}
