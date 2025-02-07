package com.verdantartifice.primalmagick.common.network.packets.config;

import com.verdantartifice.primalmagick.common.network.packets.IMessageToServer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraftforge.event.network.CustomPayloadEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;

public record AcknowledgeAffinitiesConfigPacketForge() implements IMessageToServer {
    public static final StreamCodec<FriendlyByteBuf, AcknowledgeAffinitiesConfigPacketForge> STREAM_CODEC = StreamCodec.unit(new AcknowledgeAffinitiesConfigPacketForge());
    private static final List<BiConsumer<AcknowledgeAffinitiesConfigPacketForge, CustomPayloadEvent.Context>> CALLBACKS = new ArrayList<>();

    public static void expect(BiConsumer<AcknowledgeAffinitiesConfigPacketForge, CustomPayloadEvent.Context> callback) {
        CALLBACKS.add(callback);
    }

    public static void onMessage(AcknowledgeAffinitiesConfigPacketForge message, CustomPayloadEvent.Context ctx) {
        CALLBACKS.stream().filter(Objects::nonNull).forEach(callback -> callback.accept(message, ctx));
    }
}
