package com.verdantartifice.primalmagick.common.network.packets.config;

import com.verdantartifice.primalmagick.common.affinities.AffinityManager;
import com.verdantartifice.primalmagick.common.affinities.AffinityType;
import com.verdantartifice.primalmagick.common.affinities.IAffinity;
import com.verdantartifice.primalmagick.common.affinities.IAffinitySerializer;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UpdateAffinitiesConfigPacketNeoforge implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<UpdateAffinitiesConfigPacketNeoforge> TYPE = new CustomPacketPayload.Type<>(ResourceUtils.loc("update_affinities_neoforge"));
    public static final StreamCodec<FriendlyByteBuf, UpdateAffinitiesConfigPacketNeoforge> STREAM_CODEC = StreamCodec.ofMember(UpdateAffinitiesConfigPacketNeoforge::encode, UpdateAffinitiesConfigPacketNeoforge::decode);
    private static final Logger LOGGER = LogManager.getLogger();

    protected final List<IAffinity> affinities;

    public UpdateAffinitiesConfigPacketNeoforge(Collection<IAffinity> affinities) {
        this.affinities = new ArrayList<>(affinities);
    }

    public static void encode(UpdateAffinitiesConfigPacketNeoforge message, FriendlyByteBuf buf) {
        buf.writeCollection(message.affinities, UpdateAffinitiesConfigPacketNeoforge::toNetwork);
    }

    public static UpdateAffinitiesConfigPacketNeoforge decode(FriendlyByteBuf buf) {
        return new UpdateAffinitiesConfigPacketNeoforge(buf.readList(UpdateAffinitiesConfigPacketNeoforge::fromNetwork));
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

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void onMessage(final UpdateAffinitiesConfigPacketNeoforge message, final IPayloadContext context) {
        context.enqueueWork(() -> {
            AffinityManager.getOrCreateInstance().replaceAffinities(message.affinities);
        }).exceptionally(e -> {
            LOGGER.error("Config task failed to replace affinity data");
            context.disconnect(Component.literal("Config task failed to replace affinity data"));   // TODO Localize
            return null;
        }).thenAccept($ -> {
            // Reply with acknowledgement
            context.reply(AcknowledgeAffinitiesConfigPacketNeoforge.INSTANCE);
        });
    }
}
