package com.verdantartifice.primalmagick.common.network.packets.fx;

import com.verdantartifice.primalmagick.client.fx.FxDispatcher;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.Sources;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import commonnetwork.networking.data.PacketContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Packet sent from the server to trigger a mana sparkle particle effect on the client.
 * 
 * @author Daedalus4096
 */
public class ManaSparklePacket implements IMessageToClient {
    public static final ResourceLocation CHANNEL = ResourceUtils.loc("mana_sparkle");
    public static final StreamCodec<RegistryFriendlyByteBuf, ManaSparklePacket> STREAM_CODEC = StreamCodec.ofMember(ManaSparklePacket::encode, ManaSparklePacket::decode);

    protected final double x1;
    protected final double y1;
    protected final double z1;
    protected final double x2;
    protected final double y2;
    protected final double z2;
    protected final int maxAge;
    protected final int color;
    protected final double phase;

    protected ManaSparklePacket(double x1, double y1, double z1, double x2, double y2, double z2, int maxAge, int color, double phase) {
        this.x1 = x1;
        this.y1 = y1;
        this.z1 = z1;
        this.x2 = x2;
        this.y2 = y2;
        this.z2 = z2;
        this.maxAge = maxAge;
        this.color = color;
        this.phase = phase;
    }

    public ManaSparklePacket(Position pos1, Position pos2, int maxAge, Source source) {
        this(pos1, pos2, maxAge, source.getColor(), getPhaseForSource(source));
    }

    public ManaSparklePacket(Position pos1, Position pos2, int maxAge, int color, double phase) {
        this(pos1.x(), pos1.y(), pos1.z(), pos2.x(), pos2.y(), pos2.z(), maxAge, color, phase);
    }
    
    private static double getPhaseForSource(Source source) {
        List<Source> sources = Sources.getAllSorted();
        return Math.toRadians(sources.indexOf(source) * (360D / sources.size()));
    }

    public static CustomPacketPayload.Type<CustomPacketPayload> type() {
        return new CustomPacketPayload.Type<>(CHANNEL);
    }

    public static void encode(ManaSparklePacket message, RegistryFriendlyByteBuf buf) {
        buf.writeDouble(message.x1);
        buf.writeDouble(message.y1);
        buf.writeDouble(message.z1);
        buf.writeDouble(message.x2);
        buf.writeDouble(message.y2);
        buf.writeDouble(message.z2);
        buf.writeVarInt(message.maxAge);
        buf.writeVarInt(message.color);
        buf.writeDouble(message.phase);
    }
    
    public static ManaSparklePacket decode(RegistryFriendlyByteBuf buf) {
        return new ManaSparklePacket(buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readVarInt(), buf.readVarInt(), buf.readDouble());
    }
    
    public static void onMessage(PacketContext<ManaSparklePacket> ctx) {
        ManaSparklePacket message = ctx.message();
        FxDispatcher.INSTANCE.manaSparkle(message.x1, message.y1, message.z1, message.x2, message.y2, message.z2, message.maxAge, message.color, message.phase);
    }
}
