package com.verdantartifice.primalmagic.common.network.packets.fx;

import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.client.fx.FxDispatcher;
import com.verdantartifice.primalmagic.common.network.packets.IMessageToClient;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

public class BlockPoofPacket implements IMessageToClient {
    protected double x;
    protected double y;
    protected double z;
    protected int color;
    protected boolean sound;
    protected byte face;
    
    public BlockPoofPacket() {}
    
    public BlockPoofPacket(double x, double y, double z, int color, boolean sound, @Nullable Direction facing) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.color = color;
        this.sound = sound;
        this.face = facing == null ? (byte)-1 : (byte)facing.getIndex();
    }
    
    public BlockPoofPacket(@Nonnull BlockPos pos, int color, boolean sound, @Nullable Direction facing) {
        this(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, color, sound, facing);
    }
    
    public static void encode(BlockPoofPacket message, PacketBuffer buf) {
        buf.writeDouble(message.x);
        buf.writeDouble(message.y);
        buf.writeDouble(message.z);
        buf.writeVarInt(message.color);
        buf.writeBoolean(message.sound);
        buf.writeByte(message.face);
    }
    
    public static BlockPoofPacket decode(PacketBuffer buf) {
        BlockPoofPacket message = new BlockPoofPacket();
        message.x = buf.readDouble();
        message.y = buf.readDouble();
        message.z = buf.readDouble();
        message.color = buf.readVarInt();
        message.sound = buf.readBoolean();
        message.face = buf.readByte();
        return message;
    }
    
    public static class Handler {
        public static void onMessage(BlockPoofPacket message, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                Direction side = null;
                if (message.face >= 0) {
                    side = Direction.byIndex(message.face);
                }
                FxDispatcher.INSTANCE.poof(message.x, message.y, message.z, message.color, message.sound, side);
            });
        }
    }
}
