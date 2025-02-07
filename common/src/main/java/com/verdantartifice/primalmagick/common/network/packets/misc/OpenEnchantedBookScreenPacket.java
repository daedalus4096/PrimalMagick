package com.verdantartifice.primalmagick.common.network.packets.misc;

import com.mojang.datafixers.util.Either;
import com.verdantartifice.primalmagick.client.util.ClientUtils;
import com.verdantartifice.primalmagick.common.books.BookLanguage;
import com.verdantartifice.primalmagick.common.books.BookLanguagesPM;
import com.verdantartifice.primalmagick.common.books.BookType;
import com.verdantartifice.primalmagick.common.books.BookView;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import commonnetwork.networking.data.PacketContext;
import commonnetwork.networking.data.Side;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;

/**
 * Packet sent from the server to trigger the opening of an enchanted book on the client.
 * 
 * @author Daedalus4096
 */
public class OpenEnchantedBookScreenPacket implements IMessageToClient {
    public static final ResourceLocation CHANNEL = ResourceUtils.loc("open_enchanted_book_screen");
    public static final StreamCodec<RegistryFriendlyByteBuf, OpenEnchantedBookScreenPacket> STREAM_CODEC = StreamCodec.ofMember(OpenEnchantedBookScreenPacket::encode, OpenEnchantedBookScreenPacket::decode);

    protected final BookView view;
    
    public OpenEnchantedBookScreenPacket(Holder<Enchantment> enchantmentHolder, HolderLookup.Provider registries) {
        Holder<BookLanguage> lang = registries.lookupOrThrow(RegistryKeysPM.BOOK_LANGUAGES).getOrThrow(BookLanguagesPM.GALACTIC);
        this.view = new BookView(Either.right(enchantmentHolder), lang, 0);
    }
    
    protected OpenEnchantedBookScreenPacket(BookView view) {
        this.view = view;
    }

    public static CustomPacketPayload.Type<CustomPacketPayload> type() {
        return new CustomPacketPayload.Type<>(CHANNEL);
    }

    public static void encode(OpenEnchantedBookScreenPacket message, RegistryFriendlyByteBuf buf) {
        BookView.STREAM_CODEC.encode(buf, message.view);
    }
    
    public static OpenEnchantedBookScreenPacket decode(RegistryFriendlyByteBuf buf) {
        return new OpenEnchantedBookScreenPacket(BookView.STREAM_CODEC.decode(buf));
    }
    
    public static void onMessage(PacketContext<OpenEnchantedBookScreenPacket> ctx) {
        OpenEnchantedBookScreenPacket message = ctx.message();
        if (Side.CLIENT.equals(ctx.side())) {
            ClientUtils.openStaticBookScreen(message.view, BookType.BOOK);
        }
    }
}
