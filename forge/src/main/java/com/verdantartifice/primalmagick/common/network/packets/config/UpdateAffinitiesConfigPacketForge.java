package com.verdantartifice.primalmagick.common.network.packets.config;

import com.verdantartifice.primalmagick.common.affinities.AbstractAffinity;
import com.verdantartifice.primalmagick.common.affinities.AffinityManager;
import com.verdantartifice.primalmagick.common.network.ConfigPacketHandlerForge;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraftforge.event.network.CustomPayloadEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UpdateAffinitiesConfigPacketForge implements IMessageToClient {
    public static final StreamCodec<FriendlyByteBuf, UpdateAffinitiesConfigPacketForge> STREAM_CODEC = StreamCodec.ofMember(UpdateAffinitiesConfigPacketForge::encode, UpdateAffinitiesConfigPacketForge::decode);
    private static final Logger LOGGER = LogManager.getLogger();

    protected final List<AbstractAffinity<?>> affinities;

    public UpdateAffinitiesConfigPacketForge(Collection<AbstractAffinity<?>> affinities) {
        this.affinities = new ArrayList<>(affinities);
    }

    public static void encode(UpdateAffinitiesConfigPacketForge message, FriendlyByteBuf buf) {
        buf.writeCollection(message.affinities, UpdateAffinitiesConfigPacketForge::toNetwork);
    }

    public static UpdateAffinitiesConfigPacketForge decode(FriendlyByteBuf buf) {
        return new UpdateAffinitiesConfigPacketForge(buf.readList(UpdateAffinitiesConfigPacketForge::fromNetwork));
    }

    public static AbstractAffinity<?> fromNetwork(FriendlyByteBuf buf) {
        return AbstractAffinity.dispatchStreamCodec().decode(buf);
    }

    public static void toNetwork(FriendlyByteBuf buf, AbstractAffinity<?> affinity) {
        AbstractAffinity.dispatchStreamCodec().encode(buf, affinity);
    }

    public static void onMessage(UpdateAffinitiesConfigPacketForge message, CustomPayloadEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            AffinityManager.getOrCreateInstance().replaceAffinities(message.affinities);
        }).exceptionally(e -> {
            LOGGER.error("Config task failed to replace affinity data");
            ctx.getConnection().disconnect(Component.literal("Config task failed to replace affinity data"));   // TODO Localize
            return null;
        }).thenAccept($ -> {
            ConfigPacketHandlerForge.sendOverConnection(AcknowledgeAffinitiesConfigPacketForge.INSTANCE, ctx.getConnection());
        });
        ctx.setPacketHandled(true);
    }
}
