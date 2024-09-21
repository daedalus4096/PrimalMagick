package com.verdantartifice.primalmagick.common.network.packets.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.verdantartifice.primalmagick.common.affinities.AffinityManager;
import com.verdantartifice.primalmagick.common.affinities.AffinityType;
import com.verdantartifice.primalmagick.common.affinities.IAffinity;
import com.verdantartifice.primalmagick.common.affinities.IAffinitySerializer;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraftforge.event.network.CustomPayloadEvent;

/**
 * Packet to update affinity JSON data on the client from the server.
 * 
 * @author Daedalus4096
 */
public class UpdateAffinitiesConfigPacket implements IMessageToClient {
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
    
    public static void onMessage(UpdateAffinitiesConfigPacket message, CustomPayloadEvent.Context ctx) {
        AffinityManager.getOrCreateInstance().replaceAffinities(message.affinities);
        if (message.token != NO_REPLY) {
            PacketHandler.reply(new AcknowledgementPacket(message.token), ctx);
        }
    }
}
