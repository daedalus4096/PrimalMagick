package com.verdantartifice.primalmagick.common.network.packets.config;

import com.verdantartifice.primalmagick.common.affinities.AffinityManager;
import com.verdantartifice.primalmagick.common.affinities.AffinityType;
import com.verdantartifice.primalmagick.common.affinities.IAffinity;
import com.verdantartifice.primalmagick.common.affinities.IAffinitySerializer;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import commonnetwork.networking.data.PacketContext;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Packet to update affinity JSON data on the client from the server.
 * 
 * @author Daedalus4096
 */
public class UpdateAffinitiesConfigPacket implements IMessageToClient {
    public static final ResourceLocation CHANNEL = ResourceUtils.loc("update_affinities_config");
    public static final StreamCodec<FriendlyByteBuf, UpdateAffinitiesConfigPacket> STREAM_CODEC = StreamCodec.ofMember(UpdateAffinitiesConfigPacket::encode, UpdateAffinitiesConfigPacket::decode);
    private static final int NO_REPLY = -1;

    protected final int token;
    protected final List<IAffinity> affinities;
    
    public UpdateAffinitiesConfigPacket(Collection<IAffinity> affinities) {
        this(NO_REPLY, affinities);
    }
    
    public UpdateAffinitiesConfigPacket(int token, Collection<IAffinity> affinities) {
        this.token = token;
        this.affinities = new ArrayList<>(affinities);
    }

    public static CustomPacketPayload.Type<CustomPacketPayload> type() {
        return new CustomPacketPayload.Type<>(CHANNEL);
    }

    public static void encode(UpdateAffinitiesConfigPacket message, FriendlyByteBuf buf) {
        buf.writeVarInt(message.token);
        buf.writeCollection(message.affinities, UpdateAffinitiesConfigPacket::toNetwork);
    }
    
    public static UpdateAffinitiesConfigPacket decode(FriendlyByteBuf buf) {
        return new UpdateAffinitiesConfigPacket(buf.readVarInt(), buf.readList(UpdateAffinitiesConfigPacket::fromNetwork));
    }
    
    public static IAffinity fromNetwork(FriendlyByteBuf buf) {
        // TODO Replace this with a dispatched stream codec
        String typeStr = buf.readUtf();
        AffinityType type = AffinityType.parse(typeStr);
        IAffinitySerializer<?> serializer = AffinityManager.getSerializer(type);
        if (serializer == null) {
            throw new IllegalArgumentException("Unknown affinity serializer " + typeStr);
        }
        return serializer.fromNetwork(buf);
    }
    
    @SuppressWarnings("unchecked")
    public static <T extends IAffinity> void toNetwork(FriendlyByteBuf buf, T affinity) {
        buf.writeUtf(affinity.getType().getSerializedName());
        ((IAffinitySerializer<T>)affinity.getSerializer()).toNetwork(buf, affinity);
    }
    
    public static void onMessage(PacketContext<UpdateAffinitiesConfigPacket> ctx) {
        UpdateAffinitiesConfigPacket message = ctx.message();
        AffinityManager.getOrCreateInstance().replaceAffinities(message.affinities);
        if (message.token != NO_REPLY) {
            PacketHandler.sendToServer(new AcknowledgementPacket(message.token));
        }
    }
}
