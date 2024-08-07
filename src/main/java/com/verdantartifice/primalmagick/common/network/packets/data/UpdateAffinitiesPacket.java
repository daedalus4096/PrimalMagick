package com.verdantartifice.primalmagick.common.network.packets.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.verdantartifice.primalmagick.common.affinities.AffinityManager;
import com.verdantartifice.primalmagick.common.affinities.AffinityType;
import com.verdantartifice.primalmagick.common.affinities.IAffinity;
import com.verdantartifice.primalmagick.common.affinities.IAffinitySerializer;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraftforge.event.network.CustomPayloadEvent;

/**
 * Packet to update affinity JSON data on the client from the server.
 * 
 * @author Daedalus4096
 */
public class UpdateAffinitiesPacket implements IMessageToClient {
    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateAffinitiesPacket> STREAM_CODEC = StreamCodec.ofMember(UpdateAffinitiesPacket::encode, UpdateAffinitiesPacket::decode);

    protected final List<IAffinity> affinities;
    
    public UpdateAffinitiesPacket(Collection<IAffinity> affinities) {
        this.affinities = new ArrayList<>(affinities);
    }
    
    public static void encode(UpdateAffinitiesPacket message, RegistryFriendlyByteBuf buf) {
        buf.writeCollection(message.affinities, UpdateAffinitiesPacket::toNetwork);
    }
    
    public static UpdateAffinitiesPacket decode(RegistryFriendlyByteBuf buf) {
        return new UpdateAffinitiesPacket(buf.readList(UpdateAffinitiesPacket::fromNetwork));
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
    
    public static void onMessage(UpdateAffinitiesPacket message, CustomPayloadEvent.Context ctx) {
        AffinityManager.getOrCreateInstance().replaceAffinities(message.affinities);
    }
}
