package com.verdantartifice.primalmagick.common.network.packets.misc;

import java.util.function.Supplier;

import com.verdantartifice.primalmagick.client.util.ClientUtils;
import com.verdantartifice.primalmagick.common.books.BookDefinition;
import com.verdantartifice.primalmagick.common.books.BooksPM;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.NetworkEvent;

/**
 * Packet sent from the server to trigger the opening of of a static book on the client.
 * 
 * @author Daedalus4096
 */
public class OpenStaticBookScreenPacket implements IMessageToClient {
    protected final ResourceKey<?> bookKey;
    
    public OpenStaticBookScreenPacket(BookDefinition bookDef) {
        this.bookKey = BooksPM.BOOKS.get().getResourceKey(bookDef).orElseThrow();
    }
    
    private OpenStaticBookScreenPacket(ResourceKey<?> bookKey) {
        this.bookKey = bookKey;
    }
    
    public static void encode(OpenStaticBookScreenPacket message, FriendlyByteBuf buf) {
        buf.writeResourceKey(message.bookKey);
    }
    
    public static OpenStaticBookScreenPacket decode(FriendlyByteBuf buf) {
        return new OpenStaticBookScreenPacket(buf.readResourceKey(BooksPM.BOOKS.get().getRegistryKey()));
    }
    
    public static class Handler {
        public static void onMessage(OpenStaticBookScreenPacket message, Supplier<NetworkEvent.Context> ctx) {
            // Enqueue the handler work on the main game thread
            ctx.get().enqueueWork(() -> {
                if (FMLEnvironment.dist == Dist.CLIENT) {
                    ClientUtils.openStaticBookScreen(message.bookKey);
                }
            });
            
            // Mark the packet as handled so we don't get warning log spam
            ctx.get().setPacketHandled(true);
        }
    }
}
