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
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.NetworkDirection;

/**
 * Packet to update affinity JSON data on the client from the server.
 * 
 * @author Daedalus4096
 */
public class UpdateAffinitiesPacket implements IMessageToClient {
    protected List<IAffinity> affinities;
    
    public UpdateAffinitiesPacket(Collection<IAffinity> affinities) {
        this.affinities = new ArrayList<>(affinities);
    }
    
    public UpdateAffinitiesPacket(FriendlyByteBuf buf) {
        this.affinities = buf.readList(UpdateAffinitiesPacket::fromNetwork);
    }
    
    public List<IAffinity> getAffinities() {
        return this.affinities;
    }
    
    public static NetworkDirection direction() {
        return NetworkDirection.PLAY_TO_CLIENT;
    }
    
    public static void encode(UpdateAffinitiesPacket message, FriendlyByteBuf buf) {
        buf.writeCollection(message.affinities, UpdateAffinitiesPacket::toNetwork);
    }
    
    public static UpdateAffinitiesPacket decode(FriendlyByteBuf buf) {
        return new UpdateAffinitiesPacket(buf);
    }
    
    public static IAffinity fromNetwork(FriendlyByteBuf buf) {
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
        AffinityManager.getOrCreateInstance().replaceAffinities(message.getAffinities());
    }
}
