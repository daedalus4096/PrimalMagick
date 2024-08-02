package com.verdantartifice.primalmagick.common.network.packets.misc;

import com.verdantartifice.primalmagick.client.util.ClientUtils;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;

/**
 * Packet sent from the server to trigger the opening of the Grimoire GUI on the client.
 * 
 * @author Daedalus4096
 */
public class OpenGrimoireScreenPacket implements IMessageToClient {
    public static final StreamCodec<RegistryFriendlyByteBuf, OpenGrimoireScreenPacket> STREAM_CODEC = StreamCodec.ofMember(OpenGrimoireScreenPacket::encode, OpenGrimoireScreenPacket::decode);

    public OpenGrimoireScreenPacket() {}
    
    public static void encode(OpenGrimoireScreenPacket message, RegistryFriendlyByteBuf buf) {
        // Nothing to do
    }
    
    public static OpenGrimoireScreenPacket decode(RegistryFriendlyByteBuf buf) {
        return new OpenGrimoireScreenPacket();
    }
    
    public static void onMessage(OpenGrimoireScreenPacket message, CustomPayloadEvent.Context ctx) {
        if (FMLEnvironment.dist.isClient()) {
            ClientUtils.openGrimoireScreen();
        }
    }
}
