package com.verdantartifice.primalmagick.common.network.packets.misc;

import java.util.function.Supplier;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagick.client.util.ClientUtils;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.NetworkEvent;

/**
 * Packet sent from the server to trigger the opening of of a static book on the client.
 * 
 * @author Daedalus4096
 */
public class OpenStaticBookScreenPacket implements IMessageToClient {
    protected ResourceLocation bookId;
    
    public OpenStaticBookScreenPacket() {
        this.bookId = null;
    }
    
    public OpenStaticBookScreenPacket(@Nonnull ResourceLocation bookId) {
        this.bookId = bookId;
    }
    
    public static void encode(OpenStaticBookScreenPacket message, FriendlyByteBuf buf) {
        buf.writeResourceLocation(message.bookId);
    }
    
    public static OpenStaticBookScreenPacket decode(FriendlyByteBuf buf) {
        OpenStaticBookScreenPacket message = new OpenStaticBookScreenPacket();
        message.bookId = buf.readResourceLocation();
        return message;
    }
    
    public static class Handler {
        public static void onMessage(OpenStaticBookScreenPacket message, Supplier<NetworkEvent.Context> ctx) {
            // Enqueue the handler work on the main game thread
            ctx.get().enqueueWork(() -> {
                if (FMLEnvironment.dist == Dist.CLIENT) {
                    ClientUtils.openStaticBookScreen(message.bookId);
                }
            });
            
            // Mark the packet as handled so we don't get warning log spam
            ctx.get().setPacketHandled(true);
        }
    }
}
