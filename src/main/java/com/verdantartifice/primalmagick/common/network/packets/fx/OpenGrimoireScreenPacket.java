package com.verdantartifice.primalmagick.common.network.packets.fx;

import java.util.function.Supplier;

import com.verdantartifice.primalmagick.client.util.ClientUtils;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.NetworkEvent;

/**
 * Packet sent from the server to trigger the opening of the Grimoire GUI on the client.
 * 
 * @author Daedalus4096
 */
public class OpenGrimoireScreenPacket implements IMessageToClient {
    public OpenGrimoireScreenPacket() {}
    
    public static void encode(OpenGrimoireScreenPacket message, FriendlyByteBuf buf) {
        // Nothing to do
    }
    
    public static OpenGrimoireScreenPacket decode(FriendlyByteBuf buf) {
        return new OpenGrimoireScreenPacket();
    }
    
    public static class Handler {
        public static void onMessage(OpenGrimoireScreenPacket message, Supplier<NetworkEvent.Context> ctx) {
            // Enqueue the handler work on the main game thread
            ctx.get().enqueueWork(() -> {
                if (FMLEnvironment.dist == Dist.CLIENT) {
                    ClientUtils.openGrimoireScreen();
                }
            });
            
            // Mark the packet as handled so we don't get warning log spam
            ctx.get().setPacketHandled(true);
        }
    }
}
