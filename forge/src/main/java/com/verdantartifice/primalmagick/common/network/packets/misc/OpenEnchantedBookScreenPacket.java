package com.verdantartifice.primalmagick.common.network.packets.misc;

import com.mojang.datafixers.util.Either;
import com.verdantartifice.primalmagick.client.util.ClientUtils;
import com.verdantartifice.primalmagick.common.books.BookLanguage;
import com.verdantartifice.primalmagick.common.books.BookLanguagesPM;
import com.verdantartifice.primalmagick.common.books.BookType;
import com.verdantartifice.primalmagick.common.books.BookView;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;

/**
 * Packet sent from the server to trigger the opening of of an enchanted book on the client.
 * 
 * @author Daedalus4096
 */
public class OpenEnchantedBookScreenPacket implements IMessageToClient {
    public static final StreamCodec<RegistryFriendlyByteBuf, OpenEnchantedBookScreenPacket> STREAM_CODEC = StreamCodec.ofMember(OpenEnchantedBookScreenPacket::encode, OpenEnchantedBookScreenPacket::decode);

    protected final BookView view;
    
    public OpenEnchantedBookScreenPacket(Holder<Enchantment> ench, HolderLookup.Provider registries) {
        Holder<BookLanguage> lang = registries.lookupOrThrow(RegistryKeysPM.BOOK_LANGUAGES).getOrThrow(BookLanguagesPM.GALACTIC);
        this.view = new BookView(Either.right(ench), lang, 0);
    }
    
    protected OpenEnchantedBookScreenPacket(BookView view) {
        this.view = view;
    }
    
    public static void encode(OpenEnchantedBookScreenPacket message, RegistryFriendlyByteBuf buf) {
        BookView.STREAM_CODEC.encode(buf, message.view);
    }
    
    public static OpenEnchantedBookScreenPacket decode(RegistryFriendlyByteBuf buf) {
        return new OpenEnchantedBookScreenPacket(BookView.STREAM_CODEC.decode(buf));
    }
    
    public static void onMessage(OpenEnchantedBookScreenPacket message, CustomPayloadEvent.Context ctx) {
        if (FMLEnvironment.dist.isClient()) {
            ClientUtils.openStaticBookScreen(message.view, BookType.BOOK);
        }
    }
}
