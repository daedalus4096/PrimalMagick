package com.verdantartifice.primalmagick.common.network.packets.config;

import com.verdantartifice.primalmagick.common.affinities.AffinityManager;
import com.verdantartifice.primalmagick.common.affinities.AffinityType;
import com.verdantartifice.primalmagick.common.affinities.IAffinity;
import com.verdantartifice.primalmagick.common.affinities.IAffinitySerializer;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraftforge.event.network.CustomPayloadEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UpdateAffinitiesConfigPacketForge implements IMessageToClient {
    public static final StreamCodec<FriendlyByteBuf, UpdateAffinitiesConfigPacketForge> STREAM_CODEC = StreamCodec.ofMember(UpdateAffinitiesConfigPacketForge::encode, UpdateAffinitiesConfigPacketForge::decode);

    protected final List<IAffinity> affinities;

    public UpdateAffinitiesConfigPacketForge(Collection<IAffinity> affinities) {
        this.affinities = new ArrayList<>(affinities);
    }

    public static void encode(UpdateAffinitiesConfigPacketForge message, FriendlyByteBuf buf) {
        buf.writeCollection(message.affinities, UpdateAffinitiesConfigPacketForge::toNetwork);
    }

    public static UpdateAffinitiesConfigPacketForge decode(FriendlyByteBuf buf) {
        return new UpdateAffinitiesConfigPacketForge(buf.readList(UpdateAffinitiesConfigPacketForge::fromNetwork));
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

    public static void onMessage(UpdateAffinitiesConfigPacketForge message, CustomPayloadEvent.Context ctx) {
        AffinityManager.getOrCreateInstance().replaceAffinities(message.affinities);
        ctx.getConnection().send();
    }
}
