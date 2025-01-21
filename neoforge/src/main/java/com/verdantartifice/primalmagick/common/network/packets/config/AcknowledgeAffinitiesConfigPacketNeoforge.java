package com.verdantartifice.primalmagick.common.network.packets.config;

import com.verdantartifice.primalmagick.common.network.tasks.SyncAffinityDataTaskNeoforge;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record AcknowledgeAffinitiesConfigPacketNeoforge() implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<AcknowledgeAffinitiesConfigPacketNeoforge> TYPE = new CustomPacketPayload.Type<>(ResourceUtils.loc("acknowledge_affinities_neoforge"));
    public static final StreamCodec<FriendlyByteBuf, AcknowledgeAffinitiesConfigPacketNeoforge> STREAM_CODEC = StreamCodec.unit(new AcknowledgeAffinitiesConfigPacketNeoforge());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void onMessage(final AcknowledgeAffinitiesConfigPacketNeoforge message, final IPayloadContext context) {
        context.finishCurrentTask(SyncAffinityDataTaskNeoforge.TYPE);
    }
}
