package com.verdantartifice.primalmagick.common.network.packets.misc;

import com.verdantartifice.primalmagick.client.util.ClientUtils;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.NetworkDirection;

/**
 * Packet sent from the server to trigger the opening of the Grimoire GUI on the client.
 * 
 * @author Daedalus4096
 */
public class OpenGrimoireScreenPacket implements IMessageToClient {
    public OpenGrimoireScreenPacket() {}
    
    public static NetworkDirection direction() {
        return NetworkDirection.PLAY_TO_CLIENT;
    }
    
    public static void encode(OpenGrimoireScreenPacket message, FriendlyByteBuf buf) {
        // Nothing to do
    }
    
    public static OpenGrimoireScreenPacket decode(FriendlyByteBuf buf) {
        return new OpenGrimoireScreenPacket();
    }
    
    public static void onMessage(OpenGrimoireScreenPacket message, CustomPayloadEvent.Context ctx) {
        if (FMLEnvironment.dist.isClient()) {
            ClientUtils.openGrimoireScreen();
        }
    }
}
