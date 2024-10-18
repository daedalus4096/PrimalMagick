package com.verdantartifice.primalmagick.common.network.packets.config;

import com.verdantartifice.primalmagick.common.network.packets.IMessageToServer;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import commonnetwork.networking.data.PacketContext;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Consumer;

public record AcknowledgementPacket(int token) implements IMessageToServer {
    private static final Logger LOGGER = LogManager.getLogger();

    public static final ResourceLocation CHANNEL = ResourceUtils.loc("acknowledgement");
    public static final StreamCodec<FriendlyByteBuf, AcknowledgementPacket> STREAM_CODEC = StreamCodec.ofMember(AcknowledgementPacket::encode, AcknowledgementPacket::decode);
    
    private static int nextAckId = 0;
    private static final Int2ObjectMap<Consumer<PacketContext<AcknowledgementPacket>>> PENDING = new Int2ObjectOpenHashMap<>();

    public static CustomPacketPayload.Type<CustomPacketPayload> type() {
        return new CustomPacketPayload.Type<>(CHANNEL);
    }

    public static int expect(Consumer<PacketContext<AcknowledgementPacket>> callback) {
        int id = nextAckId++;
        PENDING.put(id, callback);
        return id;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(this.token);
    }

    public static AcknowledgementPacket decode(FriendlyByteBuf buf) {
        return new AcknowledgementPacket(buf.readVarInt());
    }
    
    public static void onMessage(PacketContext<AcknowledgementPacket> ctx) {
        AcknowledgementPacket message = ctx.message();
        var callback = PENDING.remove(message.token());
        if (callback != null) {
            LOGGER.debug("Primal Magick received acknowledgement packet {} from client", message.token());
            callback.accept(ctx);
        } else {
            LOGGER.error("Primal Magick received unexpected acknowledgement packet {} from client", message.token());
            ctx.sender().connection.disconnect(Component.literal("Illegal AcknowledgementPacket received, unknown token: " + Integer.toHexString(message.token())));
        }
    }
}
