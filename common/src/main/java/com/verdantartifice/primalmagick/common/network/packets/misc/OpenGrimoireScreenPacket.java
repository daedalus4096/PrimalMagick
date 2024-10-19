package com.verdantartifice.primalmagick.common.network.packets.misc;

import com.verdantartifice.primalmagick.client.util.ClientUtils;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import commonnetwork.networking.data.PacketContext;
import commonnetwork.networking.data.Side;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/**
 * Packet sent from the server to trigger the opening of the Grimoire GUI on the client.
 * 
 * @author Daedalus4096
 */
public class OpenGrimoireScreenPacket implements IMessageToClient {
    public static final ResourceLocation CHANNEL = ResourceUtils.loc("open_grimoire_screen");
    public static final StreamCodec<RegistryFriendlyByteBuf, OpenGrimoireScreenPacket> STREAM_CODEC = StreamCodec.ofMember(OpenGrimoireScreenPacket::encode, OpenGrimoireScreenPacket::decode);

    public OpenGrimoireScreenPacket() {}

    public static CustomPacketPayload.Type<CustomPacketPayload> type() {
        return new CustomPacketPayload.Type<>(CHANNEL);
    }

    public static void encode(OpenGrimoireScreenPacket message, RegistryFriendlyByteBuf buf) {
        // Nothing to do
    }
    
    public static OpenGrimoireScreenPacket decode(RegistryFriendlyByteBuf buf) {
        return new OpenGrimoireScreenPacket();
    }
    
    public static void onMessage(PacketContext<OpenGrimoireScreenPacket> ctx) {
        if (Side.CLIENT.equals(ctx.side())) {
            ClientUtils.openGrimoireScreen();
        }
    }
}
