package com.verdantartifice.primalmagick.common.network.packets.config;

import com.verdantartifice.primalmagick.common.network.packets.IMessageToServer;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraftforge.event.network.CustomPayloadEvent;

public record AcknowledgeAffinitiesConfigPacketForge() implements CustomPacketPayload, IMessageToServer {
    public static final StreamCodec<FriendlyByteBuf, AcknowledgeAffinitiesConfigPacketForge> STREAM_CODEC = StreamCodec.unit(new AcknowledgeAffinitiesConfigPacketForge());
    private static final CustomPacketPayload.Type<AcknowledgeAffinitiesConfigPacketForge> TYPE = new CustomPacketPayload.Type<>(ResourceUtils.loc("acknowledge_affinities"));

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void onMessage(AcknowledgeAffinitiesConfigPacketForge message, CustomPayloadEvent.Context ctx) {
        // FIXME ???
    }
}
