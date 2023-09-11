package com.verdantartifice.primalmagick.common.network.packets.misc;

import java.util.function.Supplier;

import com.google.common.base.Preconditions;
import com.verdantartifice.primalmagick.client.util.ClientUtils;
import com.verdantartifice.primalmagick.common.books.BookLanguagesPM;
import com.verdantartifice.primalmagick.common.books.BooksPM;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.items.books.StaticBookItem;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
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
    protected final ResourceLocation languageId;
    protected final int translatedComprehension;
    
    public OpenStaticBookScreenPacket(ItemStack bookStack) {
        Preconditions.checkArgument(bookStack.is(ItemsPM.STATIC_BOOK.get()), "Packet item stack must be a static book");
        this.bookKey = BooksPM.BOOKS.get().getResourceKey(StaticBookItem.getBookDefinition(bookStack)).orElseThrow();
        this.languageId = BookLanguagesPM.LANGUAGES.get().getKey(StaticBookItem.getBookLanguage(bookStack));
        this.translatedComprehension = StaticBookItem.getTranslatedComprehension(bookStack).orElse(0);
    }
    
    private OpenStaticBookScreenPacket(ResourceKey<?> bookKey, ResourceLocation languageId, int translatedComprehension) {
        this.bookKey = bookKey;
        this.languageId = languageId;
        this.translatedComprehension = translatedComprehension;
    }
    
    public static void encode(OpenStaticBookScreenPacket message, FriendlyByteBuf buf) {
        buf.writeResourceKey(message.bookKey);
        buf.writeResourceLocation(message.languageId);
        buf.writeVarInt(message.translatedComprehension);
    }
    
    public static OpenStaticBookScreenPacket decode(FriendlyByteBuf buf) {
        return new OpenStaticBookScreenPacket(buf.readResourceKey(BooksPM.BOOKS.get().getRegistryKey()), buf.readResourceLocation(), buf.readVarInt());
    }
    
    public static class Handler {
        public static void onMessage(OpenStaticBookScreenPacket message, Supplier<NetworkEvent.Context> ctx) {
            // Enqueue the handler work on the main game thread
            ctx.get().enqueueWork(() -> {
                if (FMLEnvironment.dist == Dist.CLIENT) {
                    ClientUtils.openStaticBookScreen(message.bookKey, message.languageId, message.translatedComprehension);
                }
            });
            
            // Mark the packet as handled so we don't get warning log spam
            ctx.get().setPacketHandled(true);
        }
    }
}
