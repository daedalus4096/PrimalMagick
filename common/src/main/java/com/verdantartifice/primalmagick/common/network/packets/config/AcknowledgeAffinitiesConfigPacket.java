package com.verdantartifice.primalmagick.common.network.packets.config;

import com.verdantartifice.primalmagick.common.network.packets.IMessageToServer;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public class AcknowledgeAffinitiesConfigPacket implements CustomPacketPayload, IMessageToServer {
    public static final ResourceLocation CHANNEL = ResourceUtils.loc("acknowledge_affinities");
    public static final StreamCodec<FriendlyByteBuf, AcknowledgeAffinitiesConfigPacket> STREAM_CODEC = StreamCodec.unit(new AcknowledgeAffinitiesConfigPacket());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return new CustomPacketPayload.Type<>(CHANNEL);
    }
}
