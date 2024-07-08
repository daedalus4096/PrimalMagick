package com.verdantartifice.primalmagick.common.network.packets.misc;

import com.google.common.base.Preconditions;
import com.verdantartifice.primalmagick.client.util.ClientUtils;
import com.verdantartifice.primalmagick.common.books.BookLanguage;
import com.verdantartifice.primalmagick.common.books.BookType;
import com.verdantartifice.primalmagick.common.items.books.StaticBookItem;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.tags.ItemTagsPM;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;

/**
 * Packet sent from the server to trigger the opening of of a static book on the client.
 * 
 * @author Daedalus4096
 */
public class OpenStaticBookScreenPacket implements IMessageToClient {
    public static final StreamCodec<RegistryFriendlyByteBuf, OpenStaticBookScreenPacket> STREAM_CODEC = StreamCodec.ofMember(OpenStaticBookScreenPacket::encode, OpenStaticBookScreenPacket::decode);

    // TODO Can this be strongly typed as a ResourceKey<BookDefinition> instead?
    protected final ResourceKey<?> bookKey;
    protected final ResourceKey<BookLanguage> languageId;
    protected final int translatedComprehension;
    protected final BookType bookType;
    
    public OpenStaticBookScreenPacket(ItemStack bookStack, BookType bookType) {
        Preconditions.checkArgument(bookStack.is(ItemTagsPM.STATIC_BOOKS), "Packet item stack must be a static book or tablet");
        this.bookKey = StaticBookItem.getBookId(bookStack).orElseThrow();
        this.languageId = StaticBookItem.getBookLanguageId(bookStack).orElseThrow();
        this.translatedComprehension = StaticBookItem.getTranslatedComprehension(bookStack).orElse(0);
        this.bookType = bookType;
    }
    
    private OpenStaticBookScreenPacket(ResourceKey<?> bookKey, ResourceKey<BookLanguage> languageId, int translatedComprehension, BookType bookType) {
        this.bookKey = bookKey;
        this.languageId = languageId;
        this.translatedComprehension = translatedComprehension;
        this.bookType = bookType;
    }
    
    public static void encode(OpenStaticBookScreenPacket message, RegistryFriendlyByteBuf buf) {
        buf.writeResourceKey(message.bookKey);
        buf.writeResourceKey(message.languageId);
        buf.writeVarInt(message.translatedComprehension);
        buf.writeEnum(message.bookType);
    }
    
    public static OpenStaticBookScreenPacket decode(RegistryFriendlyByteBuf buf) {
        return new OpenStaticBookScreenPacket(buf.readResourceKey(RegistryKeysPM.BOOKS), buf.readResourceKey(RegistryKeysPM.BOOK_LANGUAGES), buf.readVarInt(), buf.readEnum(BookType.class));
    }
    
    public static void onMessage(OpenStaticBookScreenPacket message, CustomPayloadEvent.Context ctx) {
        if (FMLEnvironment.dist.isClient()) {
            ClientUtils.openStaticBookScreen(message.bookKey, message.languageId, message.translatedComprehension, message.bookType);
        }
    }
}
