package com.verdantartifice.primalmagick.common.network.packets.config;

import com.verdantartifice.primalmagick.common.network.tasks.SyncLinguisticsGridDataTaskNeoforge;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class AcknowledgeLinguisticsGridConfigPacketNeoforge implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<AcknowledgeLinguisticsGridConfigPacketNeoforge> TYPE = new CustomPacketPayload.Type<>(ResourceUtils.loc("acknowledge_linguistics_grid_neoforge"));
    public static final StreamCodec<FriendlyByteBuf, AcknowledgeLinguisticsGridConfigPacketNeoforge> STREAM_CODEC = StreamCodec.unit(new AcknowledgeLinguisticsGridConfigPacketNeoforge());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void onMessage(final AcknowledgeLinguisticsGridConfigPacketNeoforge message, final IPayloadContext context) {
        context.finishCurrentTask(SyncLinguisticsGridDataTaskNeoforge.TYPE);
    }
}
