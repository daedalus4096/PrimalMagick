package com.verdantartifice.primalmagick.common.network.packets.fx;

import com.verdantartifice.primalmagick.client.fx.FxDispatcher;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.NetworkDirection;

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

    public SpellBoltPacket(Vec3 source, Vec3 target, int color) {
        this(source.x, source.y, source.z, target.x, target.y, target.z, color);
    }
    
    public SpellBoltPacket(BlockPos source, BlockPos target, int color) {
        this(source.getX() + 0.5D, source.getY() + 0.5D, source.getZ() + 0.5D, target.getX() + 0.5D, target.getY() + 0.5D, target.getZ() + 0.5D, color);
    }
    
    public static NetworkDirection direction() {
        return NetworkDirection.PLAY_TO_CLIENT;
    }
    
    public static void encode(SpellBoltPacket message, FriendlyByteBuf buf) {
        buf.writeDouble(message.x1);
        buf.writeDouble(message.y1);
        buf.writeDouble(message.z1);
        buf.writeDouble(message.x2);
        buf.writeDouble(message.y2);
        buf.writeDouble(message.z2);
        buf.writeVarInt(message.color);
    }
    
    public static SpellBoltPacket decode(FriendlyByteBuf buf) {
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
    
    public static void onMessage(SpellBoltPacket message, CustomPayloadEvent.Context ctx) {
        FxDispatcher.INSTANCE.spellBolt(message.x1, message.y1, message.z1, message.x2, message.y2, message.z2, message.color);
    }
}
