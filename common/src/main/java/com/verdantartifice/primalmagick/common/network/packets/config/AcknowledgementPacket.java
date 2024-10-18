package com.verdantartifice.primalmagick.common.network.packets.config;

import java.util.function.BiConsumer;

import com.verdantartifice.primalmagick.common.network.packets.IMessageToServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraftforge.event.network.CustomPayloadEvent;

public record AcknowledgementPacket(int token) implements IMessageToServer {
    private static final Logger LOGGER = LogManager.getLogger();
    
    public static final StreamCodec<FriendlyByteBuf, AcknowledgementPacket> STREAM_CODEC = StreamCodec.ofMember(AcknowledgementPacket::encode, AcknowledgementPacket::decode);
    
    private static int nextAckId = 0;
    private static final Int2ObjectMap<BiConsumer<AcknowledgementPacket, CustomPayloadEvent.Context>> PENDING = new Int2ObjectOpenHashMap<>();

    public static int expect(BiConsumer<AcknowledgementPacket, CustomPayloadEvent.Context> callback) {
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
    
    public static void onMessage(AcknowledgementPacket message, CustomPayloadEvent.Context ctx) {
        var callback = PENDING.remove(message.token());
        if (callback != null) {
            LOGGER.debug("Primal Magick received acknowledgement packet {} from client", message.token());
            callback.accept(message, ctx);
        } else {
            LOGGER.error("Primal Magick received unexpected acknowledgement packet {} from client", message.token());
            ctx.getConnection().disconnect(Component.literal("Illegal AcknowledgementPacket received, unknown token: " + Integer.toHexString(message.token())));
        }
        ctx.setPacketHandled(true);
    }
}
