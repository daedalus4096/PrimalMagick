package com.verdantartifice.primalmagick.common.network.packets.fx;

import com.verdantartifice.primalmagick.client.fx.FxDispatcher;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import commonnetwork.networking.data.PacketContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

/**
 * Packet sent from the server to trigger a wand poof particle effect on the client.
 * 
 * @author Daedalus4096
 */
public class WandPoofPacket implements IMessageToClient {
    public static final ResourceLocation CHANNEL = ResourceUtils.loc("wand_poof");
    public static final StreamCodec<RegistryFriendlyByteBuf, WandPoofPacket> STREAM_CODEC = StreamCodec.ofMember(WandPoofPacket::encode, WandPoofPacket::decode);

    protected final double x;
    protected final double y;
    protected final double z;
    protected final int color;
    protected final boolean sound;
    protected final Direction face;
    
    public WandPoofPacket(double x, double y, double z, int color, boolean sound, @Nonnull Direction facing) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.color = color;
        this.sound = sound;
        this.face = facing;
    }
    
    public WandPoofPacket(@Nonnull BlockPos pos, int color, boolean sound, @Nonnull Direction facing) {
        this(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, color, sound, facing);
    }

    public static CustomPacketPayload.Type<CustomPacketPayload> type() {
        return new CustomPacketPayload.Type<>(CHANNEL);
    }

    public static void encode(WandPoofPacket message, RegistryFriendlyByteBuf buf) {
        buf.writeDouble(message.x);
        buf.writeDouble(message.y);
        buf.writeDouble(message.z);
        buf.writeVarInt(message.color);
        buf.writeBoolean(message.sound);
        buf.writeEnum(message.face);
    }
    
    public static WandPoofPacket decode(RegistryFriendlyByteBuf buf) {
        return new WandPoofPacket(buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readVarInt(), buf.readBoolean(), buf.readEnum(Direction.class));
    }
    
    public static void onMessage(PacketContext<WandPoofPacket> ctx) {
        WandPoofPacket message = ctx.message();
        FxDispatcher.INSTANCE.wandPoof(message.x, message.y, message.z, message.color, message.sound, message.face);
    }
}
