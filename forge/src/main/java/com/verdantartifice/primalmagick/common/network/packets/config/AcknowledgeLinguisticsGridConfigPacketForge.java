package com.verdantartifice.primalmagick.common.network.packets.config;

import com.verdantartifice.primalmagick.common.network.packets.IMessageToServer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraftforge.event.network.CustomPayloadEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;

public record AcknowledgeLinguisticsGridConfigPacketForge() implements IMessageToServer {
    public static final AcknowledgeLinguisticsGridConfigPacketForge INSTANCE = new AcknowledgeLinguisticsGridConfigPacketForge();
    public static final StreamCodec<FriendlyByteBuf, AcknowledgeLinguisticsGridConfigPacketForge> STREAM_CODEC = StreamCodec.unit(INSTANCE);
    private static final List<BiConsumer<AcknowledgeLinguisticsGridConfigPacketForge, CustomPayloadEvent.Context>> CALLBACKS = new ArrayList<>();

    public static void expect(BiConsumer<AcknowledgeLinguisticsGridConfigPacketForge, CustomPayloadEvent.Context> callback) {
        CALLBACKS.add(callback);
    }

    public static void onMessage(AcknowledgeLinguisticsGridConfigPacketForge message, CustomPayloadEvent.Context ctx) {
        CALLBACKS.stream().filter(Objects::nonNull).forEach(callback -> callback.accept(message, ctx));
    }
}
